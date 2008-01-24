package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.TestDataSource;

public class LocationDAOMemory implements LocationDAO
{
	// the instance
	private static LocationDAOMemory instance;
	//the object list
	private static List<Location> locationList;
	
	/**
	 * Default class constructor
	 */
	private LocationDAOMemory()
	{
		locationList = new ArrayList<Location>();
		//init the test data
		for(Location location:TestDataSource.getInstance().locationList)
			locationList.add(location);
	}
	
	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static LocationDAOMemory getInstance()
	{
		if(instance == null)
			instance = new LocationDAOMemory();
		return instance;
	}

	@Override
	public int addLocation(Location location) 
	{
		locationList.add(location);
		return locationList.size();
	}

	@Override
	public Location getLocation(int id) 
	{
		return locationList.get(id);
	}

	@Override
	public List<Location> listLocations() 
	{
		return locationList;
	}

	@Override
	public boolean removeLocation(int id) 
	{
		if(locationList.remove(id) != null)
			return true;
		//nothing removed
		return false;
	}

	@Override
	public boolean updateLocation(Location location) 
	{
		int index = locationList.indexOf(location);
		locationList.set(index, location);
		return true;
	}
}
