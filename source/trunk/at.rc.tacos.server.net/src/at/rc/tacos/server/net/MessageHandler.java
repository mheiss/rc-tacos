package at.rc.tacos.server.net;

import java.sql.Connection;
import java.util.List;

import org.apache.mina.core.session.IdleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.mina.ServerContext;
import at.rc.tacos.platform.net.mina.ServerHandler;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.DataSourceResolver;
import at.rc.tacos.platform.services.ServiceAnnotationResolver;
import at.rc.tacos.platform.services.ServiceFactory;
import at.rc.tacos.platform.services.exception.NoSuchHandlerException;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>MessageHandler</code> is responsible for the communication between
 * the clients and the server.
 * 
 * @author Michael
 */
public class MessageHandler implements ServerHandler {

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(MessageHandler.class);

	// the server context
	private ServerContext serverContext;

	/**
	 * Initialize the message handler
	 */
	public void init(final ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	@Override
	public void messageReceived(ServerIoSession session, Message<Object> message) throws Exception {
		// get the needed factory instances
		HandlerFactory handlerFactory = serverContext.getHandlerFactory();
		ServiceFactory serviceFactory = serverContext.getServiceFactory();
		DataSource dataSource = serverContext.getDataSource();

		long start = System.currentTimeMillis();

		// assert we have a connection
		Connection connection = dataSource.getConnection();
		if (connection == null)
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");

		Object requestModel = message.getObjects().get(0);
		// try to get a handler for the object
		Handler<Object> handler = handlerFactory.getHandler(requestModel);
		if (handler == null)
			throw new NoSuchHandlerException(requestModel.getClass().getName());

		// now inject the needed services by this handler
		// and for all dependend services
		ServiceAnnotationResolver resolver = new ServiceAnnotationResolver(serviceFactory);
		List<Object> resolvedServices = resolver.resolveAnnotations(handler);
		
		// now check if the resolved services need a data source
		DataSourceResolver dataSourceResolver = new DataSourceResolver(connection);
		dataSourceResolver.resolveAnnotations(resolvedServices.toArray());

		long end = System.currentTimeMillis();

		if (log.isDebugEnabled()) {
			log.debug("Resolving the request agains " + handler.getClass().getSimpleName() + " took " + (end - start) + " ms");
		}

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
				case EXEC:
					handler.execute(session, message);
					break;
			}
		}
		catch (Exception ioe) {
			// create and setup the message
			String errorMessage = "Failed to handle the request: " + ioe.getMessage();
			// log the error
			log.error("errorMessage", ioe);
			// send back to the client
			session.writeError(message, errorMessage);
		}
	}

	@Override
	public void exceptionCaught(ServerIoSession session, Throwable throwable) throws Exception {
		log.error("Session caused an exception, closing session", throwable);
		session.closeOnFlush().awaitUninterruptibly(10000);
	}

	@Override
	public void messageSent(ServerIoSession session, Message<Object> message) throws Exception {
		// do nothing
	}

	@Override
	public void sessionClosed(ServerIoSession session) throws Exception {
		// do nothing
	}

	@Override
	public void sessionCreated(ServerIoSession session) throws Exception {
		// do nothing
	}

	@Override
	public void sessionIdle(ServerIoSession session, IdleStatus status) throws Exception {
		// check if the idle session is not authenticated
		if (!session.isLoggedIn()) {
			log.info("Unauthenticated session is idle, closing");
			session.closeOnFlush().awaitUninterruptibly(10000);
		}
		// TODO: close idle connections from web clients
	}

	@Override
	public void sessionOpened(ServerIoSession session) throws Exception {
		// do nothing
	}
}
