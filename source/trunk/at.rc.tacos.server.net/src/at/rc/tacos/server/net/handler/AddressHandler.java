package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.AddressService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The address handler is responsible for request that are made agains address
 * objects.
 * 
 * @author Michael
 */
public class AddressHandler implements Handler<Address> {

	@Service(clazz = AddressService.class)
	private AddressService addressService;

	// the logger for this class
	private Logger log = LoggerFactory.getLogger(AddressHandler.class);

	@Override
	public void add(ServerIoSession session, Message<Address> message) throws ServiceException, SQLException {
		List<Address> addressList = message.getObjects();
		// loop and try to add each address object
		for (Address adr : addressList) {
			// add each record to the database
			int id = addressService.addAddress(adr);
			if (id == -1)
				throw new ServiceException("Failed to add the address record, service returnd -1");
			adr.setAddressId(id);
		}
		// write the result back to the client
		session.writeBrodcast(message, addressList.toArray());
	}

	@Override
	public void get(ServerIoSession session, Message<Address> message) throws ServiceException, SQLException {
		// get the params from the message
		Map<String, String> params = message.getParams();

		// the filter types for the database
		String streetFilter = new String("%");
		String streetNumberFilter = new String("%");
		String cityFilter = new String("%");
		String zipFilter = new String("%");

		// get the passed filter values and add some wildcards
		if (params.containsKey(IFilterTypes.SEARCH_STRING_STREET)) {
			streetFilter = params.get(IFilterTypes.SEARCH_STRING_STREET);
			streetFilter = "%" + streetFilter + "%";
		}
		if (params.containsKey(IFilterTypes.SEARCH_STRING_CITY)) {
			cityFilter = params.get(IFilterTypes.SEARCH_STRING_CITY);
			cityFilter = "%" + cityFilter + "%";
		}
		if (params.containsKey(IFilterTypes.SEARCH_STRING_ZIP)) {
			zipFilter = params.get(IFilterTypes.SEARCH_STRING_ZIP);
			zipFilter = "%" + zipFilter + "%";
		}
		if (params.containsKey(IFilterTypes.SEARCH_STRING_STREETNUMBER)) {
			streetNumberFilter = params.get(IFilterTypes.SEARCH_STRING_STREETNUMBER);
			streetNumberFilter = "%" + streetNumberFilter + "%";
		}
		// the query filter
		final String addressFilter = "Street: " + streetFilter + " | City: " + cityFilter + " | Zip: " + zipFilter;
		log.debug(addressFilter);

		List<Address> addressList = addressService.getAddressList(streetFilter, streetNumberFilter, cityFilter, zipFilter);
		if (addressList == null)
			throw new ServiceException("Failed to list the address records by search string");

		// write the result back to the client
		session.write(message, addressList.toArray());
	}

	@Override
	public void remove(ServerIoSession session, Message<Address> message) throws ServiceException, SQLException {
		List<Address> addressList = message.getObjects();
		// loop and try to add each address object
		for (Address adr : addressList) {
			if (!addressService.removeAddress(adr.getAddressId()))
				throw new ServiceException("Failed to remove the address record");
		}
		// write the result back to the client
		session.writeBrodcast(message, addressList.toArray());
	}

	@Override
	public void update(ServerIoSession session, Message<Address> message) throws ServiceException, SQLException {
		List<Address> addressList = message.getObjects();
		// loop and try to add each address object
		for (Address adr : addressList) {
			if (!addressService.updateAddress(adr))
				throw new ServiceException("Failed to update the address record");
		}
		// write the result back to the client
		session.writeBrodcast(message, addressList.toArray());
	}

	@Override
	public void execute(ServerIoSession session, Message<Address> message) throws SQLException, ServiceException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
