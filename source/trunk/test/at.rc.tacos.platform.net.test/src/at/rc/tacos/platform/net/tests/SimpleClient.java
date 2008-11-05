package at.rc.tacos.platform.net.tests;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import at.rc.tacos.platform.net.XmlCodecFactory;

public class SimpleClient {

    // timeout for the future operations
    public static final int CONNECT_TIMEOUT = 3000;

    // properties for the session
    private String host;
    private int port;

    // the session for the communication
    private IoSession session;

    public SimpleClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(IoHandler handler) throws Exception {
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
        connector.setHandler(handler);
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
        connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
        session = connectFuture.getSession();
    }

    public IoSession getSession() {
        return session;
    }

    public void disconnect() {
        if (session != null) {
            session.close().awaitUninterruptibly(CONNECT_TIMEOUT);
            session = null;
        }
    }
}
