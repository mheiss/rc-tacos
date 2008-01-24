package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;

public class LocationListener extends ServerListenerAdapter
{
	private LocationDAO locationDao = DaoFactory.TEST.createLocationDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Location location = (Location)addObject;
        //add the location
        int id = locationDao.addLocation(location);
        location.setId(id);
        //set the id
        return location;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	list.addAll(locationDao.listLocations());
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
    	Location location = (Location)removeObject;
    	locationDao.removeLocation(location.getId());
    	return location;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
    	Location location = (Location)updateObject;
    	locationDao.updateLocation(location);
    	return location;
    }
}