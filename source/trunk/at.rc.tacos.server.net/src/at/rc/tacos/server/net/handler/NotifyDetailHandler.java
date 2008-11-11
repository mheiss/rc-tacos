package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CallerService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class NotifyDetailHandler implements INetHandler<CallerDetail> {

	@Service(clazz = CallerService.class)
	private CallerService callerService;

	@Override
	public CallerDetail add(CallerDetail model) throws ServiceException, SQLException {
		int id = callerService.addCaller(model);
		if (id == -1)
			throw new ServiceException("Failed to add the caller:" + model);
		return model;
	}

	@Override
	public List<CallerDetail> execute(String command, List<CallerDetail> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<CallerDetail> get(Map<String, String> params) throws ServiceException, SQLException {
		final List<CallerDetail> callerList = new ArrayList<CallerDetail>();
		// if there is no filter -> not supported
		if (params == null || params.isEmpty()) {
			throw new ServiceException("Listing of all callers is not supported");
		}
		else if (params.containsKey(IFilterTypes.ID_FILTER)) {
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			CallerDetail caller = callerService.getCallerByID(id);
			if (caller == null)
				throw new ServiceException("Failed to get the caller by id:" + id);
			callerList.add(caller);
		}
		return callerList;
	}

	@Override
	public CallerDetail remove(CallerDetail model) throws ServiceException, SQLException {
		throw new NoSuchCommandException("remove");
	}

	@Override
	public CallerDetail update(CallerDetail model) throws ServiceException, SQLException {
		if (!callerService.updateCaller(model))
			throw new ServiceException("Failed to update the caller:" + model);
		return model;
	}

}
