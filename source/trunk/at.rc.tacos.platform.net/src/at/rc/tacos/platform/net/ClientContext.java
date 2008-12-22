package at.rc.tacos.platform.net;

import java.io.File;

import at.rc.tacos.platform.config.ClientConfiguration;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;

/**
 * Interface definition for a client context. Provides access to the resource
 * and service implementations of the current running client instance.
 * 
 * @author Michael
 */
public interface ClientContext {

	/**
	 * Returns the <code>DataChangeListenerFactory</code> that holds the
	 * available {@link DataChangeListener} implementations.
	 * 
	 * @return the listener factory implementation
	 */
	public DataChangeListenerFactory getDataChangeListenerFactory();

	/**
	 * Returns the <code>HandlerFactory</code> that holds the available
	 * {@link Handler} implementations.
	 * 
	 * @return the handler factory implementation
	 */
	public HandlerFactory getHandlerFactory();

	/**
	 * Returns the current {@link ClientConfiguration} instance that is attached
	 * to this context.
	 * 
	 * @return the client configuration
	 */
	public ClientConfiguration getClientConfiguration();

	/**
	 * Returns the configuration file of the client instance
	 * 
	 * @return the configuration file of the client.
	 */
	public File getConfigurationFile();
}
