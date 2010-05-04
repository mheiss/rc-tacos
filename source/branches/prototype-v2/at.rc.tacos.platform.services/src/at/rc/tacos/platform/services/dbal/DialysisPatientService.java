package at.rc.tacos.platform.services.dbal;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.DialysisPatient;

public interface DialysisPatientService {

	public static final String TABLE_NAME = "dialysis";
	public static final String TABLE_DEPENDENT = "dialysis_transport";

	/**
	 * Adds a new dialyse patient to the database and returns the unique id.
	 * 
	 * @param patient
	 *            the patient to add
	 * @return the unique id of the patient in the database
	 */
	public int addDialysisPatient(DialysisPatient patient) throws SQLException;

	/**
	 * Updates a dialyse patient in the database
	 * 
	 * @param patient
	 *            the patient to update
	 * @return true if the update was successfully otherwise false
	 */
	public boolean updateDialysisPatient(DialysisPatient patient) throws SQLException;

	/**
	 * Removes the dialyse patient from the database.
	 * 
	 * @param id
	 *            the id of the patient to remove
	 * @return true if the deletion was successfully.
	 */
	public boolean removeDialysisPatient(int id) throws SQLException;

	/**
	 * Returns the dialysis patient accociated with the given id.
	 * 
	 * @param id
	 *            the id to get the patient from
	 * @return the queried patient
	 */
	public DialysisPatient getDialysisPatientById(int id) throws SQLException;

	/**
	 * Returns a list of all stored dialysis patients in the database
	 * 
	 * @return the complete list of all dialysis patients
	 */
	public List<DialysisPatient> listDialysisPatient() throws SQLException;
}
