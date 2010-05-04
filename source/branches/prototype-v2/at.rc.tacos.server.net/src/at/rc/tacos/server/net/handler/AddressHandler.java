package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.AddressService;
import at.rc.tacos.platform.services.dbal.LockableService;
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

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	// the logger for this class
	private Logger log = LoggerFactory.getLogger(AddressHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Address> message) throws ServiceException, SQLException {
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
		session.writeResponseBrodcast(message, addressList);
	}

	@Override
	public void get(MessageIoSession session, Message<Address> message) throws ServiceException, SQLException {
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

		// check if we have locks for this address records
		for (Address adr : addressList) {
			Lockable lockable = lockableService.getLock(adr);
			if (lockable == null) {
				continue;
			}
			adr.setLocked(lockable.isLocked());
			adr.setLockedBy(lockable.getLockedBy());
		}

		// write the result back to the client
		session.writeResponse(message, addressList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Address> message) throws ServiceException, SQLException {
		List<Address> addressList = message.getObjects();
		// loop and try to remove each address object
		for (Address adr : addressList) {
			if (!addressService.removeAddress(adr.getAddressId()))
				throw new ServiceException("Failed to remove the address record");
			// remove the lockable instance
			lockableService.removeLock(adr);
		}
		// write the result back to the client
		session.writeResponseBrodcast(message, addressList);
	}

	@Override
	public void update(MessageIoSession session, Message<Address> message) throws ServiceException, SQLException {
		List<Address> addressList = message.getObjects();
		// loop and try to add each address object
		for (Address adr : addressList) {
			if (!addressService.updateAddress(adr))
				throw new ServiceException("Failed to update the address record");
			// update the lockable instance
			lockableService.updateLock(adr);
		}
		// write the result back to the client
		session.writeResponseBrodcast(message, addressList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Address> message) throws SQLException, NoSuchCommandException {
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			// brodcast the new lock
			UpdateMessage<Address> updateMessage = new UpdateMessage<Address>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			// brodcast the removed lock
			UpdateMessage<Address> updateMessage = new UpdateMessage<Address>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		// throw an execption because the 'exec' command is not implemented
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public Address[] toArray() {
		return null;
	}
}
