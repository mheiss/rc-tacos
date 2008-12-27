package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>LocationHandler</code> manages the locally chached {@link Location}
 * instances.
 * 
 * @author Michael
 */
public class LocationHandler implements Handler<Location> {

	private List<Location> locationList = Collections.synchronizedList(new ArrayList<Location>());
	private Logger log = LoggerFactory.getLogger(LocationHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Location> message) throws SQLException, ServiceException {
		synchronized (locationList) {
			locationList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Location> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Location> message) throws SQLException, ServiceException {
		synchronized (locationList) {
			// add or update the location
			for (Location location : message.getObjects()) {
				int index = locationList.indexOf(location);
				if (index == -1) {
					locationList.add(location);
				}
				else {
					locationList.set(index, location);
				}
			}
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Location> message) throws SQLException, ServiceException {
		synchronized (locationList) {
			locationList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Location> message) throws SQLException, ServiceException {
		synchronized (locationList) {
			for (Location updateLocation : locationList) {
				if (!locationList.contains(updateLocation)) {
					continue;
				}
				int index = locationList.indexOf(updateLocation);
				locationList.set(index, updateLocation);
			}
		}
	}

	/**
	 * Returns the first <code>Location</code> instance that exactly matches the
	 * string returned by {@link Location#getLocationName()}.
	 * 
	 * @param locationName
	 *            the name of the location to search
	 * @return the matched location or null if nothing found
	 */
	public Location getLocationByName(String locationName) {
		for (Location loc : locationList) {
			if (loc.getLocationName().equalsIgnoreCase(locationName))
				return loc;
		}
		// nothing found
		return null;
	}

	/**
	 * Returns a new array containing the managed <code>Location</code>
	 * instances.
	 * 
	 * @return an array containing the <code>Location</code> instances.
	 */
	@Override
	public Location[] toArray() {
		return locationList.toArray(new Location[locationList.size()]);
	}
}
