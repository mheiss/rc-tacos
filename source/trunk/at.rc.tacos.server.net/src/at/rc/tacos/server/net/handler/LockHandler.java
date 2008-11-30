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
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LockHandler implements Handler<Lock> {

	// the list of locked object
	private final static List<Lock> lockedList = Collections.synchronizedList(new ArrayList<Lock>());
	private static Logger logger = LoggerFactory.getLogger(LockHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		List<Lock> newLocks = message.getObjects();
		// loop and add the locks
		synchronized (lockedList) {
			for (Lock lock : newLocks) {
				// check if the list already contains the lock
				if (lockedList.contains(lock)) {
					int index = lockedList.indexOf(lock);
					logger.debug("The lock is existing, editing is denied");
					// set the username and return the message
					Lock existingLock = lockedList.get(index);
					lock.setLockedBy(existingLock.getLockedBy());
				}
				else {
					logger.debug("The object is not locked, editing is granted");
					// set the lock for this user
					lock.setHasLock(true);
					lock.setLockedBy(session.getUsername());
					synchronized (lockedList) {
						lockedList.listIterator().add(lock);
					}
				}
			}
		}
		// brodcast the new locks to all clients
		session.writeBrodcast(message, newLocks);
	}

	@Override
	public void get(MessageIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// send the current list of locks
		session.write(message, lockedList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// loop and remove the locks
		List<Lock> newLocks = message.getObjects();
		synchronized (lockedList) {
			for (Lock lock : newLocks) {
				logger.debug("Request to remove the lock:" + lock);
				// remove the lock from the list
				ListIterator<Lock> iter = lockedList.listIterator();
				while (iter.hasNext()) {
					Lock listLock = iter.next();
					// check the object and remove it from the list
					if (listLock.equals(lock)) {
						logger.debug("Removing lock:" + lock);
						iter.remove();
						lock.setHasLock(false);
					}
				}
			}
		}
		// brodcast the removed lock
		session.writeBrodcast(message, newLocks);
	}

	@Override
	public void update(MessageIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = MessageType.UPDATE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void execute(MessageIoSession session, Message<Lock> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
