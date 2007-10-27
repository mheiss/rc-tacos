package at.rc.tacos.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The database source to connect to a mysql database
 * @author Michael
 */
public class DataSource
{
    //OWNED, you got my password, but its only for testing
    //send a mail to m-heiss@aon.at to show me how to make it better 
    private static final String dbHost = "jdbc:mysql://projekt-itm.fh-joanneum.at/heissm";
    private static final String dbUser = "heissm";
    private static final String dbPwd = "P@ssw0rd";
    
    private static DataSource instance;
    
    /**
     * Returns the instance of the database connection
     * @return the shared instance
     */
    public static DataSource getInstance()
    {
        if (instance == null)
            instance = new DataSource();
        return instance;
    }
    
    /**
     * Returns the connection to the database.
     * When the connection cannot be established then the method will return null
     * @return the database connection object
     */
    public Connection getConnection()
    {
        try
        {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            return DriverManager.getConnection(dbHost,dbUser,dbPwd);
        } 
        catch (SQLException e)
        {
            System.out.println("Failed to open a connection to the server");
            e.printStackTrace();
        }
        catch(Exception ie)
        {
            System.out.println("Failed to get a vaild driver instance");
            ie.printStackTrace();
        }
        return null;
    }
}
