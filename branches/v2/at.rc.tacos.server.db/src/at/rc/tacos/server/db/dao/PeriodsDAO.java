package at.rc.tacos.server.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Period;

public interface PeriodsDAO
{
    public static final String TABLE_NAME = "periods";
    
	/**
	 * Adds a new period into the database and returns the generated id
	 * @param period the period to add
	 * @return the id of the new period
	 */
	public int addPeriod(Period period) throws SQLException;
	
	/**
	 * Updates the given period in the database
	 * @param period the period to update
	 * @return true if the update was successfully
	 */
	public boolean updatePeriod(Period period) throws SQLException;
	
	/**
	 * Removes the period from the database
	 * @param id the period to remove
	 * @return true if the removal was successfully
	 */
	public boolean removePeriod(int id) throws SQLException;
	
	/**
	 * Returns a list of all periodes in the database if one of the fields contains the given string
	 * @return the period list with matched entries
	 */
	public List<Period> getPeriodListByServiceTypeCompetence(String serviceTypeCompetence) throws SQLException;
	
	/**
	 * Returns a specific id identified by the period id
	 * @param id the id of the period to get
	 * @return the period or null if no period with this id was found
	 */
	public Period getPeriod(int id) throws SQLException;
}
