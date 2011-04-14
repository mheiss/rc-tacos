package at.redcross.tacos.web.beans.dto;

public enum EntryState {

    /**
     * The given entity is new (not in the database)
     */
    NEW,

    /**
     * The given entity is sync
     */
    SYNC,

    /**
     * The given entity has errors
     */
    ERROR,

    /**
     * The given entity has warnings
     */
    WARN,

    /**
     * The given entity should be removed
     */
    DELETE;

    public String getName() {
        return name().toLowerCase();
    }
}
