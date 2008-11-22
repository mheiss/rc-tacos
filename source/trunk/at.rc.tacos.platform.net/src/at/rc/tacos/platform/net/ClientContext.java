package at.rc.tacos.platform.net;

import java.net.InetSocketAddress;

import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;

/**
 * Interface definition for a client context. Provides access to the resource
 * and service implementations of the current running client instance.
 * 
 * @author Michael
 */
public interface ClientContext {

	// listeners

	/**
	 * Returns the {@link Handler} factory that holds the available handler
	 * implementation
	 * 
	 * @return the handler factory implementation
	 */
	public HandlerFactory getHandlerFactory();

	/**
	 * Returns the {@link InetSocketAddress} that should be used to connect to
	 * the server
	 * 
	 * @return the socket address of the remote host to connect
	 */
	public InetSocketAddress getSocketAddress();
}
