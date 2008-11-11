package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DayInfoService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class DayInfoHandler implements INetHandler<DayInfoMessage> {

	// the day info service
	@Service(clazz = DayInfoService.class)
	private DayInfoService dayInfoService;

	@Override
	public DayInfoMessage add(DayInfoMessage model) throws ServiceException, SQLException {
		throw new NoSuchCommandException("add");
	}

	@Override
	public List<DayInfoMessage> execute(String command, List<DayInfoMessage> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException("command");
	}

	@Override
	public List<DayInfoMessage> get(Map<String, String> params) throws ServiceException, SQLException {
		if (params.containsKey(IFilterTypes.DATE_FILTER)) {
			List<DayInfoMessage> resultList = new ArrayList<DayInfoMessage>();
			// get the query filter
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			// get the timestamp
			long date = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			// the day info
			DayInfoMessage message = dayInfoService.getDayInfoByDate(date);
			// assert valid
			if (message == null) {
				message = new DayInfoMessage();
				message.setTimestamp(date);
				message.setMessage("");
				message.setDirty(false);
				message.setLastChangedBy("<keine Änderung>");
			}
			resultList.add(message);
			// return the requested message
			return resultList;
		}
		return null;
	}

	@Override
	public DayInfoMessage remove(DayInfoMessage model) throws ServiceException, SQLException {
		// update the message on the server
		if (!dayInfoService.updateDayInfoMessage(model))
			throw new ServiceException("Failed to update the day info message: " + model);
		// reset the dirty flag
		model.setDirty(false);
		return model;
	}

	@Override
	public DayInfoMessage update(DayInfoMessage model) throws ServiceException, SQLException {
		throw new NoSuchCommandException("remove");
	}

}
