package at.rc.tacos.server.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Helo;

public interface HeloDAO 
{
	public static final String TABLE_NAME = "SERVER_STATUS";
	
	/**
	 * Queries and returns the list of currently online servers
	 */
	public List<Helo> getServerList() throws SQLException, DAOException;
	
	/**
	 * Adds a new server to the list of online servers
	 */
	public int addServer(Helo helo) throws SQLException, DAOException;
	
	/**
	 * Removes the server from the list of servers
	 */
	public boolean removeServer(int id) throws SQLException, DAOException;
	
	/**
	 * Sets the PRIMARY flag for this id
	 */
	public boolean setAsPrimary(int id) throws SQLException, DAOException;
}
