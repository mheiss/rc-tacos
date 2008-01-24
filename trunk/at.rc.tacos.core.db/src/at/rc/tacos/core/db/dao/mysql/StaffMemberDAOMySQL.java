package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.model.*;

public class StaffMemberDAOMySQL implements StaffMemberDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";
	
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

	public List<StaffMember> getAllStaffMembers()
	{
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		StaffMember staff = new StaffMember();
		Location station = new Location();
		Competence competence = new Competence();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembers"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
	    		
	    		station.setId(rs.getInt("e.primaryLocation"));
	    		station.setLocationName(rs.getString("lo.locationname"));
	    		staff.setPrimaryLocation(station);
	    		
	    		staff.setLastName(rs.getString("e.lastname"));
	    		staff.setFirstName(rs.getString("e.firstname"));
	    		staff.setStreetname(rs.getString("e.street"));
	    		staff.setCityname(rs.getString("e.city"));
	    		staff.setMale(rs.getBoolean("e.sex"));
	    		staff.setBirthday(convertDateIntoLong(rs.getString("e.birthday")));
	    		staff.setEMail(rs.getString("e.email"));
	    		staff.setUserName(rs.getString("e.username"));
	    		
	    		{
	    			competence.setId(rs.getInt("c.competence_ID"));
	    			competence.setCompetenceName(rs.getString("c.competence"));
	    			competences.add(competence);
	    		}while(rs.next());
	    		staff.setCompetenceList(competences);

	    		//ph.phonenumber, ph.phonenumber_ID
	    		final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
	    		query3.setInt(1, rs.getInt("ro.staffmember_ID"));
	    		final ResultSet rs2 = query3.executeQuery();
	    		
	    		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
	    		while(rs2.next())
	    		{
	    			MobilePhoneDetail phone = new MobilePhoneDetail();
	    			
	    			phone.setId(rs2.getInt("ph.phonenumber_ID"));
	    			phone.setMobilePhoneNumber(rs2.getString("ph.phonenumber"));
	    			phoneList.add(phone);
	    		}
	    		staff.setPhonelist(phoneList);
	    		
	    		staffMembers.add(staff);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staffMembers;
	}
	
	public List<StaffMember> getStaffMembersFromLocation(int locationId)
	{
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		StaffMember staff = new StaffMember();
		Location station = new Location();
		Competence competence = new Competence();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembersFromLocation"));
			query.setInt(1, locationId);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
	    		
	    		station.setId(rs.getInt("e.primaryLocation"));
	    		station.setLocationName(rs.getString("lo.locationname"));
	    		staff.setPrimaryLocation(station);
	    		
	    		staff.setLastName(rs.getString("e.lastname"));
	    		staff.setFirstName(rs.getString("e.firstname"));
	    		staff.setStreetname(rs.getString("e.street"));
	    		staff.setCityname(rs.getString("e.city"));
	    		staff.setMale(rs.getBoolean("e.sex"));
	    		staff.setBirthday(convertDateIntoLong(rs.getString("e.birthday")));
	    		staff.setEMail(rs.getString("e.email"));
	    		staff.setUserName(rs.getString("e.username"));
	    		
	    		{
	    			competence.setId(rs.getInt("c.competence_ID"));
	    			competence.setCompetenceName(rs.getString("c.competence"));
	    			competences.add(competence);
	    		}while(rs.next());
	    		staff.setCompetenceList(competences);

	    		//ph.phonenumber, ph.phonenumber_ID
	    		final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
	    		query3.setInt(1, rs.getInt("ro.staffmember_ID"));
	    		final ResultSet rs2 = query3.executeQuery();
	    		
	    		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
	    		while(rs2.next())
	    		{
	    			MobilePhoneDetail phone = new MobilePhoneDetail();
	    			
	    			phone.setId(rs2.getInt("ph.phonenumber_ID"));
	    			phone.setMobilePhoneNumber(rs2.getString("ph.phonenumber"));
	    			phoneList.add(phone);
	    		}
	    		staff.setPhonelist(phoneList);
	    		
	    		staffMembers.add(staff);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staffMembers;
	}
	
	public StaffMember getStaffMemberByID(int id)
	{
		StaffMember staff = new StaffMember();
		Location station = new Location();
		Competence competence = new Competence();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			rs.first();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
	    		
	    		station.setId(rs.getInt("e.primaryLocation"));
	    		station.setLocationName(rs.getString("lo.locationname"));
	    		staff.setPrimaryLocation(station);
	    		
	    		staff.setLastName(rs.getString("e.lastname"));
	    		staff.setFirstName(rs.getString("e.firstname"));
	    		staff.setStreetname(rs.getString("e.street"));
	    		staff.setCityname(rs.getString("e.city"));
	    		staff.setMale(rs.getBoolean("e.sex"));
	    		staff.setBirthday(convertDateIntoLong(rs.getString("e.birthday")));
	    		staff.setEMail(rs.getString("e.email"));
	    		staff.setUserName(rs.getString("e.username"));
	    		
	    		{
	    			competence.setId(rs.getInt("c.competence_ID"));
	    			competence.setCompetenceName(rs.getString("c.competence"));
	    			competences.add(competence);
	    		}while(rs.next());
	    		staff.setCompetenceList(competences);

	    		//ph.phonenumber, ph.phonenumber_ID
	    		final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
	    		query3.setInt(1, rs.getInt("ro.staffmember_ID"));
	    		final ResultSet rs2 = query3.executeQuery();
	    		
	    		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
	    		while(rs2.next())
	    		{
	    			MobilePhoneDetail phone = new MobilePhoneDetail();
	    			
	    			phone.setId(rs2.getInt("ph.phonenumber_ID"));
	    			phone.setMobilePhoneNumber(rs2.getString("ph.phonenumber"));
	    			phoneList.add(phone);
	    		}
	    		staff.setPhonelist(phoneList);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staff;
	}
	
	public StaffMember getStaffMemberByUsername(String username)
	{
		StaffMember staff = new StaffMember();
		Location station = new Location();
		Competence competence = new Competence();
		List<Competence> competences = new ArrayList<Competence>();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberbyUsername"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();

			rs.first();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
	    		
	    		station.setId(rs.getInt("e.primaryLocation"));
	    		station.setLocationName(rs.getString("lo.locationname"));
	    		staff.setPrimaryLocation(station);
	    		
	    		staff.setLastName(rs.getString("e.lastname"));
	    		staff.setFirstName(rs.getString("e.firstname"));
	    		staff.setStreetname(rs.getString("e.street"));
	    		staff.setCityname(rs.getString("e.city"));
	    		staff.setMale(rs.getBoolean("e.sex"));
	    		staff.setBirthday(convertDateIntoLong(rs.getString("e.birthday")));
	    		staff.setEMail(rs.getString("e.email"));
	    		staff.setUserName(rs.getString("e.username"));
	    		
	    		{
	    			competence.setId(rs.getInt("c.competence_ID"));
	    			competence.setCompetenceName(rs.getString("c.competence"));
	    			competences.add(competence);
	    		}while(rs.next());
	    		staff.setCompetenceList(competences);

	    		//ph.phonenumber, ph.phonenumber_ID
	    		final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
	    		query3.setInt(1, rs.getInt("ro.staffmember_ID"));
	    		final ResultSet rs2 = query3.executeQuery();
	    		
	    		List<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
	    		while(rs2.next())
	    		{
	    			MobilePhoneDetail phone = new MobilePhoneDetail();
	    			
	    			phone.setId(rs2.getInt("ph.phonenumber_ID"));
	    			phone.setMobilePhoneNumber(rs2.getString("ph.phonenumber"));
	    			phoneList.add(phone);
	    		}
	    		staff.setPhonelist(phoneList);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staff;
	}
}
