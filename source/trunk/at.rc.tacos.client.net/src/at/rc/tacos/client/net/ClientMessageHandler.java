package at.rc.tacos.client.net;

import org.apache.mina.core.session.IdleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.mina.MessageHandler;
import at.rc.tacos.platform.net.mina.MessageIoSession;

/**
 * Handles the events from mina for the client application
 * 
 * @author mheiss
 * 
 */
public class ClientMessageHandler implements MessageHandler {

    // the logging plugin
    private Logger log = LoggerFactory.getLogger(ClientMessageHandler.class);

    // the client context
    private ClientContext context;

    /**
     * Initializes the message handler
     * 
     * @param context
     *            the client context to use
     */
    public ClientMessageHandler(ClientContext context) {
        this.context = context;
    }

    @Override
    public void messageReceived(MessageIoSession session, Message<Object> message) throws Exception {
        log.trace(context.getClass().getSimpleName());
    }

    @Override
    public void sessionCreated(MessageIoSession session) throws Exception {
        // TODO hide reconnect dialog (if showed)
        // TODO send outstanding (chached) messages
    }

    @Override
    public void sessionClosed(MessageIoSession session) throws Exception {
        // TODO: show dialog that connection is terminated
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

}
