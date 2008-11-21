package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>LockHandler</code> manages the locally chached {@link Lock}
 * instances.
 * 
 * @author Michael
 */
public class LockHandler implements Handler<Lock> {

	private List<Lock> lockList = Collections.synchronizedList(new LinkedList<Lock>());
	private Logger log = LoggerFactory.getLogger(LocationHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Lock> message) throws SQLException, ServiceException {
		synchronized (lockList) {
			lockList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Lock> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Lock> message) throws SQLException, ServiceException {
		synchronized (lockList) {
			lockList.clear();
			lockList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Lock> message) throws SQLException, ServiceException {
		synchronized (lockList) {
			lockList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Lock> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	/**
	 * Returns whether or not a lock is existing for the {@link Lock#getClass()}
	 * identified by the {@link Lock#getObjectId()} and the
	 * 
	 * @param id
	 *            the id of the object to check for the lock
	 * @param clazz
	 *            the class object to check
	 * @return true if the object is in the list of the locked object
	 */
	public boolean containsLock(int id, Class<?> clazz) {
		// create a new lock object
		Lock lock = new Lock(id, clazz, "");
		// check if this lock is existing
		return lockList.contains(lock);
	}

}
