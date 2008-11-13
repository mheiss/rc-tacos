package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
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
		List<CallerDetail> callerList = message.getObjects();
		// loop and add the callers
		for (CallerDetail detail : callerList) {
			int id = callerService.addCaller(detail);
			if (id == -1)
				throw new ServiceException("Failed to add the caller:" + detail);
		}
		// brodcast the added callers
		session.writeBrodcast(message, callerList);
	}

	@Override
	public void get(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		// the params
		Map<String, String> params = message.getParams();

		// query a single caller by the id
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			CallerDetail caller = callerService.getCallerByID(id);
			if (caller == null)
				throw new ServiceException("Failed to get the caller by id:" + id);
			// send the result back
			session.write(message, caller);
		}

		// listing of all callers is not allowed
		throw new ServiceException("Listing of all callers is not supported");
	}

	@Override
	public void remove(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = MessageType.REMOVE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void update(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		List<CallerDetail> callerList = message.getObjects();
		// loop and update the callers
		for (CallerDetail detail : callerList) {
			if (!callerService.updateCaller(detail))
				throw new ServiceException("Failed to update the caller:" + detail);
		}
		// brodcast the updated callers
		session.writeBrodcast(message, callerList);
	}

	@Override
	public void execute(ServerIoSession session, Message<CallerDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
