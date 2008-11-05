package at.rc.tacos.platform.net.tests;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import at.rc.tacos.platform.net.XmlCodecFactory;

/**
 * Simple mina server implementation for tests
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
    public void listen(IoHandler handler) throws Exception {
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.setHandler(handler);
        acceptor.bind(new InetSocketAddress(port));
    }

    public void close() throws Exception {
        acceptor.unbind();
        acceptor.dispose();
    }

    public void setHandler(IoHandler handler) {
        acceptor.setHandler(handler);
    }
}
