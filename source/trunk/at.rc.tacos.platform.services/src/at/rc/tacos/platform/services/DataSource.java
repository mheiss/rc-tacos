package at.rc.tacos.platform.services;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The dbal interface defines methods to interact with the database
 * 
 * @author Michael
 */
public interface DataSource {

	/**
	 * Opens a new connection to the database
	 */
	public void open() throws Exception;

	/**
	 * Returns a new connection to interact with the database
	 * 
	 * @return a database connection
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * Returns the current status of the database connection
	 * 
	 * @return true if the database connection is established
	 */
	public boolean status() throws SQLException;

	/**
	 * Closes the database connection
	 */
	public void shutdown() throws SQLException;

}
