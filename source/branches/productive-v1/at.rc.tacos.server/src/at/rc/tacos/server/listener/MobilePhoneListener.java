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
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;

/**
 * This class will be notified uppon mobile phone changes
 * 
 * @author Michael
 */
public class MobilePhoneListener extends ServerListenerAdapter {

	private MobilePhoneDAO mobilePhoneDao = DaoFactory.SQL.createMobilePhoneDAO();
	// the logger
	private static Logger logger = Logger.getLogger(MobilePhoneListener.class);

	/**
	 * Add a mobile phone
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException {
		MobilePhoneDetail phone = (MobilePhoneDetail) addObject;
		int id = mobilePhoneDao.addMobilePhone(phone);
		if (id == -1)
			throw new DAOException("MobilePhoneListener", "Failed to add the mobile phone:" + phone);
		phone.setId(id);
		logger.info("added by:" + username + ";" + addObject);
		return addObject;
	}

	/**
	 * Listing of all mobile phones
	 */
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException {
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<MobilePhoneDetail> phoneList = mobilePhoneDao.listMobilePhones();
		if (phoneList == null)
			throw new DAOException("MobilePhoneListener", "Failed to list the mobile phones");
		list.addAll(phoneList);
		return list;
	}

	/**
	 * Remove a mobile phone
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException {
		MobilePhoneDetail phone = (MobilePhoneDetail) removeObject;
		if (!mobilePhoneDao.removeMobilePhone(phone.getId()))
			throw new DAOException("MobilePhoneListener", "Failed to remove the mobile phone:" + phone);
		return phone;
	}

	/**
	 * Update a mobile phone
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException {
		MobilePhoneDetail phone = (MobilePhoneDetail) updateObject;
		if (!mobilePhoneDao.updateMobilePhone(phone))
			throw new DAOException("MobilePhoneListener", "Failed to update the mobile phone:" + phone);
		logger.info("updated by: " + username + ";" + phone);
		return phone;
	}
}
