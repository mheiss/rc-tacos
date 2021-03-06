package at.rc.tacos.platform.net.mina;

import java.io.IOException;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.Message;

/**
 * Specialized {@link IoHandler} that handles all I/O events fired by MINA.
 * 
 * @author Michael
 */
public interface MessageHandler {

	/**
	 * Invoked when any exception is thrown by user {@link IoHandler}
	 * implementation or by MINA. If <code>cause</code> is instanceof
	 * {@link IOException}, MINA will close the connection automatically.
	 */
	public void exceptionCaught(MessageIoSession session, Throwable throwable) throws Exception;

	/**
	 * Invoked when a new message is received.
	 */
	public void messageReceived(MessageIoSession session, Message<Object> message) throws Exception;

	/**
	 * Invoked when a message written by {@link IoSession#write(Object)} is sent
	 * out.
	 */
	public void messageSent(MessageIoSession session, Message<Object> message) throws Exception;

	/**
	 * Invoked when the connection is closed
	 */
	public void sessionClosed(MessageIoSession session) throws Exception;

	/**
	 * Invoked from an I/O processor thread when a new connection has been
	 * created. Because this method is supposed to be called from the same
	 * thread that handles I/O of multiple sessions, please implement this
	 * method to perform tasks that consumes minimal amount of time such as
	 * socket parameter and user-defined session attribute initialization.
	 */
	public void sessionCreated(MessageIoSession session) throws Exception;

	/**
	 * Invoked with the related {@link IdleStatus} when a connection becomes
	 * idle.
	 */
	public void sessionIdle(MessageIoSession session, IdleStatus status) throws Exception;

	/**
	 * Invoked when a connection has been opened. This method is invoked after
	 * {@link #sessionCreated(MessageIoSession)}. The biggest difference from
	 * {@link #sessionCreated(MessageIoSession)} is that it's invoked from other
	 * thread than an I/O processor thread once thread modesl is configured
	 * properly.
	 */
	public void sessionOpened(MessageIoSession session) throws Exception;
}
