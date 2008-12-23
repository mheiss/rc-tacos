package at.rc.tacos.server.dbal.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.services.dbal.LockableService;

/**
 * The <code>LockableServiceImpl</code> holds the objects that are currently
 * edited and should not be modified.
 * 
 * @author Michael
 */
public class LockableServiceImpl implements LockableService {

	private Logger logger = LoggerFactory.getLogger(LockableServiceImpl.class);

	private final List<Lockable> locks = Collections.synchronizedList(new ArrayList<Lockable>());

	@Override
	public boolean addLock(Lockable lockable) {
		logger.debug("Adding new lock " + lockable);
		synchronized (locks) {
			if (locks.contains(lockable)) {
				return true;
			}
			locks.add(lockable);
		}
		return true;
	}

	@Override
	public boolean containsLock(Lockable lockable) {
		synchronized (locks) {
			return locks.contains(lockable);
		}
	}

	@Override
	public Lockable getLock(Lockable lockable) {
		// check if we have a object
		if (locks.contains(lockable)) {
			return null;
		}
		synchronized (locks) {
			int index = locks.indexOf(lockable);
			return locks.get(index);
		}
	}

	@Override
	public boolean removeLock(Lockable lockable) {
		logger.debug("Removing existing lock " + lockable);
		synchronized (locks) {
			return locks.remove(lockable);
		}
	}

	@Override
	public void updateLock(Lockable lockable) {
		// check if the object is locked and we alread have the lock
		if (lockable.isLocked() && locks.contains(lockable)) {
			logger.debug("Updating existing lock object " + lockable);
			return;
		}
		// if the object is locked but we do not have the object
		if (lockable.isLocked() & !locks.contains(lockable)) {
			addLock(lockable);
			return;
		}
		// object is not locked but we have it
		if (!lockable.isLocked() && locks.contains(lockable)) {
			removeLock(lockable);
			return;
		}
	}

	// BULK OPERATION IMPLEMENTATION
	@Override
	public void addAllLocks(List<? extends Lockable> lockables) {
		logger.debug("Adding new locks " + lockables);
		synchronized (locks) {
			locks.addAll(lockables);
		}
	}

	@Override
	public void removeAllLocks(List<? extends Lockable> lockables) {
		logger.debug("Removing existing locks " + lockables);
		synchronized (locks) {
			locks.removeAll(lockables);
		}
	}
}
