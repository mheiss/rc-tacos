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

import at.rc.tacos.model.SickPerson;

public interface SickPersonDAO {

	public static final String TABLE_NAME = "sickperson";

	/**
	 * Adds a new sick person into the database and returns the generated id
	 * 
	 * @param sickPerson
	 *            the sickPerson to add
	 * @return the id of the new sick person
	 */
	public int addSickPerson(SickPerson sickPerson) throws SQLException;

	/**
	 * Updates the given sick person in the database
	 * 
	 * @param sickPerson
	 *            the sickPerson to update
	 * @return true if the update was successfully
	 */
	public boolean updateSickPerson(SickPerson sickPerson) throws SQLException;

	/**
	 * Removes the sichPerson from the database
	 * 
	 * @param id
	 *            the sickPerson to remove
	 * @return true if the removal was successfully
	 */
	public boolean removeSickPerson(int id) throws SQLException;

	/**
	 * Returns a list of all sick persons in the database if the last name
	 * contains the lastNameSubString.
	 * 
	 * @return the lastNameSubString sickPersonList
	 */
	public List<SickPerson> getSickPersonList(String lastNameSubString) throws SQLException;

	/**
	 * Returns a specific id identified by the sick person id
	 * 
	 * @param id
	 *            the id of the sick person to get
	 * @return the sick person or null if no sick person with this id was found
	 */
	public SickPerson getSickPerson(int id) throws SQLException;
}
