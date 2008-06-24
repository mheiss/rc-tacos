package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.LinkDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

/**
 * Link Listener
 * @author Payer Martin
 * @version 1.0
 */
public class LinkListener extends ServerListenerAdapter {

	//The DAO classes
	private LinkDAO linkDao = DaoFactory.SQL.createLinkDAO();
	//the logger
	private static Logger logger = Logger.getLogger(DayInfoListener.class);
	
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//listing by date
		if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
		}
		return list;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException,SQLException
	{
		return null;
	}
}
