package at.rc.tacos.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.net.exception.ConfigurationException;
import at.rc.tacos.platform.net.mina.MessageHandler;
import at.rc.tacos.platform.net.mina.MessageHandlerAdapter;
import at.rc.tacos.platform.net.mina.XmlCodecFactory;

/**
 * The message server manages the communication between the clients and the
 * server
 * 
 * @author Michael
 */
public class MinaMessageServer {

	// the logging plugin
	private Logger log = LoggerFactory.getLogger(MinaMessageServer.class);

	private SocketAcceptor acceptor;
	private ExecutorService filterExecutor;
	private MessageHandler handler;

	/**
	 * Starts listening to client connections
	 */
	public void start(ServerContext serverContext) {
		log.info("Attemp to start mina socket listener");
		try {
			acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors());
			acceptor.setReuseAddress(false);
			acceptor.getSessionConfig().setReadBufferSize(2048);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 300);

			MdcInjectionFilter mdcFilter = new MdcInjectionFilter();
			acceptor.getFilterChain().addLast("mdcFilter", mdcFilter);

			filterExecutor = new OrderedThreadPoolExecutor();
			acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(filterExecutor));
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
			acceptor.getFilterChain().addLast("mdcFilter2", mdcFilter);

			handler = new ServerMessageHandler(serverContext);
			acceptor.setHandler(new MessageHandlerAdapter(handler));

			int serverPort = serverContext.getServerConfiguration().getServerPort();

			try {
				acceptor.bind(new InetSocketAddress(serverPort));
			}
			catch (IOException ioe) {
				throw new ConfigurationException("Failed to listen on port " + serverPort + ", check the configuration", ioe.getCause());
			}

			log.info("Listening to client connections on port " + acceptor.getLocalAddress().getPort());
		}
		catch (RuntimeException e) {
			stop();
			throw e;
		}
	}

	/**
	 * Shutdown the server and closes all open connections.
	 */
	public void stop() {
		log.debug("Shuting down the server");
		log.debug("Try to close the active sessions (" + acceptor.getManagedSessionCount() + ")");
		// close each open session
		for (Map.Entry<Long, IoSession> entry : acceptor.getManagedSessions().entrySet()) {
			IoSession session = entry.getValue();
			log.debug("Attemp to terminate the session " + session);
			session.close(true);
		}
		// close server socket
		if (acceptor != null) {
			acceptor.unbind();
			acceptor.dispose();
			acceptor = null;
		}
		// close the execution pool
		if (filterExecutor != null) {
			filterExecutor.shutdown();
			try {
				filterExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				log.warn("Failed to await the termination of the thread pool executor");
			}
		}
		log.debug("Server shutdown successfully");
	}

	/**
	 * Returns the server acceptor
	 * 
	 * @return the server acceptor
	 */
	public SocketAcceptor getAcceptor() {
		return acceptor;
	}
}
