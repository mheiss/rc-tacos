package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class CompetenceHandler implements Handler<Competence> {

	// the competence service
	@Service(clazz = CompetenceService.class)
	private CompetenceService competenceService;

	@Override
	public void add(ServerIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to add each competence object
		for (Competence competence : competenceList) {
			int id = competenceService.addCompetence(competence);
			// check for error while adding
			if (id == -1)
				throw new ServiceException("Failed to add the competence " + competence + " to the database");
			competence.setId(id);
		}
		session.writeBrodcast(message, competenceList);
	}

	@Override
	public void get(ServerIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		// request the listing
		List<Competence> compList = competenceService.listCompetences();
		if (compList == null)
			throw new ServiceException("Failed to list the competences, service returned null");

		// send the response back to the client
		session.write(message, compList);
	}

	@Override
	public void remove(ServerIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to remove each competence object
		for (Competence competence : competenceList) {
			if (!competenceService.removeCompetence(competence.getId()))
				throw new ServiceException("Failed to remove the competence " + competence);
		}
		session.writeBrodcast(message, competenceList);
	}

	@Override
	public void update(ServerIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		List<Competence> competenceList = message.getObjects();
		// loop and try to update each competence object
		for (Competence competence : competenceList) {
			if (!competenceService.updateCompetence(competence))
				throw new ServiceException("Failed to update the competence: " + competence);
		}
		session.writeBrodcast(message, competenceList);
	}

	@Override
	public void execute(ServerIoSession session, Message<Competence> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
