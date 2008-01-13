package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.model.Login;

/**
 * Data source for login/logout
 * @author Michael
 */
public class UserDAOMemory implements UserLoginDAO
{
    //the shared instance
    private static UserDAOMemory instance;
    
    //the data list
    private List<Login> userList;
    
    /**
     * Default class constructor
     */
    private UserDAOMemory()
    {
        userList = new ArrayList<Login>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static UserDAOMemory getInstance()
    {
        if (instance == null)
            instance = new UserDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        userList.clear();
    }
    
    /**
     * Checks the given username and password.
     * @param username the username to check
     * @param password the password to check
     * @return the id of the logged in user or -1 if the login failed
     */
    @Override
    public boolean checkLogin(String username,String password)
    {
        //loop over all user logins
        for(Login login:userList)
        {
            if(username.equals(login.getUsername()) && password.equals(login.getPassword()))
                return true;
        }
        //no valid login
        return false;
    }
}
