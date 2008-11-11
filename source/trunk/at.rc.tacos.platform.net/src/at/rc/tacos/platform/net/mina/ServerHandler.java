package at.rc.tacos.platform.net.mina;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.request.Message;
import at.rc.tacos.platform.services.ServerContext;

/**
 * The handler is responsible to process the requests from the clients.
 * 
 * @author Michael
 */
public interface ServerHandler {
	
	/**
	 * Invoked to initialize the handler.
	 */
	public void init(ServerContext context);

	/**
	 * Invoked when any exception is thrown by user {@link IoHandler}
	 * implementation or by MINA. If <code>cause</code> is instanceof
	 * {@link IOException}, MINA will close the connection automatically.
	 */
	public void exceptionCaught(ServerIoSession session, Throwable throwable) throws Exception;

	/**
	 * Invoked when a new message is received.
	 */
	public void messageReceived(ServerIoSession session, Message message) throws Exception;

	/**
	 * Invoked when a message written by {@link IoSession#write(Object)} is sent
	 * out.
	 */
	public void messageSent(ServerIoSession session, Message message) throws Exception;

	/**
	 * Invoked when the connection is closed
	 */
	public void sessionClosed(ServerIoSession session) throws Exception;

	/**
	 * Invoked from an I/O processor thread when a new connection has been
	 * created. Because this method is supposed to be called from the same
	 * thread that handles I/O of multiple sessions, please implement this
	 * method to perform tasks that consumes minimal amount of time such as
	 * socket parameter and user-defined session attribute initialization.
	 */
	public void sessionCreated(ServerIoSession session) throws Exception;

	/**
	 * Invoked with the related {@link IdleStatus} when a connection becomes
	 * idle.
	 */
	public void sessionIdle(ServerIoSession session, IdleStatus status) throws Exception;

	/**
	 * Invoked when a connection has been opened. This method is invoked after
	 * {@link #sessionCreated(IoSession)}. The biggest difference from
	 * {@link #sessionCreated(IoSession)} is that it's invoked from other thread
	 * than an I/O processor thread once thread modesl is configured properly.
	 */
	public void sessionOpened(ServerIoSession session) throws Exception;
}
