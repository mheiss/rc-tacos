package at.rc.tacos.client.net;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.mina.MessageClient;
import at.rc.tacos.platform.net.mina.MessageHandler;

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

	private Logger log = LoggerFactory.getLogger(NetWrapper.class);

	// the singleton instance
	private static NetWrapper instance;

	// the message client
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
	protected static NetWrapper getInstance() {
		if (instance == null)
			instance = new NetWrapper();
		return instance;
	}

	/**
	 * Opens a connection to the server and starts listening to incomming data
	 */
	public void start(ClientContext context) throws Exception {
		// create the handler instance
		handler = new ClientMessageHandler(context);

		// create a new message client
		client = new MessageClient(context.getSocketAddress());
		client.connect(handler);
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
	public static IoSession getSession() {
		// get the current instance
		NetWrapper wrapper = getInstance();
		MessageClient client = wrapper.getClient();

		// assert valid client instance
		if (client == null) {
			return null;
		}

		// try to get and return the session
		try {
			return client.getSession();
		}
		catch (Exception ex) {
			wrapper.log.error("Failed to get the session from the message client", ex);
			return null;
		}
	}

	/**
	 * Sends the message using the current {@link IoSession} instance. This is
	 * the recommended way to send a message asynchronous to the server.
	 * <p>
	 * If there is currently no valid session available then the message is
	 * caches and will be send when the sessions is available again.
	 * </p>
	 * 
	 * @param message
	 *            the message to send to the server
	 */
	public static void sendMessage(Message<Object> message) {

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

}
