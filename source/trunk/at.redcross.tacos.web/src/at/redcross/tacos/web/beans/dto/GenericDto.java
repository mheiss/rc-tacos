package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;

import at.redcross.tacos.dbal.entity.EntityImpl;

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
	protected DtoState state = DtoState.SYNC;

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

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public T getEntity() {
		return entity;
	}

	public DtoState getState() {
		return state;
	}

	public boolean isSelected() {
		return selected;
	}
}
