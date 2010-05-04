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

import at.rc.tacos.model.DayInfoMessage;

public interface DayInfoDAO {

	public static final String TABLE_NAME = "dayinfo";

	/**
	 * Creates a new day info message or updates a existing message in the
	 * database.
	 * 
	 * @param message
	 *            the message to update
	 * @return the a boolean value if update/insert was sucessful
	 */
	public boolean updateDayInfoMessage(DayInfoMessage message) throws SQLException;

	/**
	 * Returns the day info message for a given date. This method will return
	 * <code>null</code> when no day info message is stored for the day.
	 * 
	 * @param date
	 *            the date to get the message
	 * @return the day info message for the day
	 */
	public DayInfoMessage getDayInfoByDate(long date) throws SQLException;
}
