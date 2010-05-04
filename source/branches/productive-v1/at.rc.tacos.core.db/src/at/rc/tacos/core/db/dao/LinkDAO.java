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

import at.rc.tacos.model.Link;

/**
 * Link DAO Interface
 * 
 * @author Payer Martin
 * @version 1.0
 */
public interface LinkDAO {

	public static final String TABLE_NAME = "link";

	public int addLink(Link link) throws SQLException;

	public List<Link> listLinks() throws SQLException;

	public Link getLinkById(int linkId) throws SQLException;

	public boolean removeLink(int linkId) throws SQLException;

	public boolean updateLink(Link link) throws SQLException;
}
