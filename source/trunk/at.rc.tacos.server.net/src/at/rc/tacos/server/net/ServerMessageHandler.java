package at.rc.tacos.server.net;

import java.sql.Connection;
import java.util.List;

import org.apache.mina.core.session.IdleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.net.exception.NoSuchHandlerException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.mina.MessageHandler;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.DbalServiceFactory;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.utils.DataSourceResolver;
import at.rc.tacos.platform.services.utils.ServiceAnnotationResolver;

/**
 * The <code>MessageHandler</code> is responsible for the communication between
 * the clients and the server.
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class ServerMessageHandler implements MessageHandler {

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(ServerMessageHandler.class);

	// the server context
	private final ServerContext serverContext;

	/**
	 * Default class constructor to create a new message handler
	 */
	public ServerMessageHandler(final ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	@Override
	public void messageReceived(MessageIoSession session, Message<Object> message) throws Exception {
		// get the needed factory instances
		HandlerFactory handlerFactory = serverContext.getHandlerFactory();
		DbalServiceFactory serviceFactory = serverContext.getDbalServiceFactory();
		DataSource dataSource = serverContext.getDataSource();

		long start = System.currentTimeMillis();

		// assert we have a connection
		Connection connection = dataSource.getConnection();
		if (connection == null)
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");

		// try to get a handler for the object
		Handler<Object> handler = handlerFactory.getHandler((Class<Object>) message.getFirstElement().getClass());
		if (handler == null) {
			throw new NoSuchHandlerException(message.getFirstElement().getClass().getSimpleName());
		}

		// now inject the needed services by this handler
		// and for all dependend services
		ServiceAnnotationResolver resolver = new ServiceAnnotationResolver(serviceFactory);
		List<Object> resolvedServices = resolver.resolveAnnotations(handler);
		// now check if the resolved services need a data source
		DataSourceResolver dataSourceResolver = new DataSourceResolver(connection);
		List<Object> resolvedSources = dataSourceResolver.resolveAnnotations(resolvedServices.toArray());

		long end = System.currentTimeMillis();

		// print out debugging information
		if (log.isTraceEnabled()) {
			// the handlers
			StringBuffer buffer = new StringBuffer();
			for (Object obj : resolvedServices) {
				buffer.append(obj.getClass().getSimpleName());
			}
			log.trace("Resolved handlers: " + buffer.toString());

			// the resources
			buffer = new StringBuffer();
			for (Object obj : resolvedSources) {
				buffer.append(obj.getClass().getSimpleName());
			}
			log.trace("Data source set for: " + buffer.toString());
		}

		// log the results
		if (log.isDebugEnabled()) {
			log.debug("Resolving the request agains " + handler.getClass().getSimpleName() + " took " + (end - start) + " ms");
		}

		// create a savepoint bevor handling the request
		connection.setAutoCommit(false);
		connection.setSavepoint();

		try {

			// now handle the request
			switch (message.getMessageType()) {
				case ADD:
					handler.add(session, message);
					break;
				case UPDATE:
					handler.update(session, message);
					break;
				case REMOVE:
					handler.remove(session, message);
					break;
				case GET:
					handler.get(session, message);
					break;
				default:
					handler.execute(session, message);
					break;
			}

			// the handling caused no error so commit the changes
			connection.commit();

		}
		catch (Exception ioe) {
			// rollback the changes
			connection.rollback();
			if (log.isDebugEnabled()) {
				log.debug("Rollback the changes due to a service error");
			}

			// create and setup the message
			String errorMessage = "Failed to handle the request: " + ioe.getMessage();
			// log the error
			log.error("errorMessage", ioe);
			// send back to the client
			session.writeError(message, errorMessage);
		}

		// close the connection
		connection.close();
	}

	@Override
	public void exceptionCaught(MessageIoSession session, Throwable throwable) throws Exception {
		log.error("Session caused an exception, closing session", throwable);
		session.closeOnFlush().awaitUninterruptibly(10000);
	}

	@Override
	public void sessionIdle(MessageIoSession session, IdleStatus status) throws Exception {
		// check if the idle session is not authenticated
		if (!session.isLoggedIn()) {
			log.info("Unauthenticated session is idle, closing");
			session.closeOnFlush().awaitUninterruptibly(10000);
		}
		// TODO: close idle connections from web clients
	}

	@Override
	public void messageSent(MessageIoSession session, Message<Object> message) throws Exception {
		// do nothing
	}

	@Override
	public void sessionClosed(MessageIoSession session) throws Exception {
		// do nothing
	}

	@Override
	public void sessionCreated(MessageIoSession session) throws Exception {
		// do nothing
	}

	@Override
	public void sessionOpened(MessageIoSession session) throws Exception {
		// do nothing
	}
}
