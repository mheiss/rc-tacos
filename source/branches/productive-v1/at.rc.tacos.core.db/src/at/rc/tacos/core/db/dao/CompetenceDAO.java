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

import at.rc.tacos.model.Competence;

public interface CompetenceDAO {

	public static final String TABLE_NAME = "competences";
	public static final String TABLE_DEPENDENT_NAME = "staffmember_competence";

	/**
	 * Adds a new competence to the competence list
	 * 
	 * @param competence
	 *            the competence to add
	 * @return the generated id
	 */
	public int addCompetence(Competence competence) throws SQLException;

	/**
	 * Updates the competence in the database
	 * 
	 * @param competence
	 *            the competence to update
	 * @return true if the update was successfully
	 */
	public boolean updateCompetence(Competence competence) throws SQLException;

	/**
	 * Removes the competence out of the database
	 * 
	 * @param id
	 *            the id of the competence to remove
	 * @return true if the remove was successfully
	 */
	public boolean removeCompetence(int id) throws SQLException;

	/**
	 * Returns the competence identified by the given id
	 * 
	 * @param id
	 *            the id of the competence to get
	 * @return the requested competence or null if no competence was found
	 */
	public Competence getCompetenceById(int id) throws SQLException;

	/**
	 * Lists all competences from the database
	 * 
	 * @return the list of competences
	 */
	public List<Competence> listCompetences() throws SQLException;

	/**
	 * Lists all competences of a staffmember.
	 * 
	 * @param id
	 *            the id of the staff member to get the competences
	 * @result competencelist of a staffmember
	 */
	public List<Competence> listCompetencesOfStaffMember(int id) throws SQLException;
}
