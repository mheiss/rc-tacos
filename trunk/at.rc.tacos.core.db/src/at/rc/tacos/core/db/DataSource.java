package at.rc.tacos.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * The database source to connect to a mysql database
 * @author Michael
 */
public class DataSource
{
    public static final String DB_SETTINGS_BUNDLE_PATH = "at.rc.tacos.core.db.config.db";
    //the shared instance
    private static DataSource instance;
    //the params of the config file
    private String dbDriver,dbHost,dbUser,dbPwd;
    private GenericObjectPool connectionPool;
    
    /**
     * Default class constructor to load the settings from the file
     */
    private DataSource()
    {
        try
        {
            //load the settings from the file
            dbDriver = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.driver");
            dbHost = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.url");
            dbUser = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.user");
            dbPwd = ResourceBundle.getBundle(DataSource.DB_SETTINGS_BUNDLE_PATH).getString("db.pw");
            
            //load the mysql driver
            Class.forName(dbDriver);
            
            //create and initialize the connection pool
            connectionPool = new GenericObjectPool(null);
            connectionPool.setMaxIdle(15);	 						// Maximum idle connections.
            connectionPool.setMinIdle(10);							// Minimum idle connections.
            connectionPool.setMinEvictableIdleTimeMillis(30000); 	//Evictor runs every 30 secs.
            connectionPool.setTimeBetweenEvictionRunsMillis(10000);
            connectionPool.setTestOnBorrow(true);					// Check if the connection is still valid.
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbHost, dbUser, dbPwd);
            PoolableConnectionFactory factory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
            factory.setValidationQuery("select 1");
            PoolingDriver driver = new PoolingDriver();
            driver.registerPool("tacos-pool",connectionPool);       
            
            //add some connections
            for(int i = 0; i < 15; i++) 
                connectionPool.addObject();
        }
        catch(Exception ex)
        {
            System.out.println("Failed to initialize the connection pool");
        }
    }
    
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
        	//System.out.println("Active: " + connectionPool.getNumActive() + ", Idle: " + connectionPool.getNumIdle());
        	Connection connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:tacos-pool");
        	return connection;
            
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