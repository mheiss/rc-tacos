package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The address handler manages the localy cached address objects
 * 
 * @author Michael
 */
public class AddressHandler implements Handler<Address> {

	// the chached objects
	private List<Address> addressList = Collections.synchronizedList(new LinkedList<Address>());
	private Logger log = LoggerFactory.getLogger(AddressHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		synchronized (addressList) {
			addressList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		synchronized (addressList) {
			addressList.clear();
			addressList.addAll(message.getObjects());
		}

	}

	@Override
	public void remove(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		synchronized (addressList) {
			addressList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Address> message) throws SQLException, ServiceException {
		synchronized (addressList) {
			for (Address newAddress : message.getObjects()) {
				if (!addressList.contains(newAddress))
					continue;
				int index = addressList.indexOf(newAddress);
				addressList.set(index, newAddress);
			}
		}
	}

	/**
	 * Converts the list to an array
	 * 
	 * @return the list as a array
	 */
	public Address[] toArray() {
		return addressList.toArray(new Address[addressList.size()]);
	}

	/**
	 * Converts all the streets in the manager to an array and returns it
	 * 
	 * @return the list of streets as array
	 */
	public String[] toStreetArray() {
		List<String> streets = new ArrayList<String>(addressList.size());
		// loop and add the streets
		for (Address adr : addressList) {
			streets.add(adr.getStreet());
		}
		return streets.toArray(new String[streets.size()]);
	}

	/**
	 * Converts all the cities in the manager to an array and returns it
	 * 
	 * @return the list of cities as array
	 */
	public String[] toCityArray() {
		List<String> cities = new ArrayList<String>(addressList.size());
		// loop and add the cities
		for (Address adr : addressList) {
			cities.add(adr.getCity());
		}
		return cities.toArray(new String[cities.size()]);
	}
}
