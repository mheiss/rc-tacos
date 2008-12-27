package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class CompetenceHandler implements Handler<Competence> {

	// the competence service
	@Service(clazz = CompetenceService.class)
	private CompetenceService competenceService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to add each competence object
		for (Competence competence : competenceList) {
			int id = competenceService.addCompetence(competence);
			// check for error while adding
			if (id == -1)
				throw new ServiceException("Failed to add the competence " + competence + " to the database");
			competence.setId(id);
		}
		session.writeResponseBrodcast(message, competenceList);
	}

	@Override
	public void get(MessageIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		// request the listing
		List<Competence> compList = competenceService.listCompetences();
		if (compList == null)
			throw new ServiceException("Failed to list the competences, service returned null");

		// check if we have locks for this competences
		for (Competence competence : compList) {
			Lockable lockable = lockableService.getLock(competence);
			if (lockable == null) {
				continue;
			}
			// set the data
			competence.setLocked(lockable.isLocked());
			competence.setLockedBy(lockable.getLockedBy());
		}

		// send the response back to the client
		session.writeResponse(message, compList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to remove each competence object
		for (Competence competence : competenceList) {
			if (!competenceService.removeCompetence(competence.getId()))
				throw new ServiceException("Failed to remove the competence " + competence);
			// remove the lockable
			lockableService.removeLock(competence);
		}
		session.writeResponseBrodcast(message, competenceList);
	}

	@Override
	public void update(MessageIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to update each competence object
		for (Competence competence : competenceList) {
			if (!competenceService.updateCompetence(competence))
				throw new ServiceException("Failed to update the competence: " + competence);
			// update the lockable
			lockableService.updateLock(competence);
		}
		session.writeResponseBrodcast(message, competenceList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Competence> message) throws ServiceException, SQLException {
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
	public Competence[] toArray() {
		return null;
	}
}
