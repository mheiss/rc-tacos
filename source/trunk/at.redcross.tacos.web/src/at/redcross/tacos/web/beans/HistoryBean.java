package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.RevisionInfoChange;
import at.redcross.tacos.dbal.entity.RevisionInfoEntry;
import at.redcross.tacos.dbal.helper.AuditQueryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "historyBean")
public class HistoryBean extends PagingBean {

    private static final long serialVersionUID = 1L;

    /** the primary key of the entity to show the revision */
    private Long primaryKey;

    /** the class to show the history information */
    private String className;

    /** the entity for that the history is shown */
    private EntityImpl entity;

    /** the history entries */
    private List<RevisionInfoEntry> revisionEntries;

    /** format dates using the provided formatter */
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @PostConstruct
    public void setup() {
        maxResults = 10;
    }

    @Override
    protected void init() throws Exception {
    }

    public void queryHistory(ActionEvent event) {
        Class<?> entityClass;
        EntityManager manager = null;
        try {
            entityClass = Class.forName(className);
            // query the entity by the primary key
            manager = EntityManagerFactory.createEntityManager();
            entity = (EntityImpl) manager.find(entityClass, primaryKey);

            // look for history data
            AuditReader auditReader = AuditReaderFactory.get(manager);
            AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(entityClass,
                    false, true);
            auditQuery = auditQuery.add(AuditEntity.id().eq(primaryKey));
            auditQuery.addOrder(AuditEntity.revisionNumber().asc());
            revisionEntries = AuditQueryHelper.listRevisions(auditQuery);
            computeChanges(revisionEntries);
            Collections.reverse(revisionEntries);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Änderungshistorie konnte nicht geladen werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /** Computes the changes for the given revisions */
    private void computeChanges(List<RevisionInfoEntry> entries) throws Exception {
        // check if we have something to do
        if (entries.isEmpty()) {
            return;
        }
        EntityImpl baseEntity = entries.iterator().next().getEntity();
        for (RevisionInfoEntry entry : entries) {
            // entry is added, no other compares needed
            if (entry.getType() == RevisionType.ADD) {
                continue;
            }
            if (entry.getType() == RevisionType.DEL) {
                continue;
            }
            if (entry.getType() == RevisionType.MOD) {
                // compare each property of the given entity
                entry.addChanges(computeChanges(baseEntity, entry.getEntity()));

                // compare the next entry with this one
                baseEntity = entry.getEntity();
            }
        }
    }

    /** Computes the changes between the two entities */
    @SuppressWarnings("unchecked")
    private List<RevisionInfoChange> computeChanges(EntityImpl lhsEntity, EntityImpl rhsEntity) throws Exception {
        List<RevisionInfoChange> changes = new ArrayList<RevisionInfoChange>();
        // compute the changes and add to the result
        Map<String, Object> properties = BeanUtils.describe(lhsEntity);
        for (String property : properties.keySet()) {
            // skip history property
            if (property.startsWith("history")) {
                continue;
            }
            // skip internal methods from entity
            if (property.startsWith("displayString")) {
                continue;
            }
            RevisionInfoChange change = new RevisionInfoChange(property);

            // get the value of the property and compute the changes
            Object lhsProperty = PropertyUtils.getProperty(lhsEntity, property);
            Object rhsProperty = PropertyUtils.getProperty(rhsEntity, property);
            if (!computeChanges(change, lhsProperty, rhsProperty)) {
                continue;
            }
            changes.add(change);
        }
        return changes;
    }

    /** Returns whether or not there are changes between the two objects */
    @SuppressWarnings("unchecked")
    private boolean computeChanges(RevisionInfoChange change, Object lhs, Object rhs) {
        // simple cases where one or two sides are not set
        if (rhs == null && lhs == null) {
            return false;
        }
        // the value is deleted
        if (rhs == null && lhs != null) {
            change.setOldValue(getContent(lhs));
            change.setNewValue(null);
            return true;
        }
        // initial value set, ignore (null) to empty string
        String content = getContent(rhs);
        if (rhs != null && lhs == null) {
            if (content.trim().isEmpty()) {
                return false;
            }
            change.setNewValue(content);
            return true;
        }

        // compare entities
        if (rhs instanceof EntityImpl && lhs instanceof EntityImpl) {
            EntityImpl rhsEntity = (EntityImpl) rhs;
            EntityImpl lhsEntity = (EntityImpl) lhs;
            if (rhsEntity.getDisplayString().equals(lhsEntity.getDisplayString())) {
                return false;
            }
            change.setNewValue(getContent(rhsEntity));
            change.setOldValue(getContent(lhsEntity));
            return true;
        }

        // compare objects
        if (rhs instanceof Comparable<?> && lhs instanceof Comparable<?>) {
            Comparable<Object> rhsComparable = (Comparable<Object>) rhs;
            Comparable<Object> lhsComparable = (Comparable<Object>) lhs;
            if (rhsComparable.compareTo(lhsComparable) == 0) {
                return false;
            }
            change.setNewValue(content);
            change.setOldValue(getContent(lhs));
            return true;
        }
        return false;
    }

    /** Helper method to get the current value of an object as formatted string */
    private String getContent(Object object) {
        if (object instanceof EntityImpl) {
            EntityImpl entity = (EntityImpl) object;
            return entity.getDisplayString();
        }
        if (object instanceof Date) {
            Date date = (Date) object;
            return sdf.format(date);
        }
        return String.valueOf(object);
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public Long getPrimaryKey() {
        return primaryKey;
    }

    public String getClassName() {
        return className;
    }

    public EntityImpl getEntity() {
        return entity;
    }

    public List<RevisionInfoEntry> getRevisionEntries() {
        return revisionEntries;
    }
}
