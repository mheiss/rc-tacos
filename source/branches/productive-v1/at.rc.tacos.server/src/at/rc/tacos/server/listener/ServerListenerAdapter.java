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
package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IServerListener;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

/**
 * An abstract adapter class for receiving client requests. The methods in this
 * class are empty. This class exists as convenience for creating listener
 * objects.
 * 
 * @author Michael
 */
public abstract class ServerListenerAdapter implements IServerListener {

	/**
	 * Handles login request
	 */
	@Override
	public AbstractMessage handleLoginRequest(AbstractMessage loginObject) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles logout request
	 */
	@Override
	public AbstractMessage handleLogoutRequest(AbstractMessage logoutObject) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles add requests
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles add request for multiple items
	 */
	@Override
	public List<AbstractMessage> handleAddAllRequest(List<AbstractMessage> addList) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles listing requests
	 */
	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles remove requests
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException {
		return null;
	}

	/**
	 * Handles update requests
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException {
		return null;
	}
}
