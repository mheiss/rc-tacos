package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.LinkDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Link;
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
    public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException,SQLException
    {
        Link link = (Link)addObject;
        int id = linkDao.addLink(link);
        if(id == -1)
        	throw new DAOException("LinkListener","Failed to add the link " + link);
        link.setId(id);
        logger.info("added by:" +username +";" +link);
        return link;
    }
	
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
				
		if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER)) {
			int linkId = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ID_FILTER));
			final Link link = linkDao.getLinkById(linkId);
			if (link != null) {
				list.add(link);
			}
		} else {
			final List<Link> linkList = linkDao.listLinks();
	    	if(linkList == null)
	    		throw new DAOException("LinkListener","Failed to list the links"); 
	    	list.addAll(linkList);
		}
		return list;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException,SQLException
	{
		Link link = (Link)updateObject;
		if(!linkDao.updateLink(link))
			throw new DAOException("LinkListener","Failed to update the link:"+link);
		logger.info("updated by: " +username +";" +link);
		return link;
	}
	
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
	{
		Link link = (Link)removeObject;
		if(!linkDao.removeLink(link.getId()))
			throw new DAOException("LinkListener","Failed to remove the link:"+link);
		return link;
	}
}
