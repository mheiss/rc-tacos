package at.rc.tacos.platform.services.dbal;

import java.util.List;

import at.rc.tacos.platform.model.Lockable;

public interface LockableService {

	/**
	 * Adds a new <code>Lockable</code> to the list of locked objects.
	 */
	public boolean addLock(Lockable lockable);

	/**
	 * Removes the existing <code>Lockable</code> objects
	 */
	public boolean removeLock(Lockable lockable);

	/**
	 * Updates the lockable object and syncronize the state
	 */
	public void updateLock(Lockable lockable);

	/**
	 * Returns whether or not the current object is in the list of
	 * <code>Lockable</code> objects.
	 */
	public boolean containsLock(Lockable lockable);

	/**
	 * Returns the <code>Lockable</code> instance as specified or null if not
	 * existing
	 */
	public Lockable getLock(Lockable lockable);

	// BULK OPERATIONS
	/**
	 * Adds all of the elements to the list of the locked objects
	 */
	public void addAllLocks(List<? extends Lockable> lockables);

	/**
	 * Removes all of the elements from the list of locked objects
	 */
	public void removeAllLocks(List<? extends Lockable> lockables);
}
