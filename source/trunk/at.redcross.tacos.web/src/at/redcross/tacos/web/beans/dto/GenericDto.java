package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;

import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;

/**
 * The {@code GenericDto} is a wrapper for e {@linkplain EntityImpl entity} that
 * provides an additional status flag.
 * 
 * @param <T>
 *            the type of the entity to wrapp
 */
public class GenericDto<T extends EntityImpl> implements Serializable {

    private static final long serialVersionUID = 7715478489903222917L;

    /** the object to be wrapped */
    protected final T entity;

    /** this object is selected */
    protected boolean selected;

    /** the current state of this DTO */
    protected EntryState state = EntryState.SYNC;

    /**
     * Creates a new generic DTO object wrapping the given entity
     * 
     * @param entity
     *            the entity to wrap
     */
    public GenericDto(T entity) {
        this.entity = entity;
    }

    // ---------------------------------
    // Business relevant methods
    // ---------------------------------
    /**
     * Returns whether or not the current authenticated user can view the
     * history of the entry.
     * <ul>
     * <li>Principal must have the permission to view the history</li>
     * </ul>
     */
    public boolean isHistoryEnabled() {
        return FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToViewHistory();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setState(EntryState state) {
        this.state = state;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getEntityClass() {
        return entity.getClass().getName();
    }

    public T getEntity() {
        return entity;
    }

    public EntryState getState() {
        return state;
    }

    public boolean isSelected() {
        return selected;
    }

}
