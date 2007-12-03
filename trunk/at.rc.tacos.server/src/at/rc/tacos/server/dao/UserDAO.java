package at.rc.tacos.server.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Data source for login/logout
 * @author Michael
 */
public class UserDAO
{
    //the shared instance
    private static UserDAO instance;
    //the data list
    private Map<String, String> userList;
    
    /**
     * Default private class constructor
     */
    private UserDAO()
    {
        //create the list
        userList = new HashMap<String, String>();
        //load dummy data
        userList.put("user1", "P@ssw0rd");
        userList.put("user2", "P@ssw0rd");
        userList.put("user3", "P@ssw0rd");
    }
    
    /**
     * Creates and returns the shared instance
     */
    public static UserDAO getInstance()
    {
        if( instance == null)
            instance = new UserDAO();
        return instance;
    }
}
