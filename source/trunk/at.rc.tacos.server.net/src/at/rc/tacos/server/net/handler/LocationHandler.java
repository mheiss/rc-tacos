package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class LocationHandler implements INetHandler<Location> {

	@Service(clazz = LocationService.class)
	private LocationService locationService;
	
	@Override
	public Location add(Location model) throws ServiceException, SQLException {
		// add the location
		int id = locationService.addLocation(model);
		if (id == -1)
			throw new ServiceException("Failed to add the location: " + model);
		// set the id
		model.setId(id);
		return model;
	}

	@Override
	public List<Location> execute(String command, List<Location> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Location> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Location> locationList = locationService.listLocations();
		if (locationList == null)
			throw new ServiceException("Failed to list the locations");
		return locationList;
	}

	@Override
	public Location remove(Location model) throws ServiceException, SQLException {
		if (!locationService.removeLocation(model.getId()))
			throw new ServiceException("Failed to remove the location " + model);
		return model;
	}

	@Override
	public Location update(Location model) throws ServiceException, SQLException {
		if (!locationService.updateLocation(model))
			throw new ServiceException("Failed to update the location " + model);
		return model;
	}

}
