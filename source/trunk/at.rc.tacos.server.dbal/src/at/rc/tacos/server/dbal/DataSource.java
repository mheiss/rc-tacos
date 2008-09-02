package at.rc.tacos.server.dbal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

import at.rc.tacos.platform.log.LogFactory;

/**
 * The data source provides access to the connection pool
 */
public class DataSource
{
	// The shared instance
	private static DataSource instance;
	
	//the server pool 
	private boolean isConnected;
	private GenericObjectPool connectionPool;
	
	/**
	 *  Default private constructor
	 */
	private DataSource() { 
		//prevent instantiation
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static DataSource getInstance() 
	{
		if(instance == null)
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
			Connection connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:tacos-pool");
			return connection; 
		} 
		catch (SQLException sqle)
		{
			LogFactory.error(this,"SQL-Error while trying to get the connection: "+sqle.getMessage());
		}
		catch(Exception e)
		{
			LogFactory.error(this,"Failed to get a vaild driver instance: "+e.getMessage());
		}
		return null;
	}

	/**
	 * Opens a connection to the database and sets up the connection pool
	 */
	public void initDatabaseConnection(String dbDriver,String dbHost,String dbUser,String dbPwd)
	{
		try
		{	
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LogFactory.error(this,"SQL-Error while trying to get the connection: "+e.getMessage());
			isConnected = false;
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LogFactory.error(this,"SQL-Error while trying to close the connection: "+e.getMessage());
		}
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
