package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.server.db.dao.AddressDAO;
import at.rc.tacos.server.db.dao.factory.DaoFactory;
import at.rc.tacos.server.net.ServerContext;

public class AddressListener extends ServerListenerAdapter
{
	//the database access
	private AddressDAO addressDao = DaoFactory.SQL.createAddressDAO();
	//the logger
	private static Logger logger = Logger.getLogger(AddressListener.class);
	//the user
	private String username = ServerContext.getCurrentInstance().getSession().getUsername();

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException, SQLException 
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
		logger.info("Address records added: "+addList.size()+ " Einträge");
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
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException, SQLException 
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

		//the filter types for the database
		String streetFilter = new String("%");
		String streetNumberFilter = new String("%");
		String cityFilter = new String("%");
		String zipFilter = new String("%");
		

		//get the passed filter values and add some wildcards
		if(queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_STREET) != null)
		{
			streetFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_STREET);
			streetFilter = "%" +streetFilter + "%";
		}
		if(queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_CITY) != null)
		{
			cityFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_CITY);
			cityFilter = "%" +cityFilter + "%";
		}
		if(queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_ZIP) != null)
		{
			zipFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_ZIP);
			zipFilter = "%" + zipFilter + "%";
		}
		if(queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_STREETNUMBER) != null)
		{
			streetNumberFilter = queryFilter.getFilterValue(IFilterTypes.SEARCH_STRING_STREETNUMBER);
			streetNumberFilter = "%" + streetNumberFilter + "%";
		}

		//get the query filter
		final String addressFilter = "Street: "+streetFilter+" | City: "+cityFilter + " | Zip: " +zipFilter;

		addressList = addressDao.getAddressList(streetFilter,streetNumberFilter,cityFilter,zipFilter);
		if(addressList == null)
			throw new DAOException("AddressListener","Failed to list the address records by search string: "+addressFilter);
		list.addAll(addressList);

		//return the list
		return list;
	}
}
