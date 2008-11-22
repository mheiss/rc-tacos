package at.rc.taocs.server.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.exception.ConfigurationException;

/**
 * The data source provides access to the connection pool.
 */
public class DataSourceImpl implements DataSource {

	// logger for errors
	private Logger log = LoggerFactory.getLogger(DataSourceImpl.class);

	// the server pool
	private GenericObjectPool connectionPool;
	private boolean isOpen;

	/**
	 * Default class constructor
	 */
	public DataSourceImpl() {
	}

	/**
	 * Returns the connection to the database. When the connection cannot be
	 * established then the method will return null
	 * 
	 * @return the database connection object
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if (!isOpen)
			throw new IllegalStateException("The data source must be opened to get a connection");
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:tacos-pool");
	}

	/**
	 * Creates a new pooled database connection and registers it with the
	 * {@link DriverManager}. The connection pool is registered with the name
	 * <code>tacos-pool</code>.
	 */
	@Override
	public void open() throws Exception {
		log.info("Initialize database connection pool");
		// load the settings
		ResourceBundle bundle = ResourceBundle.getBundle("at.rc.tacos.server.dbal.config.db");
		if (bundle == null) {
			throw new ConfigurationException("Failed to load the database configuration", null);
		}

		// load the settings from the file
		String dbDriver = bundle.getString("db.driver");
		String dbHost = bundle.getString("db.url");
		String dbUser = bundle.getString("db.user");
		String dbPwd = bundle.getString("db.pwd");

		// load the mysql driver
		Class.forName(dbDriver);

		// create and initialize the connection pool
		connectionPool = new GenericObjectPool(null);
		connectionPool.setMaxIdle(15); // Maximum idle connections.
		connectionPool.setMinIdle(10); // Minimum idle connections.
		connectionPool.setMinEvictableIdleTimeMillis(30000);
		connectionPool.setTimeBetweenEvictionRunsMillis(10000);

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbHost, dbUser, dbPwd);
		new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
		PoolingDriver driver = new PoolingDriver();
		driver.registerPool("tacos-pool", connectionPool);

		// add some connections
		for (int i = 0; i < 15; i++)
			connectionPool.addObject();

		isOpen = true;
	}

	/**
	 * Closes the database connection and cleans up the connection pool
	 */
	@Override
	public void shutdown() {
		try {
			connectionPool.clear();
			connectionPool.close();
			connectionPool = null;
			isOpen = false;
		}
		catch (Exception e) {
			log.error("Error while trying to close the connection", e);
		}
	}

	/**
	 * Returns the current status of the database connection
	 * 
	 * @return true if a connection to the database is established
	 */
	@Override
	public boolean status() throws SQLException {
		return getConnection() != null;
	}
}
