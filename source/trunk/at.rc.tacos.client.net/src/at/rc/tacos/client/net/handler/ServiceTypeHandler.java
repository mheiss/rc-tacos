package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>ServiceTypeHandler</code> manages the locally chached
 * {@link ServiceType} instances.
 * 
 * @author Michael
 */
public class ServiceTypeHandler implements Handler<ServiceType> {

	private List<ServiceType> serviceList = Collections.synchronizedList(new LinkedList<ServiceType>());
	private Logger log = LoggerFactory.getLogger(ServiceTypeHandler.class);

	@Override
	public void add(MessageIoSession session, Message<ServiceType> message) throws SQLException, ServiceException {
		synchronized (serviceList) {
			serviceList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<ServiceType> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<ServiceType> message) throws SQLException, ServiceException {
		synchronized (serviceList) {
			serviceList.clear();
			serviceList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<ServiceType> message) throws SQLException, ServiceException {
		synchronized (serviceList) {
			serviceList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<ServiceType> message) throws SQLException, ServiceException {
		synchronized (serviceList) {
			for (ServiceType updatedType : message.getObjects()) {
				if (!serviceList.contains(updatedType)) {
					continue;
				}
				int index = serviceList.indexOf(updatedType);
				serviceList.set(index, updatedType);
			}
		}
	}

	/**
	 * Returns the first <code>ServiceType</code> instance that exactly matches
	 * the string returned by {@link ServiceType#getServiceName()}.
	 * 
	 * @param serviceName
	 *            the name of the <code>ServiceType</code> to search for
	 * @return the matched object or null if nothing found
	 */
	public ServiceType getServiceTypeByName(String serviceName) {
		for (ServiceType service : serviceList) {
			if (service.getServiceName().equalsIgnoreCase(serviceName))
				return service;
		}
		// nothing found
		return null;
	}

	/**
	 * Returns a new array containing the managed <code>ServiceType</code>
	 * instances.
	 * 
	 * @return an array containing the <code>ServiceType</code> instances.
	 */
	public ServiceType[] toArray() {
		return serviceList.toArray(new ServiceType[serviceList.size()]);
	}

}
