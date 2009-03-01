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

import at.rc.tacos.model.Disease;

public interface DiseaseDAO {

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
