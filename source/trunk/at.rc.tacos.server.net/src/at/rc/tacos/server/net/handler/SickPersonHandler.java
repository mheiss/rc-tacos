package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.SickPersonService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class SickPersonHandler implements Handler<SickPerson> {

	@Service(clazz = SickPersonService.class)
	private SickPersonService sickPersonService;

	@Override
	public void add(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		List<SickPerson> sickPersons = message.getObjects();
		// loop and add the new sick persons
		for (SickPerson person : sickPersons) {
			int id = sickPersonService.addSickPerson(person);
			if (id == -1)
				throw new ServiceException("Failed to add the sick person: " + person);
			// set the id
			person.setSickPersonId(id);
		}
		// brodcast the added persons
		session.writeBrodcast(message, sickPersons);
	}

	@Override
	public void get(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		// get the params out of the message
		Map<String, String> params = message.getParams();

		// query the results
		if (params.containsKey(IFilterTypes.SEARCH_STRING)) {
			// get the query filter
			final String lastNameFilter = params.get(IFilterTypes.SEARCH_STRING);
			List<SickPerson> personList = sickPersonService.getSickPersonList(lastNameFilter);
			if (personList == null) {
				throw new ServiceException("Failed to list the sick persons by lastname: " + lastNameFilter);
			}
			// send back the result
			session.writeBrodcast(message, personList);
		}

		// throw a exception because no params are passed
		throw new ServiceException("Listing of all sick persons is denied");
	}

	@Override
	public void remove(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		List<SickPerson> sickPersons = message.getObjects();
		// loop and remove the new sick persons
		for (SickPerson person : sickPersons) {
			if (!sickPersonService.removeSickPerson(person.getSickPersonId()))
				throw new ServiceException("Failed to remove the sick person " + person);
		}
		// brodcast the removed persons
		session.writeBrodcast(message, sickPersons);
	}

	@Override
	public void update(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		List<SickPerson> sickPersons = message.getObjects();
		// loop and updated the new sick persons
		for (SickPerson person : sickPersons) {
			if (!sickPersonService.updateSickPerson(person))
				throw new ServiceException("Failed to update the sick person " + person);
		}
		// brodcast the updated persons
		session.writeBrodcast(message, sickPersons);
	}

	@Override
	public void execute(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
