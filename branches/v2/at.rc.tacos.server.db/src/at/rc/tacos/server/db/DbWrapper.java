package at.rc.tacos.server.db;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DbWrapper extends Plugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.db";

	//the database configuration
	public static final String DB_SETTINGS_BUNDLE_PATH = "at.rc.tacos.server.db.config.db";

	// The shared instance
	private static DbWrapper plugin;

	//the params of the config file
	private String dbDriver,dbHost,dbUser,dbPwd;
	
	//the server pool 
	private boolean isConnected;
	private GenericObjectPool connectionPool;

	//the listeners
	private ArrayList<PropertyChangeListener> databaseListeners = new ArrayList<PropertyChangeListener>();
	
	//the fired properties
	public final static String DB_CONNECTION_OPENED = "dbConnectionOpened";
	public final static String DB_CONNECTION_CLOSED = "dbConnectionClosed";
	public final static String DB_CONNECTION_ERROR = "dbConnectionError";
	
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
		catch (SQLException sqle)
		{
			DbWrapper.log("SQL-Error while trying to get the connection: "+sqle.getMessage(), Status.ERROR,sqle.getCause());
			firePropertyChangeEvent(DB_CONNECTION_ERROR, null, sqle);
		}
		catch(Exception e)
		{
			DbWrapper.log("Failed to get a vaild driver instance: "+e.getMessage(),Status.ERROR,e.getCause());
			firePropertyChangeEvent(DB_CONNECTION_ERROR, null, e);
		}
		return null;
	}

	/**
	 * Opens a connection to the database and sets up the connection pool
	 */
	public void initDatabaseConnection()
	{
		try
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
			connectionPool.setMaxIdle(15);	 							// Maximum idle connections.
			connectionPool.setMinIdle(10);								// Minimum idle connections.
			connectionPool.setMinEvictableIdleTimeMillis(30000); 		//Evictor runs every 30 secs.
			connectionPool.setTimeBetweenEvictionRunsMillis(10000);
			connectionPool.setTestOnBorrow(true);						// Check if the connection is still valid.
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbHost, dbUser, dbPwd);
			PoolableConnectionFactory factory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
			factory.setValidationQuery("select 1");
			PoolingDriver driver = new PoolingDriver();
			driver.registerPool("tacos-pool",connectionPool);       
	
			//add some connections
			for(int i = 0; i < 15; i++) 
				connectionPool.addObject();
			
			//inform the listeners
			isConnected = true;
			firePropertyChangeEvent(DB_CONNECTION_OPENED, null, connectionPool.getNumIdle());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			DbWrapper.log("SQL-Error while trying to get the connection: "+e.getMessage(), Status.ERROR,e.getCause());
			isConnected = false;
			firePropertyChangeEvent(DB_CONNECTION_CLOSED, null, e);
		}
	}

	/**
	 * Closes the database connection and cleans up the connection pool
	 */
	public void closeDatabaseConnection()
	{
		try
		{
			//clear and shutdown the pool
			connectionPool.clear();
			connectionPool.close();
			//notify the listeners
			isConnected = false;
			firePropertyChangeEvent(DB_CONNECTION_CLOSED, null, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			DbWrapper.log("SQL-Error while trying to close the connection: "+e.getMessage(), Status.ERROR,e.getCause());
			firePropertyChangeEvent(DB_CONNECTION_ERROR, null, e);
		}
	}
	
	/**
	 * Logs the error with the build in log
	 */
	public static void log(String message,int severity,Throwable cause)
	{
		DbWrapper.getDefault().getLog().log(new Status(severity,PLUGIN_ID,message,cause));
	}
	
	// CHANGE LISTENERS
	/**
	 * Informs all the listeners that the property has changed
	 * @param propertyName the name of the property that has changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	private void firePropertyChangeEvent(String event,Object oldValue,Object newValue)
	{
		//iterate over the listeners and inform them
		ListIterator<PropertyChangeListener> listIter = databaseListeners.listIterator();
		while(listIter.hasNext())
		{
			PropertyChangeListener element = (PropertyChangeListener)listIter.next();
			element.propertyChange(new PropertyChangeEvent(this,event,oldValue,newValue));
		}
	}
	
	/**
	 * A public method that allows property to registerproperty change listeners
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) 
	{
		if(!databaseListeners.contains(listener))
			databaseListeners.add(listener);
	}

	/**
	 * A public method that allows the listeners to the removed
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
		databaseListeners.remove(listener);
	}
	
	//GETTERS AND SETTERS
	/**
	 * Returns true if a connection to the database is opened, otherwise false
	 */
	public boolean isConnected() 
	{
		return isConnected;
	}
}
