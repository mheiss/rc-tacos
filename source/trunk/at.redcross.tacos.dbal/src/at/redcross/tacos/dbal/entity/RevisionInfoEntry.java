package at.redcross.tacos.dbal.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.RevisionType;

/** The {@code AuditEntry} is the result of an {@code AuditQuery} */
public class RevisionInfoEntry implements Serializable {

    private static final long serialVersionUID = 3540952605036259413L;

    /** the wrapped entity */
    private final EntityImpl entity;

    /** the revision entity */
    private final RevisionInfo revision;

    /** the audit type */
    private final RevisionType type;

    /** the changes */
    private final List<RevisionInfoChange> changes;

    /**
     * Creates a new entry using the given array of objects.
     */
    public RevisionInfoEntry(Object[] objects) {
        entity = (EntityImpl) objects[0];
        revision = (RevisionInfo) objects[1];
        type = (RevisionType) objects[2];
        changes = new ArrayList<RevisionInfoChange>();
    }

    /** Appends the given info string to this revision entry */
    public void addChange(RevisionInfoChange change) {
        changes.add(change);
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public EntityImpl getEntity() {
        return entity;
    }

    public RevisionInfo getRevision() {
        return revision;
    }

    public RevisionType getType() {
        return type;
    }

    public List<RevisionInfoChange> getChanges() {
        return changes;
    }
}
