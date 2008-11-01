package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.ServiceTypeService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class ServiceTypeHandler implements INetHandler<ServiceType> {

	@Service(clazz = ServiceTypeService.class)
	private ServiceTypeService serviceTypeService;

	@Override
	public ServiceType add(ServiceType model) throws ServiceException, SQLException {
		int id = serviceTypeService.addServiceType(model);
		// assert we have a valid id
		if (id == -1)
			throw new ServiceException("Failed to add the service type:" + model);
		model.setId(id);
		return model;
	}

	@Override
	public List<ServiceType> execute(String command, List<ServiceType> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<ServiceType> get(Map<String, String> params) throws ServiceException, SQLException {
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
	public ServiceType remove(ServiceType model) throws ServiceException, SQLException {
		if (!serviceTypeService.removeServiceType(model.getId()))
			throw new ServiceException("Failed to remove the service type: " + model);
		return model;
	}

	@Override
	public ServiceType update(ServiceType model) throws ServiceException, SQLException {
		if (!serviceTypeService.updateServiceType(model))
			throw new ServiceException("Failed to update the service type: " + model);
		return model;
	}

}
