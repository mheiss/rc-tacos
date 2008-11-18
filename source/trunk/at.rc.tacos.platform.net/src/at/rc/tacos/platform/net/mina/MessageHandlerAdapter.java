package at.rc.tacos.platform.net.mina;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.Message;

/**
 * Adapter between MINA {@link IoHandler} and the {@link MessageHandler} interface
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class MessageHandlerAdapter implements IoHandler {

    // properties
    private MessageHandler handler;

    /**
     * Default class constructor to create a new instance
     * 
     * @param handler
     *            the handler to use
     */
    public MessageHandlerAdapter(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.exceptionCaught(serverSession, throwable);
    }

    @Override
    public void messageReceived(IoSession session, Object object) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.messageReceived(serverSession, (Message<Object>) object);
    }

    @Override
    public void messageSent(IoSession session, Object object) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.messageSent(serverSession, (Message<Object>) object);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.sessionClosed(serverSession);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.sessionCreated(serverSession);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.sessionIdle(serverSession, status);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        MessageIoSession serverSession = new MessageIoSession(session);
        handler.sessionOpened(serverSession);
    }
}
