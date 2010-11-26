package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import at.redcross.tacos.dbal.entity.RevisionInfoEntry;
import at.redcross.tacos.dbal.helper.AuditQueryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "historyBean")
public class HistoryBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    /** the primary key of the entity to show the revision */
    private Long primaryKey;

    /** the class to show the history information */
    private String className;

    /** the entity for that the history is shown */
    private EntityImpl entity;

    /** the history entries */
    private List<RevisionInfoEntry> revisionEntries;

    /** the current page */
    private Integer currentPage = Integer.valueOf(0);

    @Override
    protected void init() throws Exception {
        // nothing to do
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
            auditQuery.setMaxResults(20);
            auditQuery.setFirstResult(currentPage);
            revisionEntries = AuditQueryHelper.listRevisions(auditQuery);
            computeChanges(revisionEntries);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Änderungshistorie konnte nicht geladen werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /** Computes the changes for the given revisions */
    private void computeChanges(List<RevisionInfoEntry> entires) throws Exception {
        EntityImpl baseEntity = entires.iterator().next().getEntity();
        for (RevisionInfoEntry entry : entires) {
            // entry is added, no other compares needed
            if (entry.getType() == RevisionType.ADD) {
                entry.addChange("Datensatz wurde hinzugefügt");
            }
            if (entry.getType() == RevisionType.DEL) {
                entry.addChange("Datensatz wurde gelöscht");
            }
            if (entry.getType() == RevisionType.MOD) {
                // compare each property of the given entity
                List<String> changes = computeChanges(baseEntity, entry.getEntity());
                for (String change : changes) {
                    entry.addChange(change);
                }
                // compare the next entry with this one
                baseEntity = entry.getEntity();
            }
        }
    }

    /** Computes the changes between the two entities */
    @SuppressWarnings("unchecked")
    private List<String> computeChanges(EntityImpl lhsEntity, EntityImpl rhsEntity) throws Exception {
        List<String> changes = new ArrayList<String>();
        // compute the changes and add to the result
        Map<String, Object> lhsProperties = BeanUtils.describe(lhsEntity);
        for (String property : lhsProperties.keySet()) {
            // skip history property
            if (property.startsWith("history")) {
                continue;
            }

            // get the value of the property and compute the changes
            Object lhsProperty = PropertyUtils.getProperty(lhsEntity, property);
            Object rhsProperty = PropertyUtils.getProperty(rhsEntity, property);
            String changed = computeChanges(lhsProperty, rhsProperty);

            // no changes detected
            if (changed == null) {
                continue;
            }
            changes.add(property + " - " + changed);
        }
        return changes;
    }

    /** Returns a string containing the changes between the objects */
    @SuppressWarnings("unchecked")
    private String computeChanges(Object lhs, Object rhs) {
        // simple cases where one or two sides are not set
        if (rhs == null && lhs == null) {
            return null;
        }
        if (rhs == null && lhs != null) {
            return String.valueOf(lhs) + " -> " + "(null) ";
        }
        if (rhs != null && lhs == null) {
            return "(null)" + " -> " + String.valueOf(rhs);
        }

        // compare entities
        if (rhs instanceof EntityImpl && lhs instanceof EntityImpl) {
            EntityImpl rhsEntity = (EntityImpl) rhs;
            EntityImpl lhsEntity = (EntityImpl) lhs;
            if (rhsEntity.getDisplayString().equals(lhsEntity.getDisplayString())) {
                return null;
            }
            return lhsEntity.getDisplayString() + " -> " + rhsEntity.getDisplayString();
        }

        // compare objects
        if (rhs instanceof Comparable<?> && lhs instanceof Comparable<?>) {
            Comparable<Object> rhsComparable = (Comparable<Object>) rhs;
            Comparable<Object> lhsComparable = (Comparable<Object>) lhs;
            if (rhsComparable.compareTo(lhsComparable) == 0) {
                return null;
            }
            return String.valueOf(lhs) + " -> " + String.valueOf(rhs);
        }
        return null;
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

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public EntityImpl getEntity() {
        return entity;
    }

    public List<RevisionInfoEntry> getRevisionEntries() {
        return revisionEntries;
    }
}
