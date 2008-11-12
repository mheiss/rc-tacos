package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DayInfoService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class DayInfoHandler implements Handler<DayInfoMessage> {

	// the day info service
	@Service(clazz = DayInfoService.class)
	private DayInfoService dayInfoService;

	@Override
	public void add(ServerIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'add' command is not implemented
		String command = MessageType.ADD.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void get(ServerIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// get the params from the message
		Map<String, String> params = message.getParams();

		if (params.containsKey(IFilterTypes.DATE_FILTER)) {
			// get the query filter
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long date = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			// request the message
			DayInfoMessage dayInfoMessage = dayInfoService.getDayInfoByDate(date);
			// assert valid
			if (dayInfoMessage == null) {
				dayInfoMessage = new DayInfoMessage();
				dayInfoMessage.setTimestamp(date);
				dayInfoMessage.setMessage("");
				dayInfoMessage.setDirty(false);
				dayInfoMessage.setLastChangedBy("<keine Änderung>");
			}
			// return the requested message
			session.write(message, dayInfoMessage);
			return;
		}
		throw new ServiceException("Failed to get the day info message, no request parameter provided");
	}

	@Override
	public void remove(ServerIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		List<DayInfoMessage> dayInfoList = message.getObjects();
		// loop and try to remove each day info message object
		for (DayInfoMessage dayInfoMessage : dayInfoList) {
			// update the message on the server
			if (!dayInfoService.updateDayInfoMessage(dayInfoMessage))
				throw new ServiceException("Failed to update the day info message: " + dayInfoMessage);
			// reset the dirty flag
			dayInfoMessage.setDirty(false);
		}
		session.writeBrodcast(message, dayInfoList);
	}

	@Override
	public void update(ServerIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'update' command is not implemented
		String command = MessageType.UPDATE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void execute(ServerIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

}
