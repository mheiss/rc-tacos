package at.rc.tacos.server.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.mina.MessageClient;
import at.rc.tacos.platform.net.mina.ServerContext;
import at.rc.taocs.server.ServerContextImpl;

/**
 * Provides a server and a client implementation to test the message handlers.
 * 
 * @author Michael
 */
public class BaseTestcase {

	// the message server
	private static MinaMessageServer server;
	private static MessageClient client;
	private static IoSession session;

	public static void startServer() throws Exception {
		// the server context
		ServerContext context = new ServerContextImpl(8080);
		context.getDataSource().open();

		// create the server and statup
		server = new MinaMessageServer();
		server.start(context);
	}

	public static void startClient() throws Exception {
		client = new MessageClient();
		session = client.start(new IoHandlerAdapter(), "localhost", 8080);
	}

	public static IoSession getSession() {
		return session;
	}

	public static void stopServer() throws Exception {
		server.stop();
	}

	public static void stopClient() throws Exception {
		client.stop();
	}

}
