package at.rc.tacos.platform.net.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Base class for client applications
 * 
 * @author Michael
 */
public class MessageClient {

	public static final int CONNECT_TIMEOUT = 3000;

	// the connector
	private NioSocketConnector connector;

	/**
	 * Starts the client application
	 */
	public IoSession start(IoHandler handler, String host, int port) throws Exception {
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
		connector.setHandler(handler);
		// try to open a connection to the client
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
		connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
		return connectFuture.getSession();
	}

	/**
	 * Stops the client application
	 */
	public void stop() throws Exception {
		if (connector == null)
			return;
		connector.dispose();
		connector = null;
	}

}
