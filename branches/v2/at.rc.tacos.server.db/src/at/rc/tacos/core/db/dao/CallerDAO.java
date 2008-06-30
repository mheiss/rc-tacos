package at.rc.tacos.core.db.dao;

import java.sql.SQLException;

import at.rc.tacos.model.CallerDetail;

public interface CallerDAO 
{
    public static final String TABLE_NAME = "caller";
    
	/**
	 * Adds a new caller to the list of callers.
	 * @param notifierDetail the details of the caller
	 * @return the generated caller id
	 */
	public int addCaller(CallerDetail notifierDetail) throws SQLException;
	
	/**
	 * Updates a caller object in the database
	 * @param notifierDetail the details to update
	 * @return true if the update was successfull
	 */
	public boolean updateCaller(CallerDetail notifierDetail) throws SQLException;
	
	/**
	 * Returns the caller identfied by the given id.
	 * @param id the id of the caller to get
	 * @return the caller or null if no caller was found.
	 */
	public CallerDetail getCallerByID(int id) throws SQLException;
}
