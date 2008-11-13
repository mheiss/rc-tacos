package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.RosterService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class RosterHandler implements Handler<RosterEntry> {

	@Service(clazz = RosterService.class)
	private RosterService rosterService;

	@Override
	public void add(ServerIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and add the roster entries
		for (RosterEntry entry : rosterList) {
			int id = rosterService.addRosterEntry(entry);
			if (id == -1)
				throw new ServiceException("Failed to add the roster entry " + entry);
			entry.setRosterId(id);
		}
		// brodcast the added roster entries
		session.writeBrodcast(message, rosterList);
	}

	@Override
	public void get(ServerIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		// get the params of the request
		Map<String, String> params = message.getParams();

		if (params.containsKey(IFilterTypes.DATE_FILTER) && params.containsKey(IFilterTypes.ROSTER_LOCATION_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			int filterLocationId = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_FILTER));
			List<RosterEntry> rosterList = rosterService.listRosterEntriesByDateAndLocation(dateStart, dateEnd, filterLocationId);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}
			// send the result back
			session.write(message, rosterList);
			return;
		}
		if (params.containsKey(IFilterTypes.ROSTER_MONTH_FILTER) && params.containsKey(IFilterTypes.ROSTER_YEAR_FILTER)) {
			int monthFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_MONTH_FILTER));
			int yearFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_YEAR_FILTER));
			int locationFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_LOCATION_FILTER)) {
				locationFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_FILTER));
			}
			int locationStaffMemberFilter = -1;
			String functionStaffMemberCompetenceFilter = null;
			if (params.containsKey(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER)) {
				functionStaffMemberCompetenceFilter = params.get(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER);
			}
			if (params.containsKey(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER)) {
				locationStaffMemberFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER));
			}
			int staffMemberFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER)) {
				staffMemberFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER));
			}
			String statisticFilter = null;
			if (params.containsKey(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER)) {
				statisticFilter = params.get(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER);
			}
			int serviceTypeFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_SERVICE_TYPE_FILTER)) {
				serviceTypeFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_SERVICE_TYPE_FILTER));
			}
			List<RosterEntry> rosterList = rosterService.listRosterEntriesForRosterMonth(locationFilter, monthFilter, yearFilter,
					locationStaffMemberFilter, functionStaffMemberCompetenceFilter, staffMemberFilter, statisticFilter, serviceTypeFilter);
			if (rosterList == null) {
				throw new ServiceException("Failed to list the roster entries by date from.");
			}
			// send the result back
			session.write(message, rosterList);
			return;
		}
		if (params.containsKey(IFilterTypes.DATE_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			List<RosterEntry> rosterList = rosterService.listRosterEntryByDate(dateStart, dateEnd);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}
			// send the result back
			session.write(message, rosterList);
			return;
		}
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			// get the query filter
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			RosterEntry entry = rosterService.getRosterEntryById(id);
			if (entry == null)
				throw new ServiceException("Failed to list the roster entry by id:" + id);
			// send the result back
			session.write(message, entry);
			return;
		}

		// no parameter matched
		throw new ServiceException("Listing of all roster entries is denied");
	}

	@Override
	public void remove(ServerIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and remove the roster entries
		for (RosterEntry entry : rosterList) {
			if (!rosterService.removeRosterEntry(entry.getRosterId()))
				throw new ServiceException("Failed to remove the roster entry:" + entry);
		}
		// brodcast the removed roster objects
		session.writeBrodcast(message, rosterList);
	}

	@Override
	public void update(ServerIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and update the roster entries
		for (RosterEntry entry : rosterList) {
			if (!rosterService.updateRosterEntry(entry))
				throw new ServiceException("Failed to update the roster entry:" + entry);
		}
		// brodcast the updated roster objects
		session.writeBrodcast(message, rosterList);
	}

	@Override
	public void execute(ServerIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
