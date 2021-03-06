/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.DialysisPatient;

public interface DialysisPatientDAO {

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
