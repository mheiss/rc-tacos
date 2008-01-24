package at.rc.tacos.server.listener;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.StaffMember;

public class AuthenticationListener extends ServerListenerAdapter
{
    //The DAO classes
    private UserLoginDAO userDao = DaoFactory.TEST.createUserDAO();
    private StaffMemberDAO memberDao = DaoFactory.TEST.createStaffMemberDAO();  
    /**
     * Handles the login message and checks the authentication.<br>
     * The password and the username will be checked against the database.
     */
    @Override
    public Login handleLoginRequest(AbstractMessage message)
    {
        //convert to login
        Login login = (Login)message;
        //check the password and the user
        String username = login.getUsername();
        String password = login.getPassword();
        //check agains the database        
        int loginResult = userDao.checkLogin(username, password);
        //for security reset the password
        login.resetPassword();
        if(loginResult == UserLoginDAO.LOGIN_SUCCESSFULL)
        {
            System.out.println("Login successfully, checking member");
        	//get the infos out of the database
        	StaffMember member = memberDao.getStaffMemberByUsername(username);
        	if(member != null)
        	{
        		login.setUserInformation(member);
        		login.setLoggedIn(true);
        		System.out.println("member check successfully");
        	}
        	else
        	{
        		login.setLoggedIn(false);
            	login.setErrorMessage("Failed to get the staff details for the given login");
        	}
        }
        else if(loginResult == UserLoginDAO.LOGIN_FAILED)
        {
        	login.setLoggedIn(false);
        	login.setErrorMessage("Wrong username or password");
        }
        else if(loginResult == UserLoginDAO.LOGIN_DENIED)
        {
        	login.setLoggedIn(false);
        	login.setIslocked(true);
        	login.setErrorMessage("Your account is locked, please contact the administrator");
        }
        else
        {
        	login.setLoggedIn(false);
        	login.setErrorMessage("Unexpected error occured");	
        }
        //return the message
        System.out.println("returning: "+login);
        return login;
    }

    /**
     * Handles the logout process.
     */
    @Override
    public AbstractMessage handleLogoutRequest(AbstractMessage message)
    {
        //convert to logout
        Logout logout = (Logout)message;
        logout.setLoggedOut(true);
        return logout;
    }
}
