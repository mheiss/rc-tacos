package at.rc.tacos.core.db.dao.sqlserver;

import at.rc.tacos.core.db.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.util.MyUtils;

public class RosterDAOSQL implements RosterDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();
	//the dependent dao classes
	private final StaffMemberDAO staffDAO = DaoFactory.SQL.createStaffMemberDAO();
	private final LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();

	@Override
	public int addRosterEntry(RosterEntry entry) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextRosterID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);
			
			
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.RosterEntry"));
			query.setInt(1, id);
			query.setInt(2, entry.getStation().getId());
			query.setInt(3, entry.getStaffMember().getStaffMemberId());
			query.setInt(4, entry.getServicetype().getId());
			query.setInt(5, entry.getJob().getId());
			//if the time is not set then write undefined into the database
			if(entry.getPlannedStartOfWork() == 0)
				query.setString(6, null);
			else
				query.setString(6, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getPlannedEndOfWork() == 0)
				query.setString(7, null);
			else
				query.setString(7, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getRealStartOfWork() == 0)
				query.setString(8, null);
			else
				query.setString(8, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getRealEndOfWork() == 0)
				query.setString(9, null);
			else
				query.setString(9, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.timeAndDateFormat));
			query.setString(10, entry.getRosterNotes());
			query.setBoolean(11, entry.getStandby());
			query.setString(12, entry.getCreatedByUsername());
			
			if(query.executeUpdate() == 0)
				return -1;
			
			return id;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateRosterEntry(RosterEntry entry) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.RosterEntry"));
			query.setInt(1, entry.getStation().getId());
			query.setInt(2, entry.getStaffMember().getStaffMemberId());
			query.setInt(3, entry.getServicetype().getId());
			query.setInt(4, entry.getJob().getId());
			if(entry.getPlannedStartOfWork() == 0)
				query.setString(5, null);
			else
				query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getPlannedEndOfWork() == 0)
				query.setString(6, null);
			else
				query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getRealStartOfWork() == 0)
				query.setString(7, null);
			else
				query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.timeAndDateFormat));
			if(entry.getRealEndOfWork() == 0)
				query.setString(8, null);
			else
				query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.timeAndDateFormat));
			query.setString(9, entry.getRosterNotes());
			query.setBoolean(10, entry.getStandby());
			query.setString(11, entry.getCreatedByUsername());
			query.setInt(12, entry.getRosterId());
			//assert the update was successfully
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeRosterEntry(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.RosterEntry"));
			query.setInt(1, id);
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public RosterEntry getRosterEntryById(int rosterEntryId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.RosterByID"));
			query.setInt(1, rosterEntryId);
			final ResultSet rs = query.executeQuery();
			//loop over the result set
			if(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("roster_ID"));
				entry.setCreatedByUsername(rs.getString("entry_createdBy"));
				if(rs.getString("starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("starttime"), MyUtils.sqlServerDateTime));
				if(rs.getString("endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("endtime"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("checkIn"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("checkOut"), MyUtils.sqlServerDateTime));
				//Set the location
				int locationId = rs.getInt("location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("servicetype_ID"));
				service.setServiceName(rs.getString("servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("job_ID"));
				job.setJobName(rs.getString("jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("note") != null)
					entry.setRosterNotes(rs.getString("note"));
				entry.setStandby(rs.getBoolean("standby"));
				//get the staff member
				int staffId = rs.getInt("staffmember_ID");
				entry.setStaffMember(staffDAO.getStaffMemberByID(staffId));
				return entry;
			}
			//nothin in the result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<RosterEntry> listRosterEntryByStaffMember(int employeeID) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
			//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterBystaffmemberID"));
			query.setInt(1, employeeID);
			final ResultSet rs = query.executeQuery();
			List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
			while(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("roster_ID"));
				entry.setCreatedByUsername(rs.getString("entry_createdBy"));
				if(rs.getString("starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("starttime"), MyUtils.sqlServerDateTime));
				if(rs.getString("endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("endtime"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("checkIn"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("checkOut"), MyUtils.sqlServerDateTime));
				//Set the location
				int locationId = rs.getInt("location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("servicetype_ID"));
				service.setServiceName(rs.getString("servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("job_ID"));
				job.setJobName(rs.getString("jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("note") != null)
					entry.setRosterNotes(rs.getString("note"));
				entry.setStandby(rs.getBoolean("standby"));
				//get the staff member
				int staffId = rs.getInt("staffmember_ID");
				entry.setStaffMember(staffDAO.getStaffMemberByID(staffId));
				entrylist.add(entry);
			}
			return entrylist;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
			//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.RosterByTime"));
			query.setString(1, MyUtils.timestampToString(startTime, MyUtils.timeAndDateFormat));
			query.setString(2, MyUtils.timestampToString(endTime, MyUtils.timeAndDateFormat));
			final ResultSet rs = query.executeQuery();
			//create the result list and loop over the result
			List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
			while(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("roster_ID"));
				entry.setCreatedByUsername(rs.getString("entry_createdBy"));
				if(rs.getString("starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("starttime"), MyUtils.sqlServerDateTime));
				if(rs.getString("endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("endtime"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("checkIn"), MyUtils.sqlServerDateTime));
				if(rs.getString("checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("checkOut"), MyUtils.sqlServerDateTime));
				//Set the location
				int locationId = rs.getInt("location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("servicetype_ID"));
				service.setServiceName(rs.getString("servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("job_ID"));
				job.setJobName(rs.getString("jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("note") != null)
					entry.setRosterNotes(rs.getString("note"));
				entry.setStandby(rs.getBoolean("standby"));
				//get the staff member
				int staffId = rs.getInt("staffmember_ID");
				entry.setStaffMember(staffDAO.getStaffMemberByID(staffId));
				entrylist.add(entry);
			}
			return entrylist;
		}
		finally
		{
			connection.close();
		}
	}
}