package at.rc.tacos.platform.net;

import java.net.InetSocketAddress;
import java.util.List;

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
	 * Returns a list of {@link InetSocketAddress} address object that are
	 * available to connect.
	 * 
	 * @return the available remote hosts
	 */
	public List<InetSocketAddress> getINetSocketList();

}
