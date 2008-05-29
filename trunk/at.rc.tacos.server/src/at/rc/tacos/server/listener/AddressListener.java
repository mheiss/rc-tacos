package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.AddressDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

public class AddressListener extends ServerListenerAdapter
{
	//the database access
	private AddressDAO addressDao = DaoFactory.SQL.createAddressDAO();
	//the logger
	private static Logger logger = Logger.getLogger(AddressListener.class);
	
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException 
	{
		Address newAddress = (Address)addObject;
		//add to the database
		int id = addressDao.addAddress(newAddress);
		if(id == -1)
			throw new DAOException("AddressListener","Failed to add the address record: "+newAddress);
		
		//set the id
		newAddress.setAddressId(id);
		logger.info("added by:" +username +";" +addObject);
		return newAddress;
	}
	
	@Override
	public List<AbstractMessage> handleAddAllRequest(List<AbstractMessage> addList) throws DAOException,SQLException
	{
		//loop and add all address recors
		for(AbstractMessage abstractAddress: addList)
		{
			Address newAddress = (Address)abstractAddress;
			//add to the database
			int id = addressDao.addAddress(newAddress);
			if(id == -1)
				throw new DAOException("AddressListener","Failed to add the address record: "+newAddress);
			newAddress.setAddressId(id);
		}
		return addList;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		Address address = (Address)removeObject;
		if(!addressDao.removeAddress(address.getAddressId()))
			throw new DAOException("AddressListener","Failed to remove the address record: "+address);
		logger.info("removed: " + address);
		//just forward to the client
		return address;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException 
	{
		Address address = (Address)updateObject;
		if(!addressDao.updateAddress(address))
			throw new DAOException("AddressListener","Failed to update the address record: "+address);
		logger.info("updated by: " +username +";" +address);
		//just forward to the client
		return updateObject;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<Address> addressList;
		
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all address records is denied.");
			throw new DAOException("AddressListener","Listing of all address records is denied");
		} 
		else if(queryFilter.containsFilterType(IFilterTypes.SEARCH_STRING))
		{
			//get the query filter
			final String addressFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING);
		
			addressList = addressDao.getAddressList(addressFilter);
			if(addressList == null)
				throw new DAOException("AddressListener","Failed to list the address records by search string: "+addressFilter);
			list.addAll(addressList);
		} 
		
		//return the list
		return list;
	}
}
