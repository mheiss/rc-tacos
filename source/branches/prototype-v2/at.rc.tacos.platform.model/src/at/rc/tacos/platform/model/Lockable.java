package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The <code>Lockable</code> is the abstract base class for all objects that can
 * be modfied by different users at the same time and should provide a mechanism
 * to detect and prevent this.
 * 
 * @author Michael
 */
public abstract class Lockable {

	// information about the lock
	private boolean locked;
	private String lockedBy;

	/**
	 * Returns the unique identifier of the lockable object
	 */
	public abstract int getLockedId();

	/**
	 * Returns the class for the lockable object
	 */
	public abstract Class<?> getLockedClass();

	/**
	 * Returns the human readable string for this <code>Lockable</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("lockedId", getLockedId());
		builder.append("lockedClass", getLockedClass());
		builder.append("locked", locked);
		builder.append("lockedBy", lockedBy);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Lockable</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Lockable#getLockedId()} and
	 * {@link Lockable#getLockedClass()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(59, 69);
		builder.append(getLockedId());
		builder.append(getLockedClass());
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Lockable</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Lockable#getLockedId()} and
	 * {@link Lockable#getLockedClass()}
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Lockable lockable = (Lockable) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(getLockedId(), lockable.getLockedId());
		builder.append(getLockedClass(), lockable.getLockedClass());
		return builder.isEquals();
	}

	// GETTERS AND SETTERS FOR THE LOCK INFORMATIOn

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public String getLockedBy() {
		return lockedBy;
	}
}
