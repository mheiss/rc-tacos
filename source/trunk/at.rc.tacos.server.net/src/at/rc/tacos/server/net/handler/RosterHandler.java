package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.RosterService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class RosterHandler implements INetHandler<RosterEntry> {

	@Service(clazz = RosterService.class)
	private RosterService rosterService;

	@Override
	public RosterEntry add(RosterEntry model) throws ServiceException, SQLException {
		int id = rosterService.addRosterEntry(model);
		if (id == -1)
			throw new ServiceException("Failed to add the roster entry " + model);
		model.setRosterId(id);
		return model;
	}

	@Override
	public List<RosterEntry> execute(String command, List<RosterEntry> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<RosterEntry> get(Map<String, String> params) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = new ArrayList<RosterEntry>();
		// if there is no filter -> request all
		if (params == null || params.isEmpty()) {
			throw new ServiceException("Listing of all roster entries is denied");
		}
		if (params.containsKey(IFilterTypes.DATE_FILTER) && params.containsKey(IFilterTypes.ROSTER_LOCATION_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			int filterLocationId = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_FILTER));
			rosterList = rosterService.listRosterEntriesByDateAndLocation(dateStart, dateEnd, filterLocationId);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}
			return rosterList;
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
			rosterList = rosterService.listRosterEntriesForRosterMonth(locationFilter, monthFilter, yearFilter, locationStaffMemberFilter,
					functionStaffMemberCompetenceFilter, staffMemberFilter, statisticFilter, serviceTypeFilter);
			if (rosterList == null) {
				throw new ServiceException("Failed to list the roster entries by date from.");
			}
			return rosterList;
		}
		if (params.containsKey(IFilterTypes.DATE_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			rosterList = rosterService.listRosterEntryByDate(dateStart, dateEnd);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}
			return rosterList;
		}
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			// get the query filter
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			RosterEntry entry = rosterService.getRosterEntryById(id);
			if (entry == null)
				throw new ServiceException("Failed to list the roster entry by id:" + id);
			rosterList.add(entry);
			return rosterList;
		}
		return rosterList;
	}

	@Override
	public RosterEntry remove(RosterEntry model) throws ServiceException, SQLException {
		if (!rosterService.removeRosterEntry(model.getRosterId()))
			throw new ServiceException("Failed to remove the roster entry:" + model);
		return model;
	}

	@Override
	public RosterEntry update(RosterEntry model) throws ServiceException, SQLException {
		if (!rosterService.updateRosterEntry(model))
			throw new ServiceException("Failed to update the roster entry:" + model);
		return model;
	}

}
