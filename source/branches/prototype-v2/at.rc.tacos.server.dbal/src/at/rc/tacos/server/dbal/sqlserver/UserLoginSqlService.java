package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.services.dbal.AuthenticationService;
import at.rc.tacos.platform.util.PasswordEncryption;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for user login.
 * 
 * @author Michael
 */
public class UserLoginSqlService implements AuthenticationService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	@Service(clazz = StaffMemberService.class)
	private StaffMemberService staffDAO;

	@Service(clazz = CompetenceService.class)
	private CompetenceService competenceDAO;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int checkLogin(String username, String pwdHash, boolean isWebClient) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("check.UserLogin"));
		query.setString(1, username);
		query.setString(2, PasswordEncryption.getInstance().encrypt(pwdHash));
		final ResultSet rs = query.executeQuery();
		// asser we have a result set
		if (rs.next()) {
			// check if the member is locked
			if (rs.getBoolean("locked") == true)
				return AuthenticationService.LOGIN_DENIED;

			// get the staff member by the username
			StaffMember member = staffDAO.getStaffMemberByUsername(username);
			List<Competence> competences = new ArrayList<Competence>();
			competences = competenceDAO.listCompetencesOfStaffMember(member.getStaffMemberId());

			boolean isDispo = false;
			for (Competence comp : competences) {
				if (comp.getCompetenceName().equalsIgnoreCase("Leitstellendisponent"))
					isDispo = true;
			}

			// check if the member is allowed to login
			if (!isDispo && !isWebClient)
				return AuthenticationService.LOGIN_NO_DISPONENT;

			// everything if ok
			return AuthenticationService.LOGIN_SUCCESSFULL;
		}
		return AuthenticationService.LOGIN_FAILED;
	}

	@Override
	public Login getLoginAndStaffmember(String username) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.loginByUsername"));
		query.setString(1, username);
		final ResultSet rs = query.executeQuery();
		if (rs.next()) {
			Login login = new Login();
			login.setAuthorization(rs.getString("authorisation"));
			login.setIslocked(rs.getBoolean("locked"));
			login.setLoggedIn(rs.getBoolean("isloggedin"));
			login.setUsername(rs.getString("username"));
			// query the staff member
			login.setUserInformation(staffDAO.getStaffMemberByUsername(username));
			return login;
		}
		// nothing found
		return null;
	}

	@Override
	public boolean addLogin(Login login) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.User"));
		query.setString(1, login.getUsername());
		query.setString(2, PasswordEncryption.getInstance().encrypt(login.getPassword()));
		query.setString(3, login.getAuthorization());
		query.setBoolean(4, login.isLoggedIn());
		query.setBoolean(5, login.isIslocked());
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean lockLogin(String username) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.lockUser"));
		query.setBoolean(1, true);
		query.setString(2, username);
		// assert the lock was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean unlockLogin(String username) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.lockUser"));
		query.setBoolean(1, false);
		query.setString(2, username);
		// assert the unlock was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateLogin(Login login) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.User"));
		query.setString(1, login.getAuthorization());
		query.setBoolean(2, login.isLoggedIn());
		query.setBoolean(3, login.isIslocked());
		query.setString(4, login.getUsername());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updatePassword(String username, String newPwd) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.Password"));
		query.setString(1, PasswordEncryption.getInstance().encrypt(newPwd));
		query.setString(2, username);
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public List<Login> listLogins() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.User"));
		final ResultSet rs = query.executeQuery();
		List<Login> loginList = new ArrayList<Login>();
		while (rs.next()) {
			Login login = new Login();
			login.setUsername(rs.getString("username"));
			login.setAuthorization(rs.getString("authorisation"));
			login.setIslocked(rs.getBoolean("locked"));
			login.setLoggedIn(rs.getBoolean("isloggedin"));
			loginList.add(login);
		}
		return loginList;
	}

	public List<Login> listLoginsAndStaffMemberByUsername(String username) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.UserByUsername"));
		query.setString(1, username);
		final ResultSet rs = query.executeQuery();
		List<Login> loginList = new ArrayList<Login>();
		while (rs.next()) {
			Login login = new Login();
			login.setUsername(rs.getString("username"));
			login.setAuthorization(rs.getString("authorisation"));
			login.setIslocked(rs.getBoolean("locked"));
			login.setLoggedIn(rs.getBoolean("isloggedin"));
			login.setUserInformation(staffDAO.getStaffMemberByUsername(username));
			loginList.add(login);
		}
		return loginList;
	}

}
