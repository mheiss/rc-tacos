package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.*;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class StaffMemberDAOMySQL implements StaffMemberDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();

	public List<StaffMember> getAllStaffMembers()
	{
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembers"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));

				Location station = new Location();
				station.setId(rs.getInt("e.primaryLocation"));
				station.setLocationName(rs.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));

				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));

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
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.staffmembersFromLocation"));
			query.setInt(1, locationId);
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				Location station = new Location();
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));

				station.setId(rs.getInt("e.primaryLocation"));
				station.setLocationName(rs.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));

				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));

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
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberByID"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));

				Location station = new Location();
				station.setId(rs.getInt("e.primaryLocation"));
				station.setLocationName(rs.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));

				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			}
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
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberbyUsername"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));

				Location station = new Location();
				station.setId(rs.getInt("e.primaryLocation"));
				station.setLocationName(rs.getString("lo.locationname"));
				staff.setPrimaryLocation(station);

				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));

				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return staff;
	}
}
