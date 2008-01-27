package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.MyUtils;

public class UserLoginDAOMySQL implements UserLoginDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int checkLogin(String username, String pwdHash)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("check.UserLogin"));
			query.setString(1, username);
			query.setString(2, pwdHash);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				if (rs.getString("username") != null && rs.getBoolean("locked") == false)
					return 0;
				else if(rs.getString("username") == null)
					return -1;
				else if(rs.getBoolean("locked") == true)
					return -2;
			}
			else return -1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	@Override
	public Login getLoginAndStaffmember(String username)
	{
		Login login = new Login();
		StaffMember staff = new StaffMember();
		Location station = new Location();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//u.authorization, u.isloggedin, u.locked, u.pwd, e.city, e.street, pe.phonenumber_ID, ph.phonenumber, c.competence_ID, c.competence
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberbyUsername"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				login.setAuthorization(rs.getString("u.authorization"));
				login.setIslocked(rs.getBoolean("u.locked"));
				login.setLoggedIn(rs.getBoolean("u.isloggedin"));
				login.setUsername(rs.getString("u.username"));
				login.setPassword(rs.getString("u.pwd"));
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

				final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.competenceOfStaffMember"));
				query2.setInt(1, staff.getStaffMemberId());
				final ResultSet rs2 = query2.executeQuery();
				while(rs2.next())
				{
					Competence competence = new Competence();
					competence.setId(rs.getInt("c.competence_ID"));
					competence.setCompetenceName(rs.getString("c.competence"));
					staff.addCompetence(competence);
				}
			}
			else return null;

			//ph.phonenumber, ph.phonenumber_ID
			final PreparedStatement query3 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.PhonenumbersOfMemberID"));
			query3.setInt(1, staff.getStaffMemberId());
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
			login.setUserInformation(staff);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return login;
	}

	@Override
	public int addLogin(Login login)
	{
		int staffMemberId = 0;
		try
		{	
			// username, pwd, authorization, isloggedin, locked
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.User"));
			query.setString(1, login.getUsername());
			query.setString(2, login.getPassword());
			query.setString(3, login.getAuthorization());
			query.setBoolean(4, login.isLoggedIn());
			query.setBoolean(5, login.isIslocked());
			query.executeUpdate();

			// staffmember_ID, primaryLocation, firstname, lastname, sex, birthday, email, street, city, username
			final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.staffmember"));
			query2.setInt(1, login.getUserInformation().getStaffMemberId());
			query2.setInt(2, login.getUserInformation().getPrimaryLocation().getId());
			query2.setString(3, login.getUserInformation().getFirstName());
			query2.setString(4, login.getUserInformation().getLastName());
			query2.setBoolean(5, login.getUserInformation().isMale());
			query2.setString(6, MyUtils.timestampToString(login.getUserInformation().getBirthday(), MyUtils.sqlDate));
			query2.setString(7, login.getUserInformation().getEMail());
			query2.setString(8, login.getUserInformation().getStreetname());
			query2.setString(9, login.getUserInformation().getCityname());
			query2.setString(10, login.getUsername());
			query2.executeUpdate();

			// TODO check if this works ...
			for(int i=0; i >= login.getUserInformation().getCompetenceList().size(); i++)
			{
				//staffmember_ID, competence_ID
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("add.competenceToStaffMember"));
				query1.setInt(1, login.getUserInformation().getStaffMemberId());
				query1.setInt(2, login.getUserInformation().getCompetenceList().get(i).getId());
				query1.executeUpdate();
			}

			for(int i=0; i>=login.getUserInformation().getPhonelist().size(); i++)
			{
				//staffmember_ID, phonenumber_ID
				final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.Phonestaffmember"));
				query1.setInt(1, login.getUserInformation().getStaffMemberId());
				query1.setInt(2, login.getUserInformation().getPhonelist().get(i).getId());
				query1.executeUpdate();
			}

			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.staffmemberId"));
			query1.setString(1, login.getUsername());
			final ResultSet rsStaffMemberId = query1.executeQuery();

			if(rsStaffMemberId.first())
				staffMemberId = rsStaffMemberId.getInt("staffmember_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return staffMemberId;
	}

	@Override
	public boolean lockLogin(String username)
	{
		try
		{
			// locked = ? WHERE username = ?
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.lockUser"));
			query.setBoolean(1, true);
			query.setString(2, username);
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
	public boolean unlockLogin(String username)
	{
		try
		{
			// locked = ? WHERE username = ?
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.lockUser"));
			query.setBoolean(1, false);
			query.setString(2, username);
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
	public boolean updateLogin(Login login)
	{
		try
		{
			// pwd = ?, authorization = ?, isloggedin = ?, locked = ? WHERE username
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.User"));
			query.setString(1, login.getPassword());
			query.setString(2, login.getAuthorization());
			query.setBoolean(3, login.isLoggedIn());
			query.setBoolean(4, login.isIslocked());
			query.setString(5, login.getUsername());
			query.executeUpdate();

			// primaryLocation = ?, firstname = ?, lastname = ?, sex = ?, birthday = ?, email = ?, street = ?, city = ?, username = ? where staffmember_ID = ?;
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.staffmember"));
			query1.setInt(1, login.getUserInformation().getPrimaryLocation().getId());
			query1.setString(2, login.getUserInformation().getFirstName());
			query1.setString(3, login.getUserInformation().getLastName());
			query1.setBoolean(4, login.getUserInformation().isMale());
			query1.setString(5, MyUtils.timestampToString(login.getUserInformation().getBirthday(), MyUtils.sqlDate));
			query1.setString(6, login.getUserInformation().getEMail());
			query1.setString(7, login.getUserInformation().getStreetname());
			query1.setString(8, login.getUserInformation().getCityname());
			query1.setString(9, login.getUserInformation().getUserName());
			query1.setInt(10, login.getUserInformation().getStaffMemberId());
			query1.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

}