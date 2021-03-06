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
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

public class CompetenceListener extends ServerListenerAdapter {

	// the DAO
	private CompetenceDAO compDao = DaoFactory.SQL.createCompetenceDAO();
	// the logger
	private static Logger logger = Logger.getLogger(CompetenceListener.class);

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException {
		Competence comp = (Competence) addObject;
		int id = compDao.addCompetence(comp);
		// check for error while adding
		if (id == -1)
			throw new DAOException("CompetenceListener", "Failed to add the competence " + comp + " to the database");
		comp.setId(id);
		logger.info("added by:" + username + ";" + comp);
		return comp;
	}

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException {
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<Competence> competenceList = compDao.listCompetences();

		// check the result
		if (competenceList == null)
			throw new DAOException("CompetenceListener", "Failed to list the competences");

		// add all returned competences
		for (AbstractMessage ab : competenceList)
			list.add(ab);

		return list;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException {
		Competence comp = (Competence) removeObject;
		if (!compDao.removeCompetence(comp.getId()))
			throw new DAOException("CompetenceListener", "Failed to remove the competence " + comp);
		return comp;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException {
		Competence comp = (Competence) updateObject;
		if (!compDao.updateCompetence(comp))
			throw new DAOException("CompetenceListener", "Failed to update the competence: " + comp);
		logger.info("updated by: " + username + ";" + comp);
		return comp;
	}
}
