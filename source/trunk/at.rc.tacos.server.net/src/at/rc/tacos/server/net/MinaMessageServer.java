package at.rc.tacos.server.net;

import java.net.InetSocketAddress;
import java.util.Map.Entry;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.XmlCodecFactory;
import at.rc.tacos.platform.services.ServerContext;

/**
 * The message server manages the communication between the clients and the
 * server
 * 
 * @author Michael
 */
public class MinaMessageServer {

	// the socket acceptor
	private IoAcceptor acceptor;

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(MinaMessageServer.class);

	// the server context
	private ServerContext serverContext;

	/**
	 * Default class constructor to create a new message server
	 * 
	 * @param context
	 *            the server context
	 */
	public MinaMessageServer(ServerContext serverContext) {
		this.serverContext = serverContext;
		init();
	}

	/**
	 * Initializes the mina message server
	 */
	private void init() {
		acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));

		acceptor.setHandler(new MessageHandler(serverContext));

		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		log.debug("MinaMessageServer initialized");
	}

	/**
	 * Starts listening to client connections
	 * 
	 * @throws Exception
	 *             when the server cannot listen on the given port
	 */
	public void listen() throws Exception {
		log.debug("Attemp to start mina socket listener");
		acceptor.bind(new InetSocketAddress(serverContext.getServerPort()));
		log.debug("Listening to client connections on port " + acceptor.getLocalAddress());
	}

	/**
	 * Shutdown the server and closes the connection
	 */
	public void shutdown() throws Exception {
		log.debug("Shuting down the server");
		log.debug("Try to close the active sessions (" + acceptor.getManagedSessionCount() + ")");
		// close each open session
		for (Entry<Long, IoSession> entry : acceptor.getManagedSessions().entrySet()) {
			IoSession session = entry.getValue();
			log.debug("Attemp to terminate the session " + session);
			session.close(true);
		}
		acceptor.unbind();
		acceptor.dispose();
		log.debug("Server shutdown successfully");
	}
}
