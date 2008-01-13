package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.model.*;

public class StaffMemberDAOMySQL implements EmployeeDAO
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

	public List<StaffMember> getAllStaffMembers()
	{
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembers"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				StaffMember staff = new StaffMember();

				staff.setPrimaryLocation(rs.getInt("primaryLocation"));
				staff.setLastName(rs.getString("lastname"));
				staff.setFirstName(rs.getString("firstname"));
				staff.setSex(rs.getBoolean("sex"));
				staff.setBirthday(convertDateIntoLong(rs.getString("birthday")));
				staff.setEMail(rs.getString("email"));
				staff.setStreetname(rs.getString("street"));
				staff.setCityname(rs.getString("city"));
				staff.setUserName(rs.getString("username"));
				staff.setAuthorization(rs.getString("authorization"));

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
	
	public List<StaffMember> getStaffMembersFromLocation(String locationname)
	{
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembersFromLocation"));
			query.setString(1, locationname);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				StaffMember staff= new StaffMember();

				staff.setPrimaryLocation(rs.getInt("primaryLocation"));
				staff.setLastName(rs.getString("lastname"));
				staff.setFirstName(rs.getString("firstname"));
				staff.setSex(rs.getBoolean("sex"));
				staff.setBirthday(convertDateIntoLong(rs.getString("birthday")));
				staff.setEMail(rs.getString("email"));
				staff.setStreetname(rs.getString("street"));
				staff.setCityname(rs.getString("city"));
				staff.setUserName(rs.getString("username"));
				staff.setAuthorization(rs.getString("authorization"));

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
		StaffMember staffMember = new StaffMember();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembersFromLocation"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			rs.first();

			StaffMember staff= new StaffMember();

			staff.setPrimaryLocation(rs.getInt("primaryLocation"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setSex(rs.getBoolean("sex"));
			staff.setBirthday(convertDateIntoLong(rs.getString("birthday")));
			staff.setEMail(rs.getString("email"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setUserName(rs.getString("username"));
			staff.setAuthorization(rs.getString("authorization"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return staffMember;
	}
	
	public StaffMember getStaffMemberByUsername(String username)
	{
		StaffMember staffMember = new StaffMember();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembersFromLocation"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();

			rs.first();

			StaffMember staff = new StaffMember();

			staff.setPrimaryLocation(rs.getInt("primaryLocation"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setSex(rs.getBoolean("sex"));
			staff.setBirthday(convertDateIntoLong(rs.getString("birthday")));
			staff.setEMail(rs.getString("email"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setUserName(rs.getString("username"));
			staff.setAuthorization(rs.getString("authorization"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return staffMember;
	}
	
	public Integer addStaffMember(StaffMember staffMember, String pwdHash)
	{
		Integer staffMemberId = null;
		try
		{	// username, pwd, authorization
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.User"));
			query1.setString(1, staffMember.getUserName());
			query1.setString(2, pwdHash);
			query1.setString(3, staffMember.getAuthorization());
			query1.executeUpdate();
			
			// staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username
			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.staffmember"));
			query2.setInt(1, staffMember.getPersonId());
			query2.setInt(2, staffMember.getPrimaryLocation());
			query2.setString(3, staffMember.getFirstName());
			query2.setString(4, staffMember.getLastName());
			query2.setBoolean(5, staffMember.isSex());
			query2.setString(6, convertDate(staffMember.getBirthday()));
			query2.setString(7, staffMember.getEMail());
			query2.setString(8, staffMember.getStreetname());
			query2.setString(9, staffMember.getCityname());
			query2.setString(10, staffMember.getUserName());
			
			query2.executeUpdate();
			
			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberbyUsername"));
			query3.setString(1, staffMember.getUserName());
			final ResultSet rsStaffMemberId = query3.executeQuery();
			
			if(rsStaffMemberId.next())
				staffMemberId = rsStaffMemberId.getInt("staffmember_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staffMemberId;
	}

	public boolean updateStaffMember(StaffMember staffMember)
	{
		try
		{
			// primaryLocation, firstname, lastname, sex, birthday, email, street, city, username, staffmember_ID
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.staffmember"));
			query.setInt(1, staffMember.getPrimaryLocation());
			query.setString(2, staffMember.getFirstName());
			query.setString(3, staffMember.getLastName());
			query.setBoolean(4, staffMember.isSex());
			query.setString(5, convertDate(staffMember.getBirthday()));
			query.setString(6, staffMember.getEMail());
			query.setString(7, staffMember.getStreetname());
			query.setString(8, staffMember.getCityname());
			query.setString(9, staffMember.getUserName());
			query.setInt(10, staffMember.getPersonId());

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
	public boolean deleteStaffMember(StaffMember member)
	{
    	try
    	{
    		// delete.User
    		final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.User"));
    		query1.setString(1, member.getUserName());
    		query1.executeUpdate();
    		
    		//delete.staffmember
    		final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.staffmember"));
    		query2.setInt(1, member.getPersonId());
    		query2.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}
}
