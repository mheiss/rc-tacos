package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.PasswordEncryption;

public class UserLoginDAOSQL implements UserLoginDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();
	//the dependent dao classes
	private final StaffMemberDAO staffDAO = DaoFactory.SQL.createStaffMemberDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.SQL.createCompetenceDAO();

	@Override
	public int checkLogin(String username, String pwdHash) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			StaffMember member;
			
			member = staffDAO.getStaffMemberByUsername(username);
			List<Competence> competences = new ArrayList<Competence>();
			
			competences = competenceDAO.listCompetencesOfStaffMember(member.getStaffMemberId());
			
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("check.UserLogin"));
			query.setString(1, username);
			query.setString(2, PasswordEncryption.getInstance().encrypt(pwdHash));
			final ResultSet rs = query.executeQuery();
			//asser we have a result set
			if(rs.next())
			{
				boolean isDispo = false;
				for(Competence comp : competences)
				{
					if (comp.getCompetenceName().equalsIgnoreCase("Leitstellendisponent"))
						isDispo = true;
				}
				if(!isDispo)
				{
					System.out.println("im is kein dispo");
					return -3;
				}
				if (rs.getString("username") != null &! rs.getBoolean("locked"))
					return 0;
				else if(rs.getString("username") == null)
					return -1;
				else if(rs.getBoolean("locked") == true)
					return -2;
			}
			return -1;
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
}