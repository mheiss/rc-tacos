package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;

public class LocationListener extends ServerListenerAdapter
{
	private LocationDAO locationDao = DaoFactory.MYSQL.createLocationDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
    {
        Location location = (Location)addObject;
        //add the location
        int id = locationDao.addLocation(location);
        if(id == -1)
        	throw new DAOException("LocationDAO","Failed to add the location: "+location);
        location.setId(id);
        //set the id
        return location;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	List<Location> locationList = locationDao.listLocations();
    	if(locationList == null)
    		throw new DAOException("LocationDAO","Failed to list the locations");
    	list.addAll(locationList);
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
    {
    	Location location = (Location)removeObject;
    	if(!locationDao.removeLocation(location.getId()))
    		throw new DAOException("LocationDAO","Failed to remove the location "+location);
    	return location;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
    {
    	Location location = (Location)updateObject;
    	if(!locationDao.updateLocation(location))
    		throw new DAOException("LocationDAO","Failed to update the location "+location);
    	return location;
    }
}