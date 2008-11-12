package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		int id = serviceTypeService.addServiceType(model);
		// assert we have a valid id
		if (id == -1)
			throw new ServiceException("Failed to add the service type:" + model);
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = new ArrayList<ServiceType>();
		if (params.containsKey(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER)) {
			final String nameFilter = params.get(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER);
			serviceList = serviceTypeService.listServiceTypesByName(nameFilter);
		}
		else {
			serviceList = serviceTypeService.listServiceTypes();
		}
		return serviceList;
	}

	@Override
	public void remove(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		if (!serviceTypeService.removeServiceType(model.getId()))
			throw new ServiceException("Failed to remove the service type: " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		if (!serviceTypeService.updateServiceType(model))
			throw new ServiceException("Failed to update the service type: " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
