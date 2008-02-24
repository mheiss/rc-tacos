package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

public class AddressListener extends ServerListenerAdapter
{

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException, SQLException 
	{
		//just forward to the client
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
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException, SQLException 
	{
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
