package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The competence handler manages the localy cached competence objects
 * 
 * @author Michael
 */
public class CompetenceHandler implements Handler<Competence> {

	// the chached objects
	private List<Competence> competenceList = Collections.synchronizedList(new LinkedList<Competence>());
	private Logger log = LoggerFactory.getLogger(CompetenceHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
		synchronized (competenceList) {
			competenceList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
		synchronized (competenceList) {
			competenceList.clear();
			competenceList.addAll(message.getObjects());
		}

	}

	@Override
	public void remove(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
		synchronized (competenceList) {
			competenceList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Competence> message) throws SQLException, ServiceException {
		synchronized (competenceList) {
			for (Competence newCompetence : message.getObjects()) {
				if (!competenceList.contains(newCompetence))
					continue;
				int index = competenceList.indexOf(newCompetence);
				competenceList.set(index, newCompetence);
			}

		}
	}

	/**
	 * Returns the first competence that matches the given name.
	 * 
	 * @param competenceName
	 *            name the name of the competence to get
	 * @return the competence with the given name or null if nothing found
	 */
	public Competence getCompetenceByName(String competenceName) {
		synchronized (competenceList) {
			Iterator<Competence> iter = competenceList.iterator();
			while (iter.hasNext()) {
				Competence competence = iter.next();
				if (competence.getCompetenceName().equalsIgnoreCase(competenceName)) {
					return competence;
				}
			}
		}
		// nothing found
		return null;
	}

	/**
	 * Converts the list to an array
	 * 
	 * @return the list as a array
	 */
	public Competence[] toArray() {
		return competenceList.toArray(new Competence[competenceList.size()]);
	}
}
