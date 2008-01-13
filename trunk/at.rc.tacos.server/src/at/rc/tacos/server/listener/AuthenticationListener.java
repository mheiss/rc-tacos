package at.rc.tacos.server.listener;

import java.sql.SQLException;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.server.dao.DaoService;

public class AuthenticationListener extends ServerListenerAdapter
{
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
        UserLoginDAO userDao = DaoService.getInstance().getFactory().createUserDAO();
        EmployeeDAO staffDao = DaoService.getInstance().getFactory().createStaffMemberDAO();
        boolean loggedIn = false;
        try
        {
            loggedIn = userDao.checkLogin(username, password);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        if(loggedIn)
        {
            login.resetPassword();
            login.setLoggedIn(true);
            try
            {
                login.setUserInformation(staffDao.getStaffMemberByUsername(username));
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
        else
        {
            login.resetPassword();
            login.setErrorMessage("Wrong username or password");
            login.setLoggedIn(false);
        }
        //return the message
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
