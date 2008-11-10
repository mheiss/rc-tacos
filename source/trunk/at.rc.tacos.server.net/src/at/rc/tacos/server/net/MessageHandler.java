package at.rc.tacos.server.net;

import java.sql.Connection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.request.AbstractMessage;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.platform.services.exception.NoSuchHandlerException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.HandlerFactory;
import at.rc.tacos.platform.services.net.INetHandler;

/**
 * The <code>MinaMessageHandler</code> is responsible for the communication
 * between the clients and the server. This handler implementation will be used
 * if the server is the current primary server.
 * 
 * @author Michael
 */
public class MessageHandler extends IoHandlerAdapter {

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(MessageHandler.class);

	// the server context
	private ServerContext serverContext;

	/**
	 * Default class constructor
	 * 
	 * @param serverContext
	 *            the server context
	 */
	public MessageHandler(ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// get the received message object
		AbstractMessage request = (AbstractMessage) message;

		// get the needed factory instances
		HandlerFactory handlerFactory = serverContext.getHandlerFactory();
		//ServiceFactory serviceFactory = serverContext.getServiceFactory();
		DataSource dataSource = serverContext.getDataSource();

		// assert we have a connection
		Connection connection = dataSource.getConnection();
		if (connection == null)
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");

		// handle each passed object of the request
		for (Object obj : request.getObjects()) {
			// try to get a handler for the object
			INetHandler<Object> handler = handlerFactory.getTypeSaveHandler((Object) Class.forName(obj.getClass().getName()));
			if (handler == null)
				throw new NoSuchHandlerException(obj.getClass().getName());

			// now inject the needed services by this handler
			// and for all dependend services

		}

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("Session " + session + " caused an exception " + cause.getMessage());
		session.close(true);
		log.info("Session terminated because of an error");
	}

}
