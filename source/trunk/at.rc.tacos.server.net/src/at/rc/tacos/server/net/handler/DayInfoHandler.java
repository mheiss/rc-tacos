package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DayInfoService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class DayInfoHandler implements Handler<DayInfoMessage> {

	@Service(clazz = DayInfoService.class)
	private DayInfoService dayInfoService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'add' command is not implemented
		String command = MessageType.ADD.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void get(MessageIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
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

			// check for locks of this object
			if (lockableService.containsLock(dayInfoMessage)) {
				Lockable lock = lockableService.getLock(dayInfoMessage);
				dayInfoMessage.setLocked(lock.isLocked());
				dayInfoMessage.setLockedBy(lock.getLockedBy());
			}

			// return the requested message
			session.write(message, dayInfoMessage);
			return;
		}
		throw new ServiceException("Failed to get the day info message, no request parameter provided");
	}

	@Override
	public void remove(MessageIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'remove' command is not implemented
		String command = MessageType.REMOVE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void update(MessageIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		List<DayInfoMessage> dayInfoList = message.getObjects();
		// loop and try to remove each day info message object
		for (DayInfoMessage dayInfoMessage : dayInfoList) {
			// update the message on the server
			if (!dayInfoService.updateDayInfoMessage(dayInfoMessage))
				throw new ServiceException("Failed to update the day info message: " + dayInfoMessage);
			// reset the dirty flag
			dayInfoMessage.setDirty(false);

			// update the locks
			lockableService.updateLock(dayInfoMessage);
		}
		session.writeBrodcast(message, dayInfoList);
	}

	@Override
	public void execute(MessageIoSession session, Message<DayInfoMessage> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

}
