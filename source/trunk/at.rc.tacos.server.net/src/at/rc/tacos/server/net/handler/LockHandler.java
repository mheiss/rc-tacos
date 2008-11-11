package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LockHandler implements INetHandler<Lock> {

	// the list of locked object
	private final static List<Lock> lockedList = Collections.synchronizedList(new ArrayList<Lock>());
	private static Logger logger = LoggerFactory.getLogger(LockHandler.class);

	@Override
	public Lock add(Lock model) throws ServiceException, SQLException {
		// check if the list already contains the lock
		if (lockedList.contains(model)) {
			int index = lockedList.indexOf(model);
			logger.debug("The lock is existing, editing is denied");
			// set the username and return the message
			Lock existingLock = lockedList.get(index);
			model.setLockedBy(existingLock.getLockedBy());
		}
		else {
			logger.debug("The lock is not in the list of objects, editing is granted");
			// set the lock for this user
			model.setHasLock(true);
			synchronized (lockedList) {
				lockedList.listIterator().add(model);
			}
			return model;
		}
		logger.debug("The lock was not allowed: " + model);
		// do not allow the user to have the lock
		model.setHasLock(false);
		return model;
	}

	@Override
	public List<Lock> execute(String command, List<Lock> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Lock> get(Map<String, String> params) throws ServiceException, SQLException {
		return lockedList;
	}

	@Override
	public Lock remove(Lock model) throws ServiceException, SQLException {
		logger.debug("Request to remove the lock:" + model);
		// remove the lock from the list
		synchronized (lockedList) {
			ListIterator<Lock> iter = lockedList.listIterator();
			while (iter.hasNext()) {
				Lock listLock = iter.next();
				// check the object and remove it from the list
				if (listLock.equals(model)) {
					logger.debug("Removing lock:" + model);
					iter.remove();
					model.setHasLock(false);
					return model;
				}
			}
		}
		// the lock has not been removed
		model.setHasLock(true);
		return model;
	}

	@Override
	public Lock update(Lock model) throws ServiceException, SQLException {
		throw new NoSuchCommandException("update");
	}

}
