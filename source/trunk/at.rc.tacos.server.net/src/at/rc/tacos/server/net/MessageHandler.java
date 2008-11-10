package at.rc.tacos.server.net;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.request.AbstractMessage;
import at.rc.tacos.platform.net.request.AddMessage;
import at.rc.tacos.platform.net.request.ExecMessage;
import at.rc.tacos.platform.net.request.GetMessage;
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
		ServiceFactory serviceFactory = serverContext.getServiceFactory();
		DataSource dataSource = serverContext.getDataSource();

		// assert we have a connection
		Connection connection = dataSource.getConnection();
		if (connection == null)
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");

		// handle each passed object of the request
		for (Object obj : request.getObjects()) {
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
			if (request instanceof AddMessage) {
				Object addResult = handler.add(obj);
				// send the result of the operation back
				AddMessage responseMessage = new AddMessage(addResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (request instanceof UpdateMessage) {
				Object updateResult = handler.update(obj);
				// send the result of the operation back
				UpdateMessage responseMessage = new UpdateMessage(updateResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (request instanceof RemoveMessage) {
				Object removeResult = handler.remove(obj);
				// send the result of the operation back
				RemoveMessage responseMessage = new RemoveMessage(removeResult);
				responseMessage.asnchronRequest(session);
				continue;
			}
			else if (request instanceof GetMessage) {
				List<Object> getResult = handler.get(request.getParams());
				// send the result of the operation back
				GetMessage responseMessage = new GetMessage(getResult.get(0).getClass());
				responseMessage.asnchronRequest(session);
				continue;
			}
			else {
				List<Object> execResult = handler.execute(request.getClass().getName(), request.getObjects(), request.getParams());
				//send the result of the operation back
			}
		}

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("Session " + session + " caused an exception " + cause.getMessage());
		session.close(true);
		log.info("Session terminated because of an error");
	}

}
