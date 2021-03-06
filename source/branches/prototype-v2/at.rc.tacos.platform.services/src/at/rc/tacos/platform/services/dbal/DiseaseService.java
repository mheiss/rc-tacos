package at.rc.tacos.platform.services.dbal;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Disease;

public interface DiseaseService {

	public static final String TABLE_NAME = "disease";

	/**
	 * Adds a new disease into the database and returns the generated id
	 * 
	 * @param disease
	 *            the diasese to add
	 * @return the id of the new disease
	 */
	public int addDisease(Disease disease) throws SQLException;

	/**
	 * Updates the given disease in the database
	 * 
	 * @param disease
	 *            the disease to update
	 * @return true if the update was successfully
	 */
	public boolean updateDisease(Disease disease) throws SQLException;

	/**
	 * Removes the disease from the database
	 * 
	 * @param id
	 *            the disease to remove
	 * @return true if the removal was successfully
	 */
	public boolean removeDisease(int id) throws SQLException;

	/**
	 * Returns a list of all diseases in the database.
	 * 
	 * @return the disease list
	 */
	public List<Disease> getDiseaseList() throws SQLException;
}
