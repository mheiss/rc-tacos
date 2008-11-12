package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		// add the location
		int id = sickPersonService.addSickPerson(model);
		if (id == -1)
			throw new ServiceException("Failed to add the sick person: " + model);
		// set the id
		model.setSickPersonId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		List<SickPerson> personList = new ArrayList<SickPerson>();
		// if there is no filter -> throw exception, this is not allowed
		if (params == null || params.isEmpty()) {
			throw new ServiceException("Listing of all sick persons is denied");
		}
		else if (params.containsKey(IFilterTypes.SEARCH_STRING)) {
			// get the query filter
			final String lastNameFilter = params.get(IFilterTypes.SEARCH_STRING);
			personList = sickPersonService.getSickPersonList(lastNameFilter);
			if (personList == null) {
				throw new ServiceException("Failed to list the sick persons by lastname: " + lastNameFilter);
			}
		}
		return personList;
	}

	@Override
	public void remove(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		if (!sickPersonService.removeSickPerson(model.getSickPersonId()))
			throw new ServiceException("Failed to remove the sick person " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		if (!sickPersonService.updateSickPerson(model))
			throw new ServiceException("Failed to update the sick person " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<SickPerson> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
