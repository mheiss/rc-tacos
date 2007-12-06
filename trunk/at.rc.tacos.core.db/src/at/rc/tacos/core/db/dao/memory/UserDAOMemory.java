package at.rc.tacos.core.db.dao.memory;

import java.util.HashMap;
import java.util.Map;
import at.rc.tacos.core.db.dao.UserLoginDAO;

/**
 * Data source for login/logout
 * @author Michael
 */
public class UserDAOMemory implements UserLoginDAO
{
    //the shared instance
    private static UserDAOMemory instance;
    
    //the data list
    private Map<String, String> userList;
    
    /**
     * Default class constructor
     */
    private UserDAOMemory()
    {
        userList = new HashMap<String, String>();
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
        userList = new HashMap<String, String>();
    }
    
    /**
     * Checks the given username and password.
     * @param username the username to check
     * @param password the password to check
     * @return true if the username and password matches
     */
    @Override
    public boolean checkLogin(String username,String password)
    {
        //get the password for this username
        String pwd = userList.get(username);
        //check it
        if(password.equals(pwd))
            return true;
        return false;
    }

    /**
     * Adds a new login to the login list
     * @param username the username to add
     * @param password the password to authenticate the user
     */
    @Override
    public void addUserLogin(String username, String password)
    {
        userList.put(username,password);
    }
}
