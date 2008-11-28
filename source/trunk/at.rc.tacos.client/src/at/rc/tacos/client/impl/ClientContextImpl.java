package at.rc.tacos.client.impl;

import java.net.InetSocketAddress;
import java.util.List;

import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.services.listeners.DataChangeListenerFactory;

/**
 * The client context handles the resources and factory implementations for the
 * client application
 * 
 * @author Michael
 */
public class ClientContextImpl implements ClientContext {

	private List<InetSocketAddress> inetAddressList;
	private HandlerFactory handlerFactory;
	private DataChangeListenerFactory dataChangeFactory;

	/**
	 * Default class constructor to create a new instance.
	 * 
	 * @param inetAddressList
	 *            the list of configured socket objects
	 */
	public ClientContextImpl(List<InetSocketAddress> inetAddressList) {
		this.inetAddressList = inetAddressList;
		init();
	}

	/**
	 * Initialize and setup the client context
	 */
	private void init() {
		handlerFactory = new HandlerFactoryImpl();
		dataChangeFactory = new DataChangeFactoryImpl();
	}

	/**
	 * Returns the current implementation of the {@linkplain HandlerFactory}.
	 * 
	 * @return the handler factory implementation
	 */
	@Override
	public HandlerFactory getHandlerFactory() {
		return handlerFactory;
	}

	/**
	 * Returns the current implementation of the
	 * {@link DataChangeListenerFactory}.
	 * 
	 * @return the data change listener factory implementation
	 */
	@Override
	public DataChangeListenerFactory getDataChangeListenerFactory() {
		return dataChangeFactory;
	}

	/**
	 * Returns a list of configured socket address objects.
	 * 
	 * @return the list of socket
	 */
	@Override
	public List<InetSocketAddress> getINetSocketList() {
		return inetAddressList;
	}

}
