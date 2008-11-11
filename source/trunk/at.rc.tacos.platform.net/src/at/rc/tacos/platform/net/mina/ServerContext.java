package at.rc.tacos.platform.net.mina;

import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.ServiceFactory;

/**
 * Interface definition for a server context. Provides access to the resource
 * and service implementations of the current running server instance.
 * 
 * @author Michael
 */
public interface ServerContext {

	/**
	 * Returns the handler factory that holds the available handler.
	 * implementation
	 * 
	 * @return the handler factory
	 */
	public HandlerFactory getHandlerFactory();

	/**
	 * Returns the service factory to holds the available service.
	 * implementations
	 * 
	 * @return the service factory
	 */
	public ServiceFactory getServiceFactory();

	/**
	 * Returns the data source to query the database.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource();
	
	/**
	 * Returns the port where the server instance should be listen to.
	 * @return the listening port for the server application
	 */
	public int getServerPort();
}
