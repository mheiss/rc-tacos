package at.redcross.tacos.dbal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

import at.redcross.tacos.dbal.entity.EntityImpl;

/** Computes the changes between tow different objects */
public class ObjectComparator {

    private final static Log logger = LogFactory.getLog(ObjectComparator.class);

    /** the left side of the comparator */
    private final Object leftSide;

    /** the right side of the comparator */
    private final Object rightSide;

    /** Format date value */
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    /** The entity manager instance */
    private EntityManager manager;

    /**
     * Creates a new comparator for the given object
     * 
     * @param leftSide
     *            the older instance
     * @param rightSide
     *            the newer instance
     */
    public ObjectComparator(Object leftSide, Object rightSide) {
        if (leftSide == null || rightSide == null) {
            throw new IllegalArgumentException("The object to compare cannot be null");
        }
        if (leftSide.getClass() != rightSide.getClass()) {
            throw new IllegalArgumentException("Cannot compare different objects");
        }
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    /** Sets the entity manager instance */
    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    /** Compares the objects and returns the result */
    public List<ObjectChange> getChanges() {
        List<ObjectChange> changes = new ArrayList<ObjectChange>();
        doGetChanges(changes, leftSide, rightSide);
        return changes;
    }

    /** Returns the changes between the tow objects */
    private void doGetChanges(List<ObjectChange> changes, Object lhs, Object rhs) {
        // get a list of all excluded fields
        Class<?> clazz = lhs.getClass();
        AuditOverride[] overrides = getAuditOverrides(clazz);

        // take one of the tow instances to get all fields
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            // check if the field is excluded
            String fieldName = field.getName();
            if (!shouldAnalyze(fieldName, overrides)) {
                continue;
            }

            // get the value of the field
            Object leftValue = getContent(field, lhs);
            Object rightValue = getContent(field, rhs);

            // simple cases where one or two sides are not set
            if (leftValue == null && rightValue == null) {
                continue;
            }
            // initial value set, ignore (null) to empty string
            if (rightValue != null && leftValue == null) {
                String content = getFormattedValue(rightValue);
                if (content == null || content.trim().isEmpty()) {
                    continue;
                }
                ObjectChange change = new ObjectChange(fieldName, content, null);
                changes.add(change);
                continue;
            }

            // the value is deleted
            if (rightValue == null && leftValue != null) {
                ObjectChange change = new ObjectChange(fieldName, leftValue, rightValue);
                changes.add(change);
                continue;
            }

            // check if this is an embedded field
            if (field.isAnnotationPresent(EmbeddedComparison.class)) {
                doGetChanges(changes, leftValue, rightValue);
                continue;
            }

            // compare objects
            if (leftValue instanceof Comparable<?> && rightValue instanceof Comparable<?>) {
                @SuppressWarnings("unchecked")
                Comparable<Object> rhsComparable = (Comparable<Object>) rightValue;
                @SuppressWarnings("unchecked")
                Comparable<Object> lhsComparable = (Comparable<Object>) leftValue;
                if (rhsComparable.compareTo(lhsComparable) == 0) {
                    continue;
                }
                String newValue = getFormattedValue(rightValue);
                String oldValue = getFormattedValue(leftValue);
                ObjectChange change = new ObjectChange(fieldName, newValue, oldValue);
                changes.add(change);
                continue;
            }

            // compare collections
            if (leftValue instanceof List<?> && rightValue instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<EntityImpl> rhsCollection = (List<EntityImpl>) rightValue;
                @SuppressWarnings("unchecked")
                List<EntityImpl> lhsCollection = (List<EntityImpl>) leftValue;
                // sort the collections according to the primary key
                Collections.sort(rhsCollection, new EntityComparator());
                Collections.sort(lhsCollection, new EntityComparator());

                String lhsValue = getFormattedValue(lhsCollection);
                String rhsValue = getFormattedValue(rhsCollection);
                if (lhsValue.equals(rhsValue)) {
                    continue;
                }
                ObjectChange change = new ObjectChange(fieldName, rhsValue, lhsValue);
                changes.add(change);
                continue;
            }
        }
    }

