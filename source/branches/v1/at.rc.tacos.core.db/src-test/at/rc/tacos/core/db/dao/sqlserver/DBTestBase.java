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
package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import at.rc.tacos.core.db.DataSource;

/**
 * Base class to privode common methods for the test classes
 * 
 * @author Michael
 */
public class DBTestBase {

	// The data source to get the connection
	private final DataSource source = DataSource.getInstance();

	protected void deleteTable(String table) throws SQLException {
		Connection connection = source.getConnection();
		final String SQL_DELETE = "DELETE FROM " + table;
		try {
			PreparedStatement delteStatement = connection.prepareStatement(SQL_DELETE);
			delteStatement.executeUpdate();
		}
		finally {
			connection.close();
		}
	}
}
