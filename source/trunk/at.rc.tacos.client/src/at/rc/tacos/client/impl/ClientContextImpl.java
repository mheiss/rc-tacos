package at.rc.tacos.client.impl;

import java.util.List;

import at.rc.tacos.platform.model.ServerInfo;
import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;

/**
 * The client context handles the resources and factory implementations for the
 * client application
 * 
 * @author Michael
 */
public class ClientContextImpl implements ClientContext {

	private List<ServerInfo> servers;
	private HandlerFactory handlerFactory;
	private DataChangeListenerFactory dataChangeFactory;

	/**
	 * Default class constructor to create a new instance.
	 * 
	 * @param servers
	 *            the list of configured servers to connect
	 */
	public ClientContextImpl(List<ServerInfo> servers) {
		this.servers = servers;
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
	 * Returns a list of configured <code>ServerInfo</code> instances address
	 * objects.
	 * 
	 * @return the list of servers to connect
	 */
	@Override
	public List<ServerInfo> getServers() {
		return servers;
	}
}
