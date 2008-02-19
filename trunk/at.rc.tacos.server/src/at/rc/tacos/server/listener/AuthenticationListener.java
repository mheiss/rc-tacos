package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;

public class AuthenticationListener extends ServerListenerAdapter
{
	//The DAO classes
	private UserLoginDAO userDao = DaoFactory.MYSQL.createUserDAO();

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException 
	{
		Login login = (Login)addObject;
		if(!userDao.addLogin(login))
			throw new DAOException("AuthenticationListener","Failed to add the login "+ login +" to the database");
		return login;
	}

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException  
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<Login> loginList = userDao.listLogins();
		if(loginList == null)
			throw new DAOException("AuthenticationListener","Failed to list the logins");
		list.addAll(loginList);
		return list;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException ,SQLException 
	{
		Login login = (Login)removeObject;
		//the user will only be locked, removing is not possible
		if(!userDao.lockLogin(login.getUsername()))
			throw new DAOException("AuthenticationListener","Failed to lock the login: "+login);
		return login;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException 
	{
		Login updateLogin = (Login)updateObject;		
		//check if we have a different password
		if(updateLogin.getPassword() != null)
		{
			if(!userDao.updatePassword(updateLogin.getUsername(), updateLogin.getPassword()))
				throw new DAOException("AuthenticationListener","Failed to update the password for the login "+updateLogin);
		}
		//update the other fields
		if(!userDao.updateLogin(updateLogin))
			throw new DAOException("AuthenticationListener","Failed to update the login: "+updateLogin);
		//reset the password
		updateLogin.resetPassword();
		return updateLogin;
	}

	/**
	 * Handles the login message and checks the authentication.<br>
	 * The password and the username will be checked against the database.
	 */
	@Override
	public Login handleLoginRequest(AbstractMessage message) throws DAOException,SQLException 
	{
		//convert to login
		Login login = (Login)message;
		//check the password and the user
		String username = login.getUsername();
		String password = login.getPassword();
		boolean isWebClient = login.isWebClient();
		//check agains the database        
		int loginResult = userDao.checkLogin(username, password);
		//for security reset the password
		login.resetPassword();
		if(loginResult == UserLoginDAO.LOGIN_SUCCESSFULL)
		{
			//get the infos out of the database
			login = userDao.getLoginAndStaffmember(username);
			if(login == null)
				throw new DAOException("AuthenticationListener","Failed to get the login and staff details for the given login");
			//login was successfully
			login.setWebClient(isWebClient);
			login.setLoggedIn(true);
			return login;
		}
		else if(loginResult == UserLoginDAO.LOGIN_FAILED)
		{
			login.setLoggedIn(false);
			login.setErrorMessage("Wrong username or password");
			return login;
		}
		else if(loginResult == UserLoginDAO.LOGIN_DENIED)
		{
			login.setLoggedIn(false);
			login.setIslocked(true);
			login.setErrorMessage("Your account is locked, please contact the administrator");
			return login;
		}
		else
		{
			login.setLoggedIn(false);
			login.setErrorMessage("Unexpected error occured");	
			throw new DAOException("AuthenticationListener","Failed to check the login for the username: "+username);
		}
	}

	/**
	 * Handles the logout process.
	 */
	@Override
	public AbstractMessage handleLogoutRequest(AbstractMessage message)
	{
		//convert to logout
		Logout logout = (Logout)message;
		System.out.println("Logout from user: "+logout.getUsername());
		logout.setLoggedOut(true);
		return logout;
	}
}