    /** Returns the current value of an object as formatted string */
    private String getFormattedValue(Object value) {
        if (value instanceof EntityImpl) {
            EntityImpl entity = (EntityImpl) value;
            return String.valueOf(entity.getOid());
        }
        if (value instanceof Date) {
            Date date = (Date) value;
            return sdf.format(date);
        }
        if (value instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) value;
            StringBuilder builder = new StringBuilder();
            Iterator<?> iter = collection.iterator();
            while (iter.hasNext()) {
                Object next = iter.next();
                String nextString = next.toString();
                if (next instanceof EntityImpl) {
                    nextString = ((EntityImpl) next).getDisplayString();
                }
                builder.append(nextString);
                if (iter.hasNext()) {
                    builder.append(",");
                }
            }
            return builder.toString();
        }
        return String.valueOf(value);

    }

    /** Returns the content of the given field */
    private Object getContent(Field field, Object object) {
        try {
            // make the first character an upper case
            String fieldName = field.getName();
            char firstChar = fieldName.charAt(0);

            // determine the type of this field
            String methodPrefix = "get";
            if (field.getType() == Boolean.TYPE) {
                methodPrefix = "is";
            }
            String methodName = methodPrefix + Character.toUpperCase(firstChar)
                    + fieldName.substring(1);
            Class<?> clazz = field.getDeclaringClass();
            Method method = clazz.getMethod(methodName, (Class<?>[]) null);
            Object content = method.invoke(object, (Object[]) null);
            return handleProxyObject(content);
        } catch (Exception ex) {
            logger.error("Failed to get value from field '" + field.getName() + "'", ex);
            return null;
        }
    }

    /** Handles the case that the entity has no history (_AUD) record entry */
    @SuppressWarnings("unchecked")
    private Object handleProxyObject(Object object) {
        if (!(object instanceof EntityImpl)) {
            return object;
        }

        // access any arbitrary attribute will throw an exception if the entity
        // is not existing
        EntityImpl entity = (EntityImpl) object;
        try {
            entity.getOid();
            return entity;
        } catch (EntityNotFoundException enfe) {
            // access the proxy object to get direct access to the identifier
            try {
                Class<?> clazz = object.getClass();
                Field field = clazz.getDeclaredField("handler");
                field.setAccessible(true);
                JavassistLazyInitializer handler = (JavassistLazyInitializer) field.get(entity);
                return manager.find(handler.getPersistentClass(), handler.getIdentifier());
            } catch (Exception ex) {
                logger.error("Failed to find entity", ex);
                return null;
            }
        }
    }

    /** Returns whether or not the given field should be analyzed */
    private boolean shouldAnalyze(String field, AuditOverride[] overrides) {
        for (AuditOverride override : overrides) {
            if (!field.equals(override.name()) && !override.isAudited()) {
                return false;
            }
        }
        return true;
    }

    /** Returns a list of all audit overrides for the given class */
    private AuditOverride[] getAuditOverrides(Class<?> clazz) {
        AuditOverrides auditOverrides = clazz.getAnnotation(AuditOverrides.class);
        if (auditOverrides != null) {
            return auditOverrides.value();
        }
        return new AuditOverride[0];
    }

    /** Returns a list of all fields of the given class */
    private Field[] getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        doGetFields(fields, clazz);
        return fields.toArray(new Field[fields.size()]);
    }

    private void doGetFields(List<Field> fields, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isNative(modifiers)) {
                continue;
            }
            if (Modifier.isStatic(modifiers)) {
                continue;
            }
            fields.add(field);
        }
        // check for the base class
        if (clazz.getSuperclass() == null) {
            return;
        }
        doGetFields(fields, clazz.getSuperclass());
    }

}
