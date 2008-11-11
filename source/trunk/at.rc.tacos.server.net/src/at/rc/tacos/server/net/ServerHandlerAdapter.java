package at.rc.tacos.server.net;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.mina.ServerHandler;
import at.rc.tacos.platform.net.mina.ServerIoSession;

/**
 * Adapter between MINA handler and the {@link ServerHandler} interface
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class ServerHandlerAdapter implements IoHandler {

	// properties
	private ServerHandler handler;

	/**
	 * Default class constructor to create a new instance
	 * 
	 * @param handler
	 *            the handler to use
	 */
	public ServerHandlerAdapter(ServerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.exceptionCaught(serverSession, throwable);
	}

	@Override
	public void messageReceived(IoSession session, Object object) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.messageReceived(serverSession, (Message<Object>) object);
	}

	@Override
	public void messageSent(IoSession session, Object object) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.messageSent(serverSession, (Message<Object>) object);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.sessionClosed(serverSession);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.sessionCreated(serverSession);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.sessionIdle(serverSession, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		ServerIoSession serverSession = new ServerIoSession(session);
		handler.sessionOpened(serverSession);
	}
}
