package at.rc.tacos.client.net;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;
import at.rc.tacos.platform.net.mina.MessageClient;
import at.rc.tacos.platform.net.mina.MessageHandler;
import at.rc.tacos.platform.net.mina.MessageIoSession;

/**
 * The <code>NetWrapper</code> provides network access for the client
 * application. This is the starting point for all network based operations.
 * <p>
 * The {@link MessageClient} is used to initialize and manage the connection to
 * the server. The communication is handled by the associated
 * {@link MessageHandler} implementation.
 * </p>
 * 
 * @author mheiss
 */
public class NetWrapper {

	private static Logger log = LoggerFactory.getLogger(NetWrapper.class);

	// the singleton instance
	private static NetWrapper instance;

	// the message client
	private ClientContext context;
	private MessageClient client;
	private MessageHandler handler;

	/**
	 * Default class constructor to prevent instantiation
	 */
	private NetWrapper() {
	}

	/**
	 * Returns the shared instance
	 */
	public static NetWrapper getInstance() {
		if (instance == null)
			instance = new NetWrapper();
		return instance;
	}

	/**
	 * Initializes the <code>NetWrapper</code> plugin with the given context.
	 * 
	 * @param context
	 *            the <code>ClientContext</code> to use.
	 */
	public void init(ClientContext context) {
		this.context = context;
	}

	/**
	 * Opens a new connection to the remote host and initializes the connection.
	 * <p>
	 * 
	 * @param address
	 *            the {@link InetSocketAddress} to connect to
	 * @throws Exception
	 *             if an error occured during the setup
	 */
	public void openConnection(InetSocketAddress address) throws Exception {
		// create the handler instance
		handler = new ClientMessageHandler(context);

		// create a new message client
		client = new MessageClient(address);
		client.connect(handler);
	}

	/**
	 * Shutdown the network connection
	 */
	public void shutdown() throws Exception {
		if (client == null)
			return;
		// disconnect the connection
		client.disconnect();
		handler = null;
	}

	/**
	 * Returns the current {@link IoSession} handle. This method will return
	 * null when the connection to the server is broken due to network failures.
	 * <p>
	 * Note that the recommended way to send a message asynchronous to the
	 * server is the {@link NetWrapper#sendMessage(Message)} method.
	 * <p>
	 * 
	 * @return the session handle
	 * @see NetWrapper#sendMessage(Message)
	 */
	public static MessageIoSession getSession() {
		MessageClient client = getInstance().getClient();

		// assert valid client instance
		if (client == null) {
			return null;
		}

		// try to get and return the session
		try {
			return client.getSession();
		}
		catch (Exception ex) {
			log.error("Failed to get the session from the message client", ex);
			return null;
		}
	}

	/**
	 * Convenient wrapper around
	 * {@link DataChangeListenerFactory#registerListener(DataChangeListener, Class)}
	 * to register a listener.
	 * 
	 * @see DataChangeListenerFactory
	 */
	public static void registerListener(DataChangeListener<?> listener, Class<?> dataClazz) {
		ClientContext context = getInstance().getClientContext();
		DataChangeListenerFactory factory = context.getDataChangeListenerFactory();
		factory.registerListener(listener, dataClazz);
	}

	/**
	 * Convenient wrapper around
	 * {@link DataChangeListenerFactory#registerListener(DataChangeListener, Class)}
	 * to remove a listener.
	 * 
	 * @see DataChangeListenerFactory
	 */
	public static void removeListener(DataChangeListener<?> listener, Class<?> dataClazz) {
		ClientContext context = getInstance().getClientContext();
		DataChangeListenerFactory factory = context.getDataChangeListenerFactory();
		factory.removeListener(listener, dataClazz);
	}

	public static <T> Handler<T> getHandler(Class<T> modelClazz) {
		ClientContext context = getInstance().getClientContext();
		HandlerFactory factory = context.getHandlerFactory();
		return factory.getHandler(modelClazz.getName());
	}

	// GETTERS FOR THE PROPERTIES
	/**
	 * Returns the message client that holds and manages the connecion to the
	 * server
	 * 
	 * @return the message client
	 */
	protected MessageClient getClient() {
		return client;
	}

	/**
	 * Returns the current used <code>ClientContext</code> implementation
	 * 
	 * @return the client context
	 */
	public ClientContext getClientContext() {
		return context;
	}
}
