package at.rc.tacos.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The database source to connect to a mysql database
 * @author Michael
 */
public class DataSource
{
    public static final String DB_SETTINGS_BUNDLE_PATH = "at.rc.tacos.core.db.config.db";
    
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
        //load the settings from the file
        String dbDriver = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.driver");
        String dbHost = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.url");
        String dbUser = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.user");
        String dbPwd = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.pw");
        
        try
        {
            Class.forName(dbDriver).newInstance();
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
