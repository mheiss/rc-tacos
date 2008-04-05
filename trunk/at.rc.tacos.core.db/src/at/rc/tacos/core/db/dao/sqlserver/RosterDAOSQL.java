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
	private final Queries queries = Queries.getInstance();
	//the dependent dao classes
	private final StaffMemberDAO staffDAO = DaoFactory.SQL.createStaffMemberDAO();
	private final LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();

	@Override
	public int addRosterEntry(RosterEntry entry) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.RosterEntry"));
			query.setInt(1, entry.getStation().getId());
			query.setInt(2, entry.getStaffMember().getStaffMemberId());
			query.setInt(3, entry.getServicetype().getId());
			query.setInt(4, entry.getJob().getId());
			//if the time is not set then write undefined into the database
			if(entry.getPlannedStartOfWork() == 0)
				query.setString(5, null);
			else
				query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.sqlDateTime));
			if(entry.getPlannedEndOfWork() == 0)
				query.setString(6, null);
			else
				query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.sqlDateTime));
			if(entry.getRealStartOfWork() == 0)
				query.setString(7, null);
			else
				query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.sqlDateTime));
			if(entry.getRealEndOfWork() == 0)
				query.setString(8, null);
			else
				query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.sqlDateTime));
			query.setString(9, entry.getRosterNotes());
			query.setBoolean(10, entry.getStandby());
			query.setString(11, entry.getCreatedByUsername());
			
			query.executeUpdate();
			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
		    if (rs.next()) 
		        return rs.getInt(1);
		    return -1;
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
				query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.sqlDateTime));
			if(entry.getPlannedEndOfWork() == 0)
				query.setString(6, null);
			else
				query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.sqlDateTime));
			if(entry.getRealStartOfWork() == 0)
				query.setString(7, null);
			else
				query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.sqlDateTime));
			if(entry.getRealEndOfWork() == 0)
				query.setString(8, null);
			else
				query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.sqlDateTime));
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
			if(rs.first())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("ro.roster_ID"));
				entry.setCreatedByUsername(rs.getString("ro.entry_createdBy"));
				if(rs.getString("ro.starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.starttime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.endtime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkIn"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkOut"), MyUtils.sqlDateTime));
				//Set the location
				int locationId = rs.getInt("ro.location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("ro.servicetype_ID"));
				service.setServiceName(rs.getString("st.servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("ro.job_ID"));
				job.setJobName(rs.getString("j.jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("ro.note") != null)
					entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));
				//get the staff member
				int staffId = rs.getInt("ro.staffmember_ID");
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
				entry.setRosterId(rs.getInt("ro.roster_ID"));
				entry.setCreatedByUsername(rs.getString("ro.entry_createdBy"));
				if(rs.getString("ro.starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.starttime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.endtime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkIn"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkOut"), MyUtils.sqlDateTime));
				//Set the location
				int locationId = rs.getInt("ro.location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("ro.servicetype_ID"));
				service.setServiceName(rs.getString("st.servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("ro.job_ID"));
				job.setJobName(rs.getString("j.jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("ro.note") != null)
					entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));
				//get the staff member
				int staffId = rs.getInt("ro.staffmember_ID");
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
			query.setString(1, MyUtils.timestampToString(startTime, MyUtils.sqlDateTime));
			query.setString(2, MyUtils.timestampToString(endTime, MyUtils.sqlDateTime));
			final ResultSet rs = query.executeQuery();
			//create the result list and loop over the result
			List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
			while(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("ro.roster_ID"));
				entry.setCreatedByUsername(rs.getString("ro.entry_createdBy"));
				if(rs.getString("ro.starttime") == null)
					entry.setPlannedStartOfWork(0);
				else
					entry.setPlannedStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.starttime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.endtime") == null)
					entry.setPlannedEndOfWork(0);
				else
					entry.setPlannedEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.endtime"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkIn") == null)
					entry.setRealStartOfWork(0);
				else
					entry.setRealStartOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkIn"), MyUtils.sqlDateTime));
				if(rs.getString("ro.checkOut") == null)
					entry.setRealEndOfWork(0);
				else
					entry.setRealEndOfWork(MyUtils.stringToTimestamp(rs.getString("ro.checkOut"), MyUtils.sqlDateTime));
				//Set the location
				int locationId = rs.getInt("ro.location_ID");
				entry.setStation(locationDAO.getLocation(locationId));
				//set the service type
				ServiceType service = new ServiceType();
				service.setId(rs.getInt("ro.servicetype_ID"));
				service.setServiceName(rs.getString("st.servicetype"));
				entry.setServicetype(service);
				//Set the job
				Job job = new Job();
				job.setId(rs.getInt("ro.job_ID"));
				job.setJobName(rs.getString("j.jobname"));
				entry.setJob(job);
				//set the notes
				if(rs.getString("ro.note") != null)
					entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));
				//get the staff member
				int staffId = rs.getInt("ro.staffmember_ID");
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