package at.rc.tacos.server.net;

import java.sql.Connection;
import java.util.List;

import org.apache.mina.core.session.IdleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.mina.ServerHandler;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.net.request.AddMessage;
import at.rc.tacos.platform.net.request.GetMessage;
import at.rc.tacos.platform.net.request.Message;
import at.rc.tacos.platform.net.request.RemoveMessage;
import at.rc.tacos.platform.net.request.UpdateMessage;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.DataSourceResolver;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.platform.services.ServiceAnnotationResolver;
import at.rc.tacos.platform.services.ServiceFactory;
import at.rc.tacos.platform.services.exception.NoSuchHandlerException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.HandlerFactory;
import at.rc.tacos.platform.services.net.INetHandler;

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
	public void messageReceived(ServerIoSession session, Message message) throws Exception {
		// get the needed factory instances
		HandlerFactory handlerFactory = serverContext.getHandlerFactory();
		ServiceFactory serviceFactory = serverContext.getServiceFactory();
		DataSource dataSource = serverContext.getDataSource();

		// assert we have a connection
		Connection connection = dataSource.getConnection();
		if (connection == null)
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");

		// handle each passed object of the request
		for (Object obj : message.getObjects()) {
			long startResolving = System.currentTimeMillis();
			// try to get a handler for the object
			INetHandler<Object> handler = handlerFactory.getTypeSaveHandler(obj);
			if (handler == null)
				throw new NoSuchHandlerException(obj.getClass().getName());

			// now inject the needed services by this handler
			// and for all dependend services
			ServiceAnnotationResolver resolver = new ServiceAnnotationResolver(serviceFactory);
			List<Object> resolvedServices = resolver.resolveAnnotations(handler);

			// now check if the resolved services need a data source
			DataSourceResolver dataSourceResolver = new DataSourceResolver(connection);
			dataSourceResolver.resolveAnnotations(resolvedServices);

			long endResolving = System.currentTimeMillis();

			if (log.isDebugEnabled()) {
				log.debug("Resoled: " + resolvedServices.size() + " service(s) in " + (endResolving - startResolving) + " ms");
			}

			// now proccess the request
			if (message instanceof AddMessage) {
				Object addResult = handler.add(obj);
				// send the result of the operation back
				AddMessage responseMessage = new AddMessage(addResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (message instanceof UpdateMessage) {
				Object updateResult = handler.update(obj);
				// send the result of the operation back
				UpdateMessage responseMessage = new UpdateMessage(updateResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (message instanceof RemoveMessage) {
				Object removeResult = handler.remove(obj);
				// send the result of the operation back
				RemoveMessage responseMessage = new RemoveMessage(removeResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (message instanceof GetMessage) {
				List<Object> getResult = handler.get(message.getParams());
				// send the result of the operation back
				GetMessage responseMessage = new GetMessage(getResult.get(0).getClass());
				responseMessage.asnchronRequest(session);
				continue;
			}
		}
	}

	@Override
	public void exceptionCaught(ServerIoSession session, Throwable throwable) throws Exception {
		log.error("Session caused an exception, closing session", throwable);
		session.closeOnFlush().awaitUninterruptibly(10000);
	}

	@Override
	public void messageSent(ServerIoSession session, Message message) throws Exception {
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
