package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Disease;

public interface DiseaseDAO
{
    public static final String TABLE_NAME = "disease";
    
	/**
	 * Adds a new disease into the database and returns the generated id
	 * @param disease the diasese to add
	 * @return the id of the new disease
	 */
	public int addDisease(Disease disease);
	
	/**
	 * Updates the given disease in the database
	 * @param disease the disease to update
	 * @return true if the update was successfully
	 */
	public boolean updateDisease(Disease disease);
	
	/**
	 * Removes the disease from the database
	 * @param id the disease to remove
	 * @return true if the removal was successfully
	 */
	public boolean removeDisease(int id);
	
	/**
	 * Returns a list of all diseases in the database.
	 * @return the disease list
	 */
	public List<Disease> getDiseaseList();
}
