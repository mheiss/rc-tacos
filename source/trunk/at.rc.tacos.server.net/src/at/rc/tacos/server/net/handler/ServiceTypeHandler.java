package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.dbal.ServiceTypeService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class ServiceTypeHandler implements Handler<ServiceType> {

	@Service(clazz = ServiceTypeService.class)
	private ServiceTypeService serviceTypeService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
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
		session.writeResponseBrodcast(message, serviceList);
	}

	@Override
	public void get(MessageIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		// get the params of the request
		Map<String, String> params = message.getParams();

		// query the service by the name
		if (params.containsKey(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER)) {
			final String nameFilter = params.get(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER);
			List<ServiceType> serviceList = serviceTypeService.listServiceTypesByName(nameFilter);
			if (serviceList == null)
				throw new ServiceException("Failed to query the services by the name");
			// check for lock
			for (ServiceType serviceType : serviceList) {
				if (!lockableService.containsLock(serviceType)) {
					continue;
				}
				Lockable lockable = lockableService.getLock(serviceType);
				serviceType.setLocked(lockable.isLocked());
				serviceType.setLockedBy(lockable.getLockedBy());
			}
			// send the result back
			session.writeResponse(message, serviceList);
			return;
		}

		// query all service types
		List<ServiceType> serviceList = serviceTypeService.listServiceTypes();
		for (ServiceType serviceType : serviceList) {
			if (!lockableService.containsLock(serviceType)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(serviceType);
			serviceType.setLocked(lockable.isLocked());
			serviceType.setLockedBy(lockable.getLockedBy());
		}
		session.writeResponse(message, serviceList);
	}

	@Override
	public void remove(MessageIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = message.getObjects();
		// loop and remove the service object
		for (ServiceType service : serviceList) {
			if (!serviceTypeService.removeServiceType(service.getId()))
				throw new ServiceException("Failed to remove the service type: " + service);
			// remove the lock
			lockableService.removeLock(service);
		}
		// brodcast the removed services
		session.writeResponseBrodcast(message, serviceList);
	}

	@Override
	public void update(MessageIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
		List<ServiceType> serviceList = message.getObjects();
		// loop and remove the service object
		for (ServiceType service : serviceList) {
			if (!serviceTypeService.updateServiceType(service))
				throw new ServiceException("Failed to update the service type: " + service);
			// update the lock
			lockableService.updateLock(service);
		}
		// brodcast the updated services
		session.writeResponseBrodcast(message, serviceList);
	}

	@Override
	public void execute(MessageIoSession session, Message<ServiceType> message) throws ServiceException, SQLException {
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
