package at.rc.tacos.core.db.dao.mysql;

import at.rc.tacos.core.db.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.*;

public class RosterDAOMySQL implements RosterDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	private String convertDate (long date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String stringdate = sdf.format(cal.getTime());
		
		return stringdate;
	}
	
	private long convertDateIntoLong (String stringdate)
	{
		long date=0;
        try
        {
            DateFormat dateFormat =  new SimpleDateFormat("yyyyMMddhhmmss"); 
            date = dateFormat.parse(stringdate).getTime();
        }
        catch(ParseException pe)
        {
            System.out.println("Failed to parse the given date");
            System.out.println(pe.getMessage());
        }
        return date;
	}
	
    @Override
    public Integer addRosterEntry(RosterEntry entry)
    {
    	Integer rosterId = null;
		try
		{	
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.RosterEntry"));
			query.setInt(1, entry.getStationId());
			query.setInt(2, entry.getStaffmemberId());
			query.setInt(3, entry.getServicetypeId());
			query.setInt(4, entry.getJobId());
			query.setString(5, convertDate(entry.getPlannedStartOfWork()));
			query.setString(6, convertDate(entry.getPlannedEndOfWork()));
			query.setString(7, convertDate(entry.getRealStartOfWork()));
			query.setString(8, convertDate(entry.getRealEndOfWork()));
			query.setString(9, entry.getRosterNotes());
			query.setBoolean(10, entry.getStandby());
			
			query.executeUpdate();
			
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.RosterEntryId"));
			query1.setInt(1, entry.getStaffmemberId());
			query1.setString(2, convertDate(entry.getPlannedStartOfWork()));
			final ResultSet rsRosterId = query1.executeQuery();
			
			if(rsRosterId.next())
				rosterId = rsRosterId.getInt("roster_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return rosterId;
    }
    
    @Override
    public boolean updateRosterEntry(RosterEntry entry)
    {
    	try
		{
    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.RosterEntry"));
		query.setInt(1, entry.getStationId());
		query.setInt(2, entry.getStaffmemberId());
		query.setInt(3, entry.getServicetypeId());
		query.setInt(4, entry.getJobId());
		query.setString(5, convertDate(entry.getPlannedStartOfWork()));
		query.setString(6, convertDate(entry.getPlannedEndOfWork()));
		query.setString(7, convertDate(entry.getRealStartOfWork()));
		query.setString(8, convertDate(entry.getRealEndOfWork()));
		query.setString(9, entry.getRosterNotes());
		query.setBoolean(10, entry.getStandby());
		query.setInt(11, entry.getRosterId());
		
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
    public void removeRosterEntry(RosterEntry rosterEntry)
    {
    	try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.RosterEntry"));
    		query.setInt(1, rosterEntry.getRosterId());

    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    	}

    }

    @Override
    public RosterEntry getRosterEntryById(int rosterEntryId)
    {
    	RosterEntry entry = new RosterEntry();
    	try
    	{
    		//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
    		//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.RosterByID"));
    		query.setInt(1, rosterEntryId);
    		final ResultSet rs = query.executeQuery();

    		rs.first();
    		entry.setRosterId(rs.getInt("ro.roster_ID"));
    		entry.setStationId(rs.getInt("ro.location_ID"));
			entry.setStation(rs.getString("lo.locationname"));
			entry.setStaffmemberId(rs.getInt("ro.staffmember_ID"));
			entry.setUsername(rs.getString("e.username"));
			entry.setCreatedByUser(rs.getString("ro.entry_createdBy"));
			entry.setPlannedStartOfWork(convertDateIntoLong(rs.getString("ro.starttime")));
			entry.setPlannedEndOfWork(convertDateIntoLong(rs.getString("ro.endtime")));
			entry.setRealStartOfWork(convertDateIntoLong(rs.getString("ro.checkIn")));
			entry.setRealEndOfWork(convertDateIntoLong(rs.getString("ro.checkOut")));
			entry.setServicetypeId(rs.getInt("ro.servicetype_ID"));
			entry.setServicetype(rs.getString("st.servicetype"));
			entry.setJobId(rs.getInt("ro.job_ID"));
			entry.setJob(rs.getString("j.jobname"));
			entry.setRosterNotes(rs.getString("ro.note"));
			entry.setStandby(rs.getBoolean("ro.standby"));

    		final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
    		query2.setInt(1, rs.getInt("ro.staffmember_ID"));
    		final ResultSet rs2 = query2.executeQuery();

    		StaffMember staff = new StaffMember();
    		rs2.first();    		
    		staff.setPersonId(rs2.getInt("e.staffmember_ID"));
    		staff.setPrimaryLocation(rs2.getInt("e.primaryLocation"));
    		staff.setPrimaryLocationName(rs2.getString("lo.locationname"));
    		staff.setLastName(rs2.getString("e.lastname"));
    		staff.setFirstName(rs2.getString("e.firstname"));
    		staff.setStreetname(rs2.getString("e.street"));
    		staff.setCityname(rs2.getString("e.city"));
    		staff.setSex(rs2.getBoolean("e.sex"));
    		staff.setBirthday(convertDateIntoLong(rs2.getString("e.birthday")));
    		staff.setEMail(rs2.getString("e.email"));
    		staff.setUserName(rs2.getString("e.username"));

    		//ph.phonenumber, ph.phonenumber_ID
    		final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
    		query3.setInt(1, rs.getInt("ro.staffmember_ID"));
    		final ResultSet rs3 = query3.executeQuery();
    		
    		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
    		while(rs3.next())
    		{
    			MobilePhoneDetail phone = new MobilePhoneDetail();
    			
    			phone.setMobilePhoneId(rs3.getInt("ph.phonenumber_ID"));
    			phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
    			phoneList.add(phone);
    		}
    		staff.setPhonelist(phoneList);
    		
    		entry.setStaffMember(staff);
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	return entry;
    }

    @Override
    public List<RosterEntry> listRosterEntryByEmployee(int employeeID)
    {
    	List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
    	RosterEntry entry = new RosterEntry();
    	try
    	{
    		//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
    		//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.RosterBystaffmemberID"));
    		query.setInt(1, employeeID);
    		final ResultSet rs = query.executeQuery();

    		while(rs.next())
    		{
    			entry.setRosterId(rs.getInt("ro.roster_ID"));
    			entry.setStationId(rs.getInt("ro.location_ID"));
    			entry.setStation(rs.getString("lo.locationname"));
    			entry.setStaffmemberId(rs.getInt("ro.staffmember_ID"));
    			entry.setUsername(rs.getString("e.username"));
    			entry.setCreatedByUser(rs.getString("ro.entry_createdBy"));
    			entry.setPlannedStartOfWork(convertDateIntoLong(rs.getString("ro.starttime")));
    			entry.setPlannedEndOfWork(convertDateIntoLong(rs.getString("ro.endtime")));
    			entry.setRealStartOfWork(convertDateIntoLong(rs.getString("ro.checkIn")));
    			entry.setRealEndOfWork(convertDateIntoLong(rs.getString("ro.checkOut")));
    			entry.setServicetypeId(rs.getInt("ro.servicetype_ID"));
    			entry.setServicetype(rs.getString("st.servicetype"));
    			entry.setJobId(rs.getInt("ro.job_ID"));
    			entry.setJob(rs.getString("j.jobname"));
    			entry.setRosterNotes(rs.getString("ro.note"));
    			entry.setStandby(rs.getBoolean("ro.standby"));

    			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
    			query2.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs2 = query2.executeQuery();

    			StaffMember staff = new StaffMember();
    			rs2.first();    		
    			staff.setPersonId(rs2.getInt("e.staffmember_ID"));
    			staff.setPrimaryLocation(rs2.getInt("e.primaryLocation"));
    			staff.setPrimaryLocationName(rs2.getString("lo.locationname"));
    			staff.setLastName(rs2.getString("e.lastname"));
    			staff.setFirstName(rs2.getString("e.firstname"));
    			staff.setStreetname(rs2.getString("e.street"));
    			staff.setCityname(rs2.getString("e.city"));
    			staff.setSex(rs2.getBoolean("e.sex"));
    			staff.setBirthday(convertDateIntoLong(rs2.getString("e.birthday")));
    			staff.setEMail(rs2.getString("e.email"));
    			staff.setUserName(rs2.getString("e.username"));

    			//ph.phonenumber, ph.phonenumber_ID
    			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
    			query3.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs3 = query3.executeQuery();

    			List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
    			while(rs3.next())
    			{
    				MobilePhoneDetail phone = new MobilePhoneDetail();

    				phone.setMobilePhoneId(rs3.getInt("ph.phonenumber_ID"));
    				phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
    				phoneList.add(phone);
    			}
    			staff.setPhonelist(phoneList);

    			entry.setStaffMember(staff);
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
    	RosterEntry entry = new RosterEntry();
    	try
    	{
    		//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
    		//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.RosterByTime"));
    		query.setString(1, convertDate(startTime));
    		query.setString(2, convertDate(endTime));
    		final ResultSet rs = query.executeQuery();

    		while(rs.next())
    		{
    			entry.setRosterId(rs.getInt("ro.roster_ID"));
    			entry.setStationId(rs.getInt("ro.location_ID"));
    			entry.setStation(rs.getString("lo.locationname"));
    			entry.setStaffmemberId(rs.getInt("ro.staffmember_ID"));
    			entry.setUsername(rs.getString("e.username"));
    			entry.setCreatedByUser(rs.getString("ro.entry_createdBy"));
    			entry.setPlannedStartOfWork(convertDateIntoLong(rs.getString("ro.starttime")));
    			entry.setPlannedEndOfWork(convertDateIntoLong(rs.getString("ro.endtime")));
    			entry.setRealStartOfWork(convertDateIntoLong(rs.getString("ro.checkIn")));
    			entry.setRealEndOfWork(convertDateIntoLong(rs.getString("ro.checkOut")));
    			entry.setServicetypeId(rs.getInt("ro.servicetype_ID"));
    			entry.setServicetype(rs.getString("st.servicetype"));
    			entry.setJobId(rs.getInt("ro.job_ID"));
    			entry.setJob(rs.getString("j.jobname"));
    			entry.setRosterNotes(rs.getString("ro.note"));
    			entry.setStandby(rs.getBoolean("ro.standby"));

    			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
    			query2.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs2 = query2.executeQuery();

    			StaffMember staff = new StaffMember();
    			rs2.first();    		
    			staff.setPersonId(rs2.getInt("e.staffmember_ID"));
    			staff.setPrimaryLocation(rs2.getInt("e.primaryLocation"));
    			staff.setPrimaryLocationName(rs2.getString("lo.locationname"));
    			staff.setLastName(rs2.getString("e.lastname"));
    			staff.setFirstName(rs2.getString("e.firstname"));
    			staff.setStreetname(rs2.getString("e.street"));
    			staff.setCityname(rs2.getString("e.city"));
    			staff.setSex(rs2.getBoolean("e.sex"));
    			staff.setBirthday(convertDateIntoLong(rs2.getString("e.birthday")));
    			staff.setEMail(rs2.getString("e.email"));
    			staff.setUserName(rs2.getString("e.username"));

    			//ph.phonenumber, ph.phonenumber_ID
    			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
    			query3.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs3 = query3.executeQuery();

    			List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
    			while(rs3.next())
    			{
    				MobilePhoneDetail phone = new MobilePhoneDetail();

    				phone.setMobilePhoneId(rs3.getInt("ph.phonenumber_ID"));
    				phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
    				phoneList.add(phone);
    			}
    			staff.setPhonelist(phoneList);

    			entry.setStaffMember(staff);
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
    public List<RosterEntry> listRosterEntrys()
    {
    	List<RosterEntry> entrylist = new ArrayList<RosterEntry>();
    	RosterEntry entry = new RosterEntry();
    	try
    	{
    		//ro.roster_ID, ro.location_ID, lo.locationname, ro.entry_createdBy, e.username, , ro.staffmember_ID, ro.servicetype_ID, 
    		//st.servicetype, ro.job_ID, j.jobname, ro.starttime, ro.endtime, ro.checkIn, ro.checkOut, ro.note, ro.standby
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("llist.RosterEntrys"));
    		final ResultSet rs = query.executeQuery();

    		while(rs.next())
    		{
    			entry.setRosterId(rs.getInt("ro.roster_ID"));
    			entry.setStationId(rs.getInt("ro.location_ID"));
    			entry.setStation(rs.getString("lo.locationname"));
    			entry.setStaffmemberId(rs.getInt("ro.staffmember_ID"));
    			entry.setUsername(rs.getString("e.username"));
    			entry.setCreatedByUser(rs.getString("ro.entry_createdBy"));
    			entry.setPlannedStartOfWork(convertDateIntoLong(rs.getString("ro.starttime")));
    			entry.setPlannedEndOfWork(convertDateIntoLong(rs.getString("ro.endtime")));
    			entry.setRealStartOfWork(convertDateIntoLong(rs.getString("ro.checkIn")));
    			entry.setRealEndOfWork(convertDateIntoLong(rs.getString("ro.checkOut")));
    			entry.setServicetypeId(rs.getInt("ro.servicetype_ID"));
    			entry.setServicetype(rs.getString("st.servicetype"));
    			entry.setJobId(rs.getInt("ro.job_ID"));
    			entry.setJob(rs.getString("j.jobname"));
    			entry.setRosterNotes(rs.getString("ro.note"));
    			entry.setStandby(rs.getBoolean("ro.standby"));

    			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
    			query2.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs2 = query2.executeQuery();

    			StaffMember staff = new StaffMember();
    			rs2.first();    		
    			staff.setPersonId(rs2.getInt("e.staffmember_ID"));
    			staff.setPrimaryLocation(rs2.getInt("e.primaryLocation"));
    			staff.setPrimaryLocationName(rs2.getString("lo.locationname"));
    			staff.setLastName(rs2.getString("e.lastname"));
    			staff.setFirstName(rs2.getString("e.firstname"));
    			staff.setStreetname(rs2.getString("e.street"));
    			staff.setCityname(rs2.getString("e.city"));
    			staff.setSex(rs2.getBoolean("e.sex"));
    			staff.setBirthday(convertDateIntoLong(rs2.getString("e.birthday")));
    			staff.setEMail(rs2.getString("e.email"));
    			staff.setUserName(rs2.getString("e.username"));

    			//ph.phonenumber, ph.phonenumber_ID
    			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
    			query3.setInt(1, rs.getInt("ro.staffmember_ID"));
    			final ResultSet rs3 = query3.executeQuery();

    			List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
    			while(rs3.next())
    			{
    				MobilePhoneDetail phone = new MobilePhoneDetail();

    				phone.setMobilePhoneId(rs3.getInt("ph.phonenumber_ID"));
    				phone.setMobilePhoneNumber(rs3.getString("ph.phonenumber"));
    				phoneList.add(phone);
    			}
    			staff.setPhonelist(phoneList);

    			entry.setStaffMember(staff);
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
