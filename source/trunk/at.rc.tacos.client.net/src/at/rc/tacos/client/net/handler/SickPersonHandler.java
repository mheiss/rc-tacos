package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>SickPersonHandler</code> manages the locally chached
 * {@link SickPerson} instances.
 * 
 * @author Michael
 */
public class SickPersonHandler implements Handler<SickPerson> {

	private List<SickPerson> personList = Collections.synchronizedList(new ArrayList<SickPerson>());
	private Logger log = LoggerFactory.getLogger(SickPersonHandler.class);

	@Override
	public void add(MessageIoSession session, Message<SickPerson> message) throws SQLException, ServiceException {
		synchronized (personList) {
			personList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<SickPerson> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<SickPerson> message) throws SQLException, ServiceException {
		synchronized (personList) {
			// add or update the sick persons
			for (SickPerson person : message.getObjects()) {
				int index = personList.indexOf(person);
				if (index == -1) {
					personList.add(person);
				}
				else {
					personList.set(index, person);
				}
			}
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<SickPerson> message) throws SQLException, ServiceException {
		synchronized (personList) {
			personList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<SickPerson> message) throws SQLException, ServiceException {
		synchronized (personList) {
			for (SickPerson updatedPerson : message.getObjects()) {
				if (!personList.contains(updatedPerson)) {
					continue;
				}
				int index = personList.indexOf(updatedPerson);
				personList.set(index, updatedPerson);
			}
		}
	}

	/**
	 * Returns the first <code>SickPerson</code> instance that exactly matches
	 * the integer value returned by {@link SickPerson#getSickPersonId()}.
	 * 
	 * @param id
	 *            the id of the <code>SickPerson</code> to search for
	 * @return the matched object or null if nothing found
	 */
	public SickPerson getSickPersonById(int id) {
		// loop and search
		for (SickPerson person : personList) {
			if (person.getSickPersonId() == id)
				return person;
		}
		// nothing found
		return null;
	}

	/**
	 * Returns a new array containing the managed <code>SickPerson</code>
	 * instances.
	 * 
	 * @return an array containing the <code>ServiceType</code> instances.
	 */
	@Override
	public SickPerson[] toArray() {
		return personList.toArray(new SickPerson[personList.size()]);
	}
}
