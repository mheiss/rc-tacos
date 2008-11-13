package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.ServiceTypeService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class ServiceTypeHandler implements Handler<ServiceType> {

	@Service(clazz = ServiceTypeService.class)
	private ServiceTypeService serviceTypeService;

	@Override
	public void add(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = message.getObjects();
		// loop and add the service object
		for (ServiceType service : serviceList) {
			int id = serviceTypeService.addServiceType(service);
			// assert we have a valid id
			if (id == -1)
				throw new ServiceException("Failed to add the service type:" + service);
			service.setId(id);
		}
		// brodcast the added service objects
		session.writeBrodcast(message, serviceList);
	}

	@Override
	public void get(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		// get the params of the request
		Map<String, String> params = message.getParams();

		// query the service by the name
		if (params.containsKey(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER)) {
			final String nameFilter = params.get(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER);
			List<ServiceType> serviceList = serviceTypeService.listServiceTypesByName(nameFilter);
			if (serviceList == null)
				throw new ServiceException("Failed to query the services by the name");
			session.write(message, serviceList);
			return;
		}

		// query all service types
		List<ServiceType> list = serviceTypeService.listServiceTypes();
		session.write(message, list);
	}

	@Override
	public void remove(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = message.getObjects();
		// loop and remove the service object
		for (ServiceType service : serviceList) {
			if (!serviceTypeService.removeServiceType(service.getId()))
				throw new ServiceException("Failed to remove the service type: " + service);
		}
		// brodcast the removed services
		session.writeBrodcast(message, serviceList);
	}

	@Override
	public void update(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = message.getObjects();
		// loop and remove the service object
		for (ServiceType service : serviceList) {
			if (!serviceTypeService.updateServiceType(service))
				throw new ServiceException("Failed to update the service type: " + service);
		}
		// brodcast the updated services
		session.writeBrodcast(message, serviceList);
	}

	@Override
	public void execute(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
