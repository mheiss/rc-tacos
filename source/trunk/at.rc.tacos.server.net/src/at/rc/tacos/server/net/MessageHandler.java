package at.rc.tacos.server.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.request.AbstractMessage;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.platform.services.net.INetHandler;

/**
 * The <code>MinaMessageHandler</code> is responsible for the communication between the clients and
 * the server. This handler implementation will be used if the server is the current primary server.
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

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.debug("Session opened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.debug("Session closed");
        serverContext.getDataSource();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("Session " + session + " caused an exception " + cause.getMessage());
        session.close(true);
        log.info("Session terminated because of an error");
    }

    /**
     * Returns a type save handler
     */
    public void getHandler(String modelClazz) throws Exception {
        HandlerFactoryImpl handlerFactory = (HandlerFactoryImpl) serverContext.getHandlerFactory();
        INetHandler<Address> addressHandler = handlerFactory.getTypeSaveHandler((Address) Class
                .forName("Address").newInstance());
    }

}
