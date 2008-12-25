package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LocationHandler implements Handler<Location> {

	@Service(clazz = LocationService.class)
	private LocationService locationService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Location> message) throws ServiceException, SQLException {
		// loop and try to add each location
		List<Location> locationList = message.getObjects();
		for (Location location : locationList) {
			// add the location
			int id = locationService.addLocation(location);
			if (id == -1)
				throw new ServiceException("Failed to add the location: " + location);
			// set the id
			location.setId(id);
		}
		// brodcast the updated locations
		session.writeResponseBrodcast(message, locationList);
	}

	@Override
	public void get(MessageIoSession session, Message<Location> message) throws ServiceException, SQLException {
		// query all location in the database
		List<Location> locationList = locationService.listLocations();
		if (locationList == null)
			throw new ServiceException("Failed to list the locations");

		// check for locks
		for (Location location : locationList) {
			if (!lockableService.containsLock(location)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(location);
			location.setLocked(lockable.isLocked());
			location.setLockedBy(lockable.getLockedBy());
		}

		// send the locations back
		session.writeResponse(message, locationList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Location> message) throws ServiceException, SQLException {
		// loop and try to remove each location
		List<Location> locationList = message.getObjects();
		for (Location location : locationList) {
			if (!locationService.removeLocation(location.getId()))
				throw new ServiceException("Failed to remove the location " + location);
			// remove the locks
			lockableService.removeLock(location);
		}
		// brodcast the removed locations
		session.writeResponseBrodcast(message, locationList);
	}

	@Override
	public void update(MessageIoSession session, Message<Location> message) throws ServiceException, SQLException {
		// loop and try to remove each location
		List<Location> locationList = message.getObjects();
		for (Location location : locationList) {
			if (!locationService.updateLocation(location))
				throw new ServiceException("Failed to update the location " + location);
			// update the locks
			lockableService.updateLock(location);
		}
		// brodcast the updated locations
		session.writeResponseBrodcast(message, locationList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Location> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}
}
