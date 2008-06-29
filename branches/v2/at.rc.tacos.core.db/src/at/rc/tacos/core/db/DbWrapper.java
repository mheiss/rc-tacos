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
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DbWrapper extends Plugin 
{
    // The plug-in ID
    public static final String PLUGIN_ID = "at.rc.tacos.core.db";
    
    //the database configuration
    public static final String DB_SETTINGS_BUNDLE_PATH = "at.rc.tacos.core.db.config.db";

    // The shared instance
    private static DbWrapper plugin;
    
    //the params of the config file
    private String dbDriver,dbHost,dbUser,dbPwd;
    private GenericObjectPool connectionPool;

    /**
     * The constructor
     */
    public DbWrapper() { }

    /**
     * Called when the plugin is started
     * @param context lifecyle informations
     * @throws Exception when a error occures during startup
     */
    @Override
    public void start(BundleContext context) throws Exception 
    {
        super.start(context);
        plugin = this;
        //open a connection to the database 
        initDatabaseConnection();
    }

    /**
     * Called when the plugin is stopped
     * @param context lifecyle informations
     * @throws Exception when a error occures during shutdown
     */
    @Override
    public void stop(BundleContext context) throws Exception 
    {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static DbWrapper getDefault() 
    {
        return plugin;
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
    
    
    //HELPER METHODS
    /**
     * Opens a connection to the database and sets up the connection pool
     */
    private void initDatabaseConnection() throws Exception
    {
    	 //load the settings from the file
        dbDriver = ResourceBundle.getBundle(DB_SETTINGS_BUNDLE_PATH).getString("db.driver");
        dbHost = ResourceBundle.getBundle(DB_SETTINGS_BUNDLE_PATH).getString("db.url");
        dbUser = ResourceBundle.getBundle(DB_SETTINGS_BUNDLE_PATH).getString("db.user");
        dbPwd = ResourceBundle.getBundle(DB_SETTINGS_BUNDLE_PATH).getString("db.pw");
        
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
}
