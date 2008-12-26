package at.rc.tacos.client.net;

import java.util.Collection;

import org.apache.mina.core.session.IdleStatus;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchHandlerException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;
import at.rc.tacos.platform.net.mina.MessageHandler;
import at.rc.tacos.platform.net.mina.MessageIoSession;

/**
 * Handles the events from mina for the client application
 * 
 * @author mheiss
 */
public class ClientMessageHandler implements MessageHandler {

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(ClientMessageHandler.class);

	// the client context
	private ClientContext clientContext;

	/**
	 * Initializes the message handler
	 * 
	 * @param clientContext
	 *            the client context to use
	 */
	public ClientMessageHandler(ClientContext clientContext) {
		this.clientContext = clientContext;
	}

	@Override
	public void messageReceived(MessageIoSession session, Message<Object> message) throws Exception {
		// get the needed handlers
		HandlerFactory handlerFactory = clientContext.getHandlerFactory();
		DataChangeListenerFactory listenerFactory = clientContext.getDataChangeListenerFactory();

		// print out trace information
		if (log.isTraceEnabled()) {
			log.trace("Handling new request from " + session.getUsername());
			log.trace("RequestType:" + message.getFirstElement());
			log.trace("Objects:" + message.getObjects());
			log.trace("Params:" + message.getParams());
		}

		long startResolve = System.currentTimeMillis();
		// try to get a handler for the object
		Object firstElement = message.getFirstElement();
		Handler<Object> handler = handlerFactory.getHandler(firstElement.getClass().getName());
		if (handler == null) {
			throw new NoSuchHandlerException(firstElement.getClass().getName());
		}
		long endResolve = System.currentTimeMillis();

		// log the results
		if (log.isDebugEnabled()) {
			log.debug("Resolving the request agains " + handler.getClass().getSimpleName() + " took " + (endResolve - startResolve) + " ms");
		}

		long startHandle = System.currentTimeMillis();
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

			// now inform the listeners about the new data
			Collection<DataChangeListener<Object>> listeners = listenerFactory.getListeners(message.getFirstElement().getClass());
			Display.getDefault().asyncExec(new UiInformRunnable(listeners, session, message));
		}
		catch (Exception e) {
			String errorMessage = "Failed to handle the request: " + e.getMessage();
			log.error(errorMessage, e);
		}
		long endHandle = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Handling the request agains " + handler.getClass().getSimpleName() + " took " + (endHandle - startHandle) + " ms");
		}
	}

	@Override
	public void sessionCreated(MessageIoSession session) throws Exception {
	}

	@Override
	public void sessionClosed(MessageIoSession session) throws Exception {
	}

	@Override
	public void exceptionCaught(MessageIoSession session, Throwable throwable) throws Exception {
		log.error("Session caused an exception, closing session", throwable);
		session.closeOnFlush().awaitUninterruptibly(10000);
	}

	@Override
	public void messageSent(MessageIoSession session, Message<Object> message) throws Exception {
		// do nothing
	}

	@Override
	public void sessionIdle(MessageIoSession session, IdleStatus status) throws Exception {
		// do nothing
	}

	@Override
	public void sessionOpened(MessageIoSession session) throws Exception {
		// do nothing
	}

	/**
	 * Helper class to inform the listeners about the new data
	 */
	private class UiInformRunnable implements Runnable {

		// the current data
		private Message<Object> message;
		private MessageIoSession session;

		// the listeners to inform
		private Collection<DataChangeListener<Object>> listeners;

		/**
		 * Default class constructor
		 */
		public UiInformRunnable(Collection<DataChangeListener<Object>> listeners, MessageIoSession session, Message<Object> message) {
			this.listeners = listeners;
			this.session = session;
			this.message = message;
		}

		@Override
		public void run() {
			// assert valid listeners
			if (listeners == null) {
				return;
			}
			if (listeners.isEmpty()) {
				return;
			}
			for (DataChangeListener<Object> listener : listeners) {
				listener.dataChanged(message, session);
			}
		}

	}
}
