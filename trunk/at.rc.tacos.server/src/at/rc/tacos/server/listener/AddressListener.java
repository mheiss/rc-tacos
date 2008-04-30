package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

public class AddressListener extends ServerListenerAdapter
{
	//the logger
	private static Logger logger = Logger.getLogger(AddressListener.class);
	
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException 
	{
		//just forward to the client
		logger.info("added by:" +username +";" +addObject);
		return addObject;
	}
	
	@Override
	public List<AbstractMessage> handleAddAllRequest(List<AbstractMessage> addList) throws DAOException,SQLException
	{
		//just forward
		return addList;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		//just forward to the clients
		return removeObject;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException 
	{
		logger.info("updated by: " +username +";" +updateObject);
		//just forward to the client
		return updateObject;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		//just return a cleared list
		return new ArrayList<AbstractMessage>();
	}
}
