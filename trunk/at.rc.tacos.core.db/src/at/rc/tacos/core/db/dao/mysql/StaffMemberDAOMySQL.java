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
				return staff;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
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
				System.out.println("getStaffMemberByUsername");
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
				System.out.println(staff);
				return staff;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public boolean updateStaffMember(StaffMember staffmember)
	{
		try
		{
			// primaryLocation = ?, firstname = ?, lastname = ?, sex = ?, birthday = ?, email = ?, street = ?, city = ?, username = ? where staffmember_ID = ?;
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.staffmember"));
			query1.setInt(1, staffmember.getPrimaryLocation().getId());
			query1.setString(2, staffmember.getFirstName());
			query1.setString(3, staffmember.getLastName());
			query1.setBoolean(4, staffmember.isMale());
			query1.setString(5, MyUtils.timestampToString(staffmember.getBirthday(), MyUtils.sqlDate));
			query1.setString(6, staffmember.getEMail());
			query1.setString(7, staffmember.getStreetname());
			query1.setString(8, staffmember.getCityname());
			query1.setInt(9, staffmember.getStaffMemberId());
			query1.executeUpdate();
			
			//resets all competenses to new value
			boolean result1 = updateCompetenceList(staffmember);
			if(result1 == false)
				return false;
			
			//update phonenumber
			for(MobilePhoneDetail detail:staffmember.getPhonelist())
				mobilePhoneDAO.updateMobilePhone(detail);
			
			//update connection phonenumber - staffmember
			boolean result2 = updateMobilePhoneList(staffmember);
			if(result2 == false)
				return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateCompetenceList(StaffMember staff)
	{
		try
		{
			//delete all competences of staffmember
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.competencesOfStaffMember"));
			query.setInt(1, staff.getStaffMemberId());
			query.executeUpdate();

			//inserts all new competences of staffmember
			//staffmember_ID, competence_ID
			for(Competence comp:staff.getCompetenceList())
			{
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.competenceToStaffMember"));
				query1.setInt(1, staff.getStaffMemberId());
				query1.setInt(2, comp.getId());
				query1.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	public boolean updateMobilePhoneList(StaffMember staff)
	{
		try
		{
			//delete all phonenumbers of staffmember
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.phonesOfStaffMember"));
			query.setInt(1, staff.getStaffMemberId());
			query.executeUpdate();
			
			for(MobilePhoneDetail detail:staff.getPhonelist())
			{
				//staffmember_ID, phonenumber_ID
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.Phonestaffmember"));
				query1.setInt(1, staff.getStaffMemberId());
				query1.setInt(2, detail.getId());
				query1.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
