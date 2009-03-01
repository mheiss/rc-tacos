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

import at.rc.tacos.model.CallerDetail;

public interface CallerDAO {

	public static final String TABLE_NAME = "caller";

	/**
	 * Adds a new caller to the list of callers.
	 * 
	 * @param notifierDetail
	 *            the details of the caller
	 * @return the generated caller id
	 */
	public int addCaller(CallerDetail notifierDetail) throws SQLException;

	/**
	 * Updates a caller object in the database
	 * 
	 * @param notifierDetail
	 *            the details to update
	 * @return true if the update was successfull
	 */
	public boolean updateCaller(CallerDetail notifierDetail) throws SQLException;

	/**
	 * Returns the caller identfied by the given id.
	 * 
	 * @param id
	 *            the id of the caller to get
	 * @return the caller or null if no caller was found.
	 */
	public CallerDetail getCallerByID(int id) throws SQLException;
}
