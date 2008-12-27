package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class DiseaseHandler implements Handler<Disease> {

	@Service(clazz = DiseaseService.class)
	private DiseaseService diseaseService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to add each object
		for (Disease disease : diseaseList) {
			int id = diseaseService.addDisease(disease);
			if (id == -1)
				throw new ServiceException("Failed to add the disease " + disease);
			// set the generated id
			disease.setId(id);
		}
		session.writeResponseBrodcast(message, diseaseList);
	}

	@Override
	public void get(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {

		// request the listing of all disease objects
		List<Disease> diseaseList = diseaseService.getDiseaseList();
		if (diseaseList == null)
			throw new ServiceException("Failed to list the diseases");

		// check for locks
		for (Disease disease : diseaseList) {
			if (!lockableService.containsLock(disease)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(disease);
			disease.setLocked(lockable.isLocked());
			disease.setLockedBy(lockable.getLockedBy());
		}

		// send the list back
		session.writeResponse(message, diseaseList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to remove each object
		for (Disease disease : diseaseList) {
			if (!diseaseService.removeDisease(disease.getId()))
				throw new ServiceException("Failed to remove the disease: " + disease);
			// remove the lock
			lockableService.removeLock(disease);
		}
		session.writeResponseBrodcast(message, diseaseList);
	}

	@Override
	public void update(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to update each object
		for (Disease disease : diseaseList) {
			if (!diseaseService.updateDisease(disease))
				throw new ServiceException("Failed to update the disease: " + disease);
			// update the locks
			lockableService.updateLock(disease);
		}
		session.writeResponseBrodcast(message, diseaseList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public Disease[] toArray() {
		return null;
	}
}
