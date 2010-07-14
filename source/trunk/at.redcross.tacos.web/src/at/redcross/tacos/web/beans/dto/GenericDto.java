package at.redcross.tacos.web.beans.dto;

import at.redcross.tacos.dbal.entity.EntityImpl;

/**
 * The {@code GenericDto} is a wrapper for e {@linkplain EntityImpl entity} that
 * provides an additional status flag.
 * 
 * @param <T>
 *            the type of the entity to wrapp
 */
public class GenericDto<T extends EntityImpl> {

    /** the object to be wrapped */
    private final T entity;

    /** the current state of this DTO */
    private DtoState state = DtoState.SYNC;

    /**
     * Creates a new generic DTO object wrapping the given entity
     * 
     * @param entity
     *            the entity to wrap
     */
    public GenericDto(T entity) {
        this.entity = entity;
    }

    public void setState(DtoState state) {
        this.state = state;
    }

    public T getEntity() {
        return entity;
    }

    public DtoState getState() {
        return state;
    }
}
