package at.rc.tacos.platform.model;

/**
 * This object represents a single lock for a model object
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

	// METHODS
	/**
	 * Returns a human readable string
	 */
	public String toString() {
		return "Locked " + clazz + ":" + objectId + " owner " + lockedBy;
	}

	/**
	 * Returns the calculated hash code based on the {@link Lock#objectId} and
	 * the {@link Lock#clazz}.
	 * 
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + objectId;
		return result;
	}

	/**
	 * Returns whether the objects are equal or not.
	 * <p>
	 * Two locks are equal if, and only if, the {@link Lock#objectId} and the
	 * {@link Lock#clazz} is the same.
	 * </p>
	 * 
	 * @return true if the lock is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lock other = (Lock) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		}
		else if (!clazz.equals(other.clazz))
			return false;
		if (objectId != other.objectId)
			return false;
		return true;
	}

	// GETTERS AND SETTERS
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
