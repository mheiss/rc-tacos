package at.rc.tacos.platform.net;

import java.io.File;

import at.rc.tacos.platform.config.ServerConfiguration;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.DbalServiceFactory;

/**
 * Interface definition for a server context. Provides access to the resource
 * and service implementations of the current running server instance.
 * 
 * @author Michael
 */
public interface ServerContext {

	/**
	 * Returns the handler factory that holds the available handler
	 * implementation
	 * 
	 * @return the handler factory
	 */
	public HandlerFactory getHandlerFactory();

	/**
	 * Returns the service factory to holds the available dbal service
	 * implementations
	 * 
	 * @return the service factory
	 */
	public DbalServiceFactory getDbalServiceFactory();

	/**
	 * Returns the data source to query the database.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource();

	/**
	 * Returns the current {@link ServerConfiguration} instance that is attached
	 * to this context.
	 * 
	 * @return the client configuration
	 */
	public ServerConfiguration getServerConfiguration();

	/**
	 * Returns the configuration file of the server instance
	 * 
	 * @return the configuration file of the server.
	 */
	public File getConfigurationFile();
}
