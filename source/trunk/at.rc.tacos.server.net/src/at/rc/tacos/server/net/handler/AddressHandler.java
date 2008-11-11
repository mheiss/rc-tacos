package at.rc.tacos.server.net.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.mina.ServerContext;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.net.request.AddMessage;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.AddressService;
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
	public void execute(ServerIoSession session, ServerContext context, Message<Address> message) throws SQLException, ServiceException {
		if(message instanceof AddMessage) {
			for(Address adr:message.getObjects()) {
				add(adr);
			}
		}
		
	}

	public Address add(Address model) throws ServiceException, SQLException {
		// add the address to the database
		int id = addressService.addAddress(model);
		if (id == -1)
			throw new ServiceException("Failed to add the address record, service returnd -1");
		// set the id
		model.setAddressId(id);
		return model;
	}

	public List<Address> get(Map<String, String> params) throws ServiceException, SQLException {
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
		return addressList;
	}

	public Address remove(Address model) throws ServiceException, SQLException {
		if (!addressService.removeAddress(model.getAddressId()))
			throw new ServiceException("Failed to remove the address record");
		return model;
	}

	public Address update(Address model) throws ServiceException, SQLException {
		if (!addressService.updateAddress(model))
			throw new ServiceException("Failed to update the address record");
		return model;
	}
}
