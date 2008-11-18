package at.rc.tacos.platform.net.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.Request;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * The <code>MessageClient</code> class is used to send {@link Message} objects to the server.
 * <p>
 * After a connection with the remote server has been established the {@link IoSession} can be used
 * to setup message instances like {@link AddMessage}, {@link UpdateMessage}, {@link RemoveMessage},
 * {@link GetMessage} or {@link ExecMessage} and send a {@link Request} to the server.
 * </p>
 * The response is either handled synchron by the {@link Request#synchronRequest(IoSession)} method
 * or asynchronous by the {@link IoHandler} instance. </p>
 * 
 * @author Michael
 */
public class MessageClient {

    // socket where to connect
    private final InetSocketAddress address;

    // the connector
    private NioSocketConnector connector;
    private IoSession session;
    private ExecutorService filterExecutor = new OrderedThreadPoolExecutor();

    /**
     * Creates a <code>MessageClient</code> instance.
     * 
     * @param hostname
     *            the name or ip address of the remote server
     * @param port
     *            the portnumber
     * 
     * @throws Exception
     *             when the hostname or port argument is not valid
     * @see InetSocketAddress for details about the thrown exceptions
     */
    public MessageClient(String hostname, int port) throws Exception {
        this(new InetSocketAddress(hostname, port));
    }

    /**
     * Creates a <code>MessageClient</code> instance.
     * 
     * @param address
     *            the remote address where to connect
     */
    public MessageClient(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Initializes the {@link NioSocketConnector} and opens a connection to the remote host.
     * <p>
     * This method will block the current thread and wait until a connection has been established.
     * When no connection has been established after {@link Request#TIMEOUT} (default to
     * <i>3000ms</i>) then a {@link TimeoutException} will be thrown.
     * </p>
     * After a connection
     */
    public void connect(IoHandler handler) throws Exception {
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(filterExecutor));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new XmlCodecFactory()));
        connector.setHandler(handler);

        // try to open a connection to the server
        ConnectFuture connectFuture = connector.connect(address);
        connectFuture.awaitUninterruptibly(Request.TIMEOUT);
        session = connectFuture.getSession();
    }

    /**
     * Reconnects to the server using the created {@link NioSocketConnector} instance after a
     * session has been terminated.
     * <p>
     * Plase note that this method <b> cannot </b> be called bevor a previous connection with
     * {@link MessageClient#connector} has been established or after the connection has been
     * manually terminated using {@link MessageClient#disconnect()}
     * </p>
     * 
     * @throws IllegalStateException
     *             if the connector is not valid
     */
    public void reconnect() {
        // assert we have a connector
        if (connector == null) {
            throw new IllegalStateException("The connector is not valid, cannot reconnect");
        }
        // try to open a connection to the server
        ConnectFuture connectFuture = connector.connect(address);
        connectFuture.awaitUninterruptibly(Request.TIMEOUT);
        session = connectFuture.getSession();
    }

    /**
     * Returns the current {@link IoSession} accociated with this instance.
     * <p>
     * This method will throw a {@link IllegalStateException} if no connection has been established
     * or the current session has been terminated.
     * </p>
     * 
     * @return the current session
     * @throws IllegalStateException
     *             if no connection has been established or the current session has been terminated
     */
    public IoSession getSession() throws IllegalStateException {
        if (connector == null || session == null)
            throw new IllegalStateException("Cannot get a session befor connectiong to a server");
        if (!session.isConnected()) {
            throw new IllegalStateException("The current session has been terminated");
        }
        return session;
    }

    /**
     * Returns true if a connection has been established.
     * 
     * @return true if we have a connection otherwise false.
     */
    public boolean isConnected() {
        // assert valid connector and session
        if (connector == null || session == null)
            return false;
        return session.isConnected();
    }

    /**
     * Closes the current connection to the server and terminates the current session.
     * <p>
     * All subsequent calls to {@link MessageClient#getSession()} will throw a
     * {@link IllegalStateException}.
     * </p>
     */
    public void disconnect() throws Exception {
        // terminate the session
        if (session != null) {
            session.closeOnFlush();
            session = null;
        }
        // release and close the connector
        if (connector != null) {
            connector.dispose();
            connector = null;
        }
    }

}
