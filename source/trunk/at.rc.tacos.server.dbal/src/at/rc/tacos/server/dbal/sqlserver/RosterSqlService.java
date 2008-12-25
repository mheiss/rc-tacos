package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.dbal.RosterService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.util.MyUtils;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for roster.
 * 
 * @author Michael
 */
public class RosterSqlService implements RosterService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	@Service(clazz = StaffMemberService.class)
	private StaffMemberService staffDAO;

	@Service(clazz = LocationService.class)
	private LocationService locationDAO;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addRosterEntry(RosterEntry entry) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextRosterID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.RosterEntry"));
		query.setInt(1, id);
		query.setInt(2, entry.getStation().getId());
		query.setInt(3, entry.getStaffMember().getStaffMemberId());
		query.setInt(4, entry.getServicetype().getId());
		query.setInt(5, entry.getJob().getId());
		// if the time is not set then write undefined into the database
		if (entry.getPlannedStartOfWork() == 0)
			query.setString(6, null);
		else
			query.setString(6, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getPlannedEndOfWork() == 0)
			query.setString(7, null);
		else
			query.setString(7, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getRealStartOfWork() == 0)
			query.setString(8, null);
		else
			query.setString(8, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getRealEndOfWork() == 0)
			query.setString(9, null);
		else
			query.setString(9, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.timeAndDateFormat));
		query.setString(10, entry.getRosterNotes());
		query.setBoolean(11, entry.getStandby());
		query.setString(12, entry.getCreatedByUsername());

		if (query.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public boolean updateRosterEntry(RosterEntry entry) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.RosterEntry"));
		query.setInt(1, entry.getStation().getId());
		query.setInt(2, entry.getStaffMember().getStaffMemberId());
		query.setInt(3, entry.getServicetype().getId());
		query.setInt(4, entry.getJob().getId());
		if (entry.getPlannedStartOfWork() == 0)
			query.setString(5, null);
		else
			query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getPlannedEndOfWork() == 0)
			query.setString(6, null);
		else
			query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getRealStartOfWork() == 0)
			query.setString(7, null);
		else
			query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.timeAndDateFormat));
		if (entry.getRealEndOfWork() == 0)
			query.setString(8, null);
		else
			query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.timeAndDateFormat));
		query.setString(9, entry.getRosterNotes());
		query.setBoolean(10, entry.getStandby());
		query.setString(11, entry.getCreatedByUsername());
		query.setInt(12, entry.getRosterId());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean removeRosterEntry(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.RosterEntry"));
		query.setInt(1, id);
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public RosterEntry getRosterEntryById(int rosterEntryId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.RosterByID"));
		query.setInt(1, rosterEntryId);
		final ResultSet rs = query.executeQuery();
		// loop over the result set
		if (rs.next()) {
			return setupRosterEntry(rs);
		}
		// nothin in the result set
		return null;
	}

	@Override
	public List<RosterEntry> listRosterEntryByStaffMember(int employeeID) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterBystaffmemberID"));
		query.setInt(1, employeeID);
		final ResultSet rs = query.executeQuery();
		return setupRosterList(rs);
	}

	@Override
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterByTime"));
		query.setString(1, MyUtils.timestampToString(startTime, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(endTime, MyUtils.timeAndDateFormat));
		final ResultSet rs = query.executeQuery();
		// create the result list and loop over the result
		return setupRosterList(rs);
	}

	@Override
	public List<RosterEntry> listRosterEntriesByDateAndLocation(long startTime, long endTime, int filterLocationId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterByTimeAndLocation"));
		query.setString(1, MyUtils.timestampToString(startTime, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(endTime, MyUtils.timeAndDateFormat));
		query.setInt(3, filterLocationId);
		final ResultSet rs = query.executeQuery();
		return setupRosterList(rs);
	}

	@Override
	public List<RosterEntry> listRosterEntriesByDateAndStaff(long startTime, long endTime, int staffId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterByTimeAndStaff"));
		query.setString(1, MyUtils.timestampToString(startTime, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(endTime, MyUtils.timeAndDateFormat));
		query.setInt(3, staffId);
		final ResultSet rs = query.executeQuery();
		return setupRosterList(rs);
	}

	@Override
	public List<RosterEntry> listRosterEntriesForRosterMonth(int locationFilter, int monthFilter, int yearFilter, int locationStaffMemberFilter, String functionStaffMemberCompetenceFilter, int staffMemberFilter, String statisticFilter, int serviceTypeFilter) throws SQLException {
		String queryString = queries.getStatment("list.RosterForRosterMonth");

		if (locationFilter != -1) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.locationCondition");
		}

		if (locationStaffMemberFilter != -1) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.primaryLocationCondition");
		}

		if (functionStaffMemberCompetenceFilter != null) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.defaultFunctionCondition");
		}

		if (staffMemberFilter != -1) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.staffMemberCondition");
		}

		if (statisticFilter != null) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.statisticCondition");
		}

		if (serviceTypeFilter != -1) {
			queryString = queryString + " " + queries.getStatment("list.RosterForRosterMonth.serviceTypeCondition");
		}

		final PreparedStatement query = connection.prepareStatement(queryString);

		int i = 1;
		query.setInt(i, monthFilter);

		i++;
		query.setInt(i, yearFilter);

		if (locationFilter != -1) {
			i++;
			query.setInt(i, locationFilter);
		}

		if (locationStaffMemberFilter != -1) {
			i++;
			query.setInt(i, locationStaffMemberFilter);
		}

		if (functionStaffMemberCompetenceFilter != null) {
			i++;
			query.setString(i, functionStaffMemberCompetenceFilter);
		}

		if (staffMemberFilter != -1) {
			i++;
			query.setInt(i, staffMemberFilter);
		}

		if (serviceTypeFilter != -1) {
			i++;
			query.setInt(i, serviceTypeFilter);
		}

		final ResultSet rs = query.executeQuery();
		// create the result list and loop over the result
		return setupRosterList(rs);
	}

	/**
	 * Helper method to setup a list of roster entries from an result set
	 * 
	 * @param rs
	 *            the unmodified result set
	 * @return the list of roster entries
	 */
	private List<RosterEntry> setupRosterList(ResultSet rs) throws SQLException {
		List<RosterEntry> rosterList = new ArrayList<RosterEntry>();
		while (rs.next()) {
			RosterEntry entry = setupRosterEntry(rs);
			rosterList.add(entry);
		}
		return rosterList;
	}

	/**
	 * Helper method to setup a single roster entry from an result set
	 * 
	 * @param rs
	 *            the result set to get the values
	 * @return the new roster entry instance
	 */
	private RosterEntry setupRosterEntry(ResultSet rs) throws SQLException {
		RosterEntry entry = new RosterEntry();
		entry.setRosterId(rs.getInt("roster_ID"));
		entry.setCreatedByUsername(rs.getString("entry_createdBy"));
		if (rs.getString("starttime") == null)
			entry.setPlannedStartOfWork(0);
		else
			entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("starttime"), MyUtils.sqlServerDateTime));
		if (rs.getString("endtime") == null)
			entry.setPlannedEndOfWork(0);
		else
			entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("endtime"), MyUtils.sqlServerDateTime));
		if (rs.getString("checkIn") == null)
			entry.setRealStartOfWork(0);
		else
			entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("checkIn"), MyUtils.sqlServerDateTime));
		if (rs.getString("checkOut") == null)
			entry.setRealEndOfWork(0);
		else
			entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("checkOut"), MyUtils.sqlServerDateTime));
		// Set the location
		int locationId = rs.getInt("location_ID");
		entry.setStation(locationDAO.getLocation(locationId));
		// set the service type
		ServiceType service = new ServiceType();
		service.setId(rs.getInt("servicetype_ID"));
		service.setServiceName(rs.getString("servicetype"));
		entry.setServicetype(service);
		// Set the job
		Job job = new Job();
		job.setId(rs.getInt("job_ID"));
		job.setJobName(rs.getString("jobname"));
		entry.setJob(job);
		// set the notes
		if (rs.getString("note") != null)
			entry.setRosterNotes(rs.getString("note"));
		entry.setStandby(rs.getBoolean("standby"));
		// get the staff member
		int staffId = rs.getInt("staffmember_ID");
		entry.setStaffMember(staffDAO.getStaffMemberByID(staffId));
		return entry;
	}
}
