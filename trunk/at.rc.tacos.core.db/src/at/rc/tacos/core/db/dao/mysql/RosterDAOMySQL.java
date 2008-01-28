package at.rc.tacos.core.db.dao.mysql;

import at.rc.tacos.core.db.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class RosterDAOMySQL implements RosterDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addRosterEntry(RosterEntry entry)
	{
		Integer rosterId = null;
		try
		{	
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.RosterEntry"));
			query.setInt(1, entry.getStation().getId());
			query.setInt(2, entry.getStaffMember().getStaffMemberId());
			query.setInt(3, entry.getServicetype().getId());
			query.setInt(4, entry.getJob().getId());
			query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.sqlDateTime));
			query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.sqlDateTime));
			query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.sqlDateTime));
			query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.sqlDateTime));
			query.setString(9, entry.getRosterNotes());
			query.setBoolean(10, entry.getStandby());
			query.setString(11, entry.getCreatedByUsername());

			query.executeUpdate();

			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.RosterEntryId"));
			query1.setInt(1, entry.getStaffMember().getStaffMemberId());
			query1.setString(2, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.sqlDateTime));
			final ResultSet rsRosterId = query1.executeQuery();

			if(rsRosterId.next())
				rosterId = rsRosterId.getInt("roster_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return rosterId;
	}

	@Override
	public boolean updateRosterEntry(RosterEntry entry)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.RosterEntry"));
			query.setInt(1, entry.getStation().getId());
			query.setInt(2, entry.getStaffMember().getStaffMemberId());
			query.setInt(3, entry.getServicetype().getId());
			query.setInt(4, entry.getJob().getId());
			query.setString(5, MyUtils.timestampToString(entry.getPlannedStartOfWork(), MyUtils.sqlDateTime));
			query.setString(6, MyUtils.timestampToString(entry.getPlannedEndOfWork(), MyUtils.sqlDateTime));
			query.setString(7, MyUtils.timestampToString(entry.getRealStartOfWork(), MyUtils.sqlDateTime));
			query.setString(8, MyUtils.timestampToString(entry.getRealEndOfWork(), MyUtils.sqlDateTime));
			query.setString(9, entry.getRosterNotes());
			query.setBoolean(10, entry.getStandby());
			query.setString(11, entry.getCreatedByUsername());
			query.setInt(12, entry.getRosterId());

			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeRosterEntry(int id)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.RosterEntry"));
			query.setInt(1, id);

			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public RosterEntry getRosterEntryById(int rosterEntryId)
	{
		RosterEntry entry = new RosterEntry();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
			//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.RosterByID"));
			query.setInt(1, rosterEntryId);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				
				entry.setRosterId(rs.getInt("ro.roster_ID"));

				//Set the location
				Location station = new Location();
				station.setId(rs.getInt("ro.location_ID"));
				station.setLocationName(rs.getString("lo.locationname"));
				entry.setStation(station);

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

				entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));

				final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
				query2.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs2 = query2.executeQuery();

				station = null;
				if(!rs2.first())
					return null;

				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs2.getInt("e.staffmember_ID"));

				station.setId(rs2.getInt("e.primaryLocation"));
				station.setLocationName(rs2.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs2.getString("e.lastname"));
				staff.setFirstName(rs2.getString("e.firstname"));
				staff.setStreetname(rs2.getString("e.street"));
				staff.setCityname(rs2.getString("e.city"));
				staff.setMale(rs2.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs2.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs2.getString("e.email"));
				staff.setUserName(rs2.getString("e.username"));

				{
					Competence competence = new Competence();
					competence.setId(rs2.getInt("c.competence_ID"));
					competence.setCompetenceName(rs2.getString("c.competence"));
					competences.add(competence);
					staff.addCompetence(competence);
				}while(rs2.next());
				staff.setCompetenceList(competences);

				//ph.phonenumber, ph.phonenumber_ID
				final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
				query3.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs3 = query3.executeQuery();

				while(rs3.next())
				{
					MobilePhoneDetail phone = new MobilePhoneDetail();
					phone.setId(rs3.getInt("ph.phonenumber_ID"));
					phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
					phone.setMobilePhoneName(rs3.getString("ph.phonename"));
					staff.addMobilePhone(phone);
				}

				entry.setStaffMember(staff);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return entry;
	}

	@Override
	public List<RosterEntry> listRosterEntryByStaffMember(int employeeID)
	{
		List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
			//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.RosterBystaffmemberID"));
			query.setInt(1, employeeID);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("ro.roster_ID"));

				//Set the location
				Location station = new Location();
				station.setId(rs.getInt("ro.location_ID"));
				station.setLocationName(rs.getString("lo.locationname"));
				entry.setStation(station);

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

				entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));

				final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
				query2.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs2 = query2.executeQuery();

				station = null;
				if(!rs2.first())
					return null;

				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs2.getInt("e.staffmember_ID"));

				station.setId(rs2.getInt("e.primaryLocation"));
				station.setLocationName(rs2.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs2.getString("e.lastname"));
				staff.setFirstName(rs2.getString("e.firstname"));
				staff.setStreetname(rs2.getString("e.street"));
				staff.setCityname(rs2.getString("e.city"));
				staff.setMale(rs2.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs2.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs2.getString("e.email"));
				staff.setUserName(rs2.getString("e.username"));

				{
					Competence competence = new Competence();
					competence.setId(rs2.getInt("c.competence_ID"));
					competence.setCompetenceName(rs2.getString("c.competence"));
					competences.add(competence);
					staff.addCompetence(competence);
				}while(rs2.next());
				staff.setCompetenceList(competences);

				//ph.phonenumber, ph.phonenumber_ID
				final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
				query3.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs3 = query3.executeQuery();

				while(rs3.next())
				{
					MobilePhoneDetail phone = new MobilePhoneDetail();
					phone.setId(rs3.getInt("ph.phonenumber_ID"));
					phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
					phone.setMobilePhoneName(rs3.getString("ph.phonename"));
					staff.addMobilePhone(phone);
				}

				entry.setStaffMember(staff);
				entrylist.add(entry);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return entrylist;
	}

	@Override
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime)
	{
		List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
			//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.RosterByTime"));
			query.setString(1, MyUtils.timestampToString(startTime, MyUtils.sqlDateTime));
			query.setString(2, MyUtils.timestampToString(endTime, MyUtils.sqlDateTime));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				RosterEntry entry = new RosterEntry();
				entry.setRosterId(rs.getInt("ro.roster_ID"));

				//Set the location
				Location station = new Location();
				station.setId(rs.getInt("ro.location_ID"));
				station.setLocationName(rs.getString("lo.locationname"));
				entry.setStation(station);

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

				entry.setRosterNotes(rs.getString("ro.note"));
				entry.setStandby(rs.getBoolean("ro.standby"));

				final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
				query2.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs2 = query2.executeQuery();

				station = null;
				if(!rs2.first())
					return null;

				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs2.getInt("e.staffmember_ID"));

				station.setId(rs2.getInt("e.primaryLocation"));
				station.setLocationName(rs2.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs2.getString("e.lastname"));
				staff.setFirstName(rs2.getString("e.firstname"));
				staff.setStreetname(rs2.getString("e.street"));
				staff.setCityname(rs2.getString("e.city"));
				staff.setMale(rs2.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs2.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs2.getString("e.email"));
				staff.setUserName(rs2.getString("e.username"));

				{
					Competence competence = new Competence();
					competence.setId(rs2.getInt("c.competence_ID"));
					competence.setCompetenceName(rs2.getString("c.competence"));
					competences.add(competence);
					staff.addCompetence(competence);
				}while(rs2.next());
				staff.setCompetenceList(competences);

				//ph.phonenumber, ph.phonenumber_ID
				final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
				query3.setInt(1, rs.getInt("ro.staffmember_ID"));
				final ResultSet rs3 = query3.executeQuery();

				while(rs3.next())
				{
					MobilePhoneDetail phone = new MobilePhoneDetail();
					phone.setId(rs3.getInt("ph.phonenumber_ID"));
					phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
					phone.setMobilePhoneName(rs3.getString("ph.phonename"));
					staff.addMobilePhone(phone);
				}

				entry.setStaffMember(staff);
				entrylist.add(entry);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return entrylist;
	}
}
