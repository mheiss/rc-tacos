package at.rc.tacos.core.db.dao;

import at.rc.tacos.model.CallerDetail;

public interface CallerDAO 
{
	/**
	 * Adds a new caller to the list of callers.
	 * @param notifierDetail the details of the caller
	 * @return the generated caller id
	 */
	public int addCaller(CallerDetail notifierDetail);
	
	/**
	 * Updates a caller object in the database
	 * @param notifierDetail the details to update
	 * @return true if the update was successfull
	 */
	public boolean updateCaller(CallerDetail notifierDetail);
	
	/**
	 * Returns the caller identfied by the given id.
	 * @param id the id of the caller to get
	 * @return the caller or null if no caller was found.
	 */
	public CallerDetail getCallerByID(int id);
}
