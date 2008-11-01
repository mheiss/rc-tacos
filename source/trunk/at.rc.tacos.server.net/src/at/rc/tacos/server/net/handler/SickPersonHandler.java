package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.SickPersonService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class SickPersonHandler implements INetHandler<SickPerson> {

	@Service(clazz = SickPersonService.class)
	private SickPersonService sickPersonService;

	@Override
	public SickPerson add(SickPerson model) throws ServiceException, SQLException {
		// add the location
		int id = sickPersonService.addSickPerson(model);
		if (id == -1)
			throw new ServiceException("Failed to add the sick person: " + model);
		// set the id
		model.setSickPersonId(id);
		return model;
	}

	@Override
	public List<SickPerson> execute(String command, List<SickPerson> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<SickPerson> get(Map<String, String> params) throws ServiceException, SQLException {
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
	public SickPerson remove(SickPerson model) throws ServiceException, SQLException {
		if (!sickPersonService.removeSickPerson(model.getSickPersonId()))
			throw new ServiceException("Failed to remove the sick person " + model);
		return model;
	}

	@Override
	public SickPerson update(SickPerson model) throws ServiceException, SQLException {
		if (!sickPersonService.updateSickPerson(model))
			throw new ServiceException("Failed to update the sick person " + model);
		return model;
	}

}
