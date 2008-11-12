package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LockHandler implements Handler<Lock> {

	// the list of locked object
	private final static List<Lock> lockedList = Collections.synchronizedList(new ArrayList<Lock>());
	private static Logger logger = LoggerFactory.getLogger(LockHandler.class);

	@Override
	public void add(ServerIoSession session, Message<Lock> message) throws ServiceException, SQLException {
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
	public void get(ServerIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		return lockedList;
	}

	@Override
	public void remove(ServerIoSession session, Message<Lock> message) throws ServiceException, SQLException {
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
	public void update(ServerIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = MessageType.UPDATE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void execute(ServerIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
