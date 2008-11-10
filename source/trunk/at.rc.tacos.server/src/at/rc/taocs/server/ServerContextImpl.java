package at.rc.taocs.server;

import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.platform.services.ServiceFactory;
import at.rc.tacos.platform.services.net.HandlerFactory;
import at.rc.tacos.server.dbal.DataSourceImpl;
import at.rc.tacos.server.dbal.ServiceFactoryImpl;
import at.rc.tacos.server.net.HandlerFactoryImpl;

/**
 * The server context manages and handles the resources for the application
 * 
 * @author Michael
 */
public class ServerContextImpl implements ServerContext {

	private HandlerFactory handlerFactory;
	private ServiceFactory serviceFactory;
	private DataSource dataSource;
	private int serverPort;

	/**
	 * Default class constructor to create a new server context
	 */
	public ServerContextImpl(int serverPort) {
		this.serverPort = serverPort;
		init();
	}

	/**
	 * Initialize and setup the server context
	 */
	private void init() {
		// create the default handler factory implementations
		handlerFactory = new HandlerFactoryImpl();
		serviceFactory = new ServiceFactoryImpl();
		dataSource = new DataSourceImpl();
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
	 * Returns the current implementation of the {@linkplain ServiceFactory}.
	 * 
	 * @return the service factory implementation.
	 */
	@Override
	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	/**
	 * Returns the current implementation of the {@linkplain DataSource}.
	 * 
	 * @return the data source implementation
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Returns the port where the server should listen to
	 * 
	 * @return the port number
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}
}
