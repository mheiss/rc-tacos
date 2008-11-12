package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CallerService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class NotifyDetailHandler implements Handler<CallerDetail> {

	@Service(clazz = CallerService.class)
	private CallerService callerService;

	@Override
	public void add(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		int id = callerService.addCaller(model);
		if (id == -1)
			throw new ServiceException("Failed to add the caller:" + model);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
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
	public void remove(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		throw new NoSuchCommandException("remove");
	}

	@Override
	public void update(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		if (!callerService.updateCaller(model))
			throw new ServiceException("Failed to update the caller:" + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
