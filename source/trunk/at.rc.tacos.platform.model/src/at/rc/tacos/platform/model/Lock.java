package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This object represents a single lock for a model object.
 * <p>
 * A lock object is used to prevent simultaneous editing of the same data clazz
 * instances by two ore more different users
 * </p>
 * <p>
 * A lock is typically defined using the clazz of the data object and the unique
 * identifier.
 * </p>
 * 
 * @author Michael
 */
public class Lock {

	private int objectId;
	private String clazz;
	private String lockedBy;
	private boolean hasLock;

	/**
	 * Class constructor to set up a new lock object.
	 * 
	 * @param objectId
	 *            the id of the object to create the lock
	 * @param clazz
	 *            the clazz to create the lock for
	 * @param lockedBy
	 *            the username to identify who owns the lock
	 */
	public Lock(int objectId, Class<?> clazz, String lockedBy) {
		this.objectId = objectId;
		this.clazz = clazz.getName();
		this.lockedBy = lockedBy;
	}

	/**
	 * Returns the human readable string for this <code>Lock</code> instance.
	 * 
	 * @return the build string
	 */
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("clazz", clazz);
		builder.append("objectId", objectId);
		builder.append("lockedBy", lockedBy);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Lock</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Lock#getClass()} and
	 * {@link Lock#getObjectId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(33, 43);
		builder.append(clazz);
		builder.append(objectId);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Lock</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Lock#getClass()} and
	 * {@link Lock#getObjectId()}
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
		Lock lock = (Lock) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(clazz, lock.clazz);
		builder.append(objectId, lock.objectId);
		return builder.isEquals();
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public boolean isHasLock() {
		return hasLock;
	}

	public void setHasLock(boolean hasLock) {
		this.hasLock = hasLock;
	}
}
