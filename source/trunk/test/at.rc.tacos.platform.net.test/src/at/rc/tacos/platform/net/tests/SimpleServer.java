package at.rc.tacos.platform.net.tests;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import at.rc.tacos.platform.net.XmlCodecFactory;
import at.rc.tacos.platform.net.request.Message;

/**
 * Simple mina echo server implementation for tests
 * 
 * @author mheiss
 */
public class SimpleServer {

	// server properties
	private int port;
	private IoAcceptor acceptor;

	/**
	 * Default class constructor to create a new server
	 * 
	 * @param port
	 *            the port to listen to
	 */
	public SimpleServer(int port) {
		this.port = port;
	}

	/**
	 * Start listening
	 */
	public void listen() throws Exception {
		acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
		acceptor.setHandler(new EchoHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.bind(new InetSocketAddress(port));
	}

	public void close() throws Exception {
		acceptor.unbind();
		acceptor.dispose();
	}

	protected class EchoHandler implements IoHandler {

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			Message request = (Message) message;
			System.out.println(request.getId());
			System.out.println(request.getCommand());
			System.out.println(request.getParams());
			System.out.println(request.getObjects());
			session.write(message);
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
			throwable.printStackTrace();
		}

		@Override
		public void messageSent(IoSession session, Object object) throws Exception {
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
		}

		@Override
		public void sessionCreated(IoSession session) throws Exception {
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
		}
	}

}
