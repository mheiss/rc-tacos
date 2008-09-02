package at.rc.tacos.server.dbal.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.util.PasswordEncryption;
import at.rc.tacos.server.dbal.DataSource;
import at.rc.tacos.server.dbal.SQLQueries;
import at.rc.tacos.server.dbal.dao.CompetenceDAO;
import at.rc.tacos.server.dbal.dao.StaffMemberDAO;
import at.rc.tacos.server.dbal.dao.UserLoginDAO;
import at.rc.tacos.server.dbal.factory.DaoFactory;

public class UserLoginDAOSQL implements UserLoginDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();
	//the dependent dao classes
	private final StaffMemberDAO staffDAO = DaoFactory.SQL.createStaffMemberDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.SQL.createCompetenceDAO();

	@Override
	public int checkLogin(String username, String pwdHash, boolean isWebClient) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("check.UserLogin"));
			query.setString(1, username);
			query.setString(2, PasswordEncryption.getInstance().encrypt(pwdHash));
			final ResultSet rs = query.executeQuery();
			//asser we have a result set
			if(rs.next())
			{
				//check if the member is locked
				if(rs.getBoolean("locked") == true)
					return UserLoginDAO.LOGIN_DENIED;
								
				//get the staff member by the username
				StaffMember member = staffDAO.getStaffMemberByUsername(username);
				List<Competence> competences = new ArrayList<Competence>();
				competences = competenceDAO.listCompetencesOfStaffMember(member.getStaffMemberId());
				
				boolean isDispo = false;
				for(Competence comp : competences)
				{
					if (comp.getCompetenceName().equalsIgnoreCase("Leitstellendisponent"))
						isDispo = true;
				}
				
				//check if the member is allowed to login
				if(!isDispo && !isWebClient)
					return UserLoginDAO.LOGIN_NO_DISPONENT;
				
				//everything if ok
				return UserLoginDAO.LOGIN_SUCCESSFULL;
			}
			return UserLoginDAO.LOGIN_FAILED;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public Login getLoginAndStaffmember(String username) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.loginByUsername"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();
			//assert we have a result
			if(rs.next())
			{
				Login login = new Login();
				login.setAuthorization(rs.getString("authorisation"));
				login.setIslocked(rs.getBoolean("locked"));
				login.setLoggedIn(rs.getBoolean("isloggedin"));
				login.setUsername(rs.getString("username"));
				//query the staff member
				login.setUserInformation(staffDAO.getStaffMemberByUsername(username));
				return login;
			}
			//nothing found 
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean addLogin(Login login) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// username, pwd, authorisation, isloggedin, locked
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.User"));
			query.setString(1, login.getUsername());
			query.setString(2, PasswordEncryption.getInstance().encrypt(login.getPassword()));
			query.setString(3, login.getAuthorization());
			query.setBoolean(4, login.isLoggedIn());
			query.setBoolean(5, login.isIslocked());
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
	public boolean lockLogin(String username) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// locked = ? WHERE username = ?
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.lockUser"));
			query.setBoolean(1, true);
			query.setString(2, username);
			//assert the lock was successfully
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
	public boolean unlockLogin(String username) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// locked = ? WHERE username = ?
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.lockUser"));
			query.setBoolean(1, false);
			query.setString(2, username);
			//assert the unlock was successfully
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
	public boolean updateLogin(Login login) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// authorization = ?, isloggedin = ?, locked = ? WHERE username
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.User"));
			query.setString(1, login.getAuthorization());
			query.setBoolean(2, login.isLoggedIn());
			query.setBoolean(3, login.isIslocked());
			query.setString(4, login.getUsername());
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
	public boolean updatePassword(String username, String newPwd) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// pwd = ? WHERE username
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.Password"));
			query.setString(1, PasswordEncryption.getInstance().encrypt(newPwd));
			query.setString(2, username);
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
	public List<Login> listLogins() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.User"));
			final ResultSet rs = query.executeQuery();
			//loop and add the logins
			List<Login> loginList = new ArrayList<Login>();
			while(rs.next())
			{
				Login login = new Login();
				login.setUsername(rs.getString("username"));
				login.setAuthorization(rs.getString("authorisation"));
				login.setIslocked(rs.getBoolean("locked"));
				login.setLoggedIn(rs.getBoolean("isloggedin"));
				loginList.add(login);
			}
			return loginList;
		}
		finally
		{
			connection.close();
		}
	}
	
	public List<Login> listLoginsAndStaffMemberByUsername(String username) throws SQLException {
		Connection connection = source.getConnection();
		try {
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
		finally {
			connection.close();
		}
	}

}