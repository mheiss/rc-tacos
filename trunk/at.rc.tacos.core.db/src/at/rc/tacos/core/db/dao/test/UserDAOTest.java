package at.rc.tacos.core.db.dao.test;

import java.util.Map;
import at.rc.tacos.core.db.dao.UserLoginDAO;

/**
 * Data source for login/logout
 * @author Michael
 */
public class UserDAOTest implements UserLoginDAO
{
    //the shared instance
    private static UserDAOTest instance;
    
    //the data list
    private Map<String, String> userList;
    
    /**
     * Default class constructor
     */
    private UserDAOTest()
    {
        userList = new TestDataSource().userLogin;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static UserDAOTest getInstance()
    {
        if (instance == null)
            instance = new UserDAOTest();
        return instance;
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
        if(password.equalsIgnoreCase(pwd))
            return true;
        return false;
    }
}
