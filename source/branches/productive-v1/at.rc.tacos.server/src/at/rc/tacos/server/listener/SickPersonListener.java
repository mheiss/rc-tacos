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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.SickPersonDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SickPerson;

public class SickPersonListener extends ServerListenerAdapter {

	private SickPersonDAO personDao = DaoFactory.SQL.createSickPersonDAO();
	// the logger
	private static Logger logger = Logger.getLogger(SickPersonListener.class);

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException {
		SickPerson person = (SickPerson) addObject;
		// add the location
		int id = personDao.addSickPerson(person);
		if (id == -1)
			throw new DAOException("SickPersonListener", "Failed to add the sick person: " + person);
		// set the id
		person.setSickPersonId(id);
		logger.info("added by:" + username + ";" + person);
		return person;
	}

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException {
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<SickPerson> personList;

		// if there is no filter -> request all
		if (queryFilter == null || queryFilter.getFilterList().isEmpty()) {
			System.out.println("WARNING: Listing of all sick persons is denied.");
			throw new DAOException("SickPersonListener", "Listing of all sick persons is denied");
		}
		else if (queryFilter.containsFilterType(IFilterTypes.SEARCH_STRING)) {
			// get the query filter
			final String lastNameFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING);

			personList = personDao.getSickPersonList(lastNameFilter);
			if (personList == null) {
				throw new DAOException("SickPersonListener", "Failed to list the sick persons by lastname: " + lastNameFilter);
			}
			list.addAll(personList);
		}

		// return the list
		return list;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException {
		SickPerson person = (SickPerson) removeObject;
		if (!personDao.removeSickPerson(person.getSickPersonId()))
			throw new DAOException("SickPersonListener", "Failed to remove the sick person " + person);
		logger.info("removed " + person);
		return person;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException {
		SickPerson person = (SickPerson) updateObject;
		if (!personDao.updateSickPerson(person))
			throw new DAOException("SickPersonListener", "Failed to update the sick person " + person);
		logger.info("updated by: " + username + ";" + person);
		return person;
	}
}
