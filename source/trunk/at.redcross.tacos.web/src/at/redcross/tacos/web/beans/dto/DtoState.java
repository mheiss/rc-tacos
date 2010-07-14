package at.redcross.tacos.web.beans.dto;

public enum DtoState {

    /**
     * The given entity is new (not in the database)
     */
    NEW,

    /**
     * The given entity is sync
     */
    SYNC,

    /**
     * The given entity should be removed
     */
    DELETE;

    public String getName() {
        return name().toLowerCase();
    }
}
