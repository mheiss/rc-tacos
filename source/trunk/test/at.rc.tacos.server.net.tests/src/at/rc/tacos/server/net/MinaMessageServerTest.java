package at.rc.tacos.server.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageClient;
import at.rc.tacos.platform.net.mina.ServerContext;
import at.rc.taocs.server.ServerContextImpl;

public class MinaMessageServerTest {

	// the message server
	private MinaMessageServer server;

	@Before
	public void setup() throws Exception {
		// the server context
		ServerContext context = new ServerContextImpl(8080);
		context.getDataSource().open();

		// create the server and statup
		server = new MinaMessageServer();
		server.start(context);
	}

	@Test
	public void connectTest() throws Exception {
		MessageClient client = new MessageClient();
		IoSession session = client.start(new IoHandlerAdapter(), "localhost", 8080);

		// send a request
		long start = System.currentTimeMillis();
		Login login = new Login("test", "test", false);
		ExecMessage<Login> loginMessage = new ExecMessage<Login>("login", login);
		Message<Login> response = loginMessage.synchronRequest(session);
		long end = System.currentTimeMillis();

		System.out.println("Request took:" + (end - start) + " ms");
		System.out.println("Received: " + response.getObjects().get(0).getUsername());
		System.out.println("logged in: " + response.getObjects().get(0).isLoggedIn());

		// shutdown the client
		client.stop();
	}

	@Test
	public void connectTest1() throws Exception {
		MessageClient client = new MessageClient();
		IoSession session = client.start(new IoHandlerAdapter(), "localhost", 8080);

		// send a request
		long start = System.currentTimeMillis();
		GetMessage<Transport> transportRequest = new GetMessage<Transport>(new Transport());
		transportRequest.addParameter(IFilterTypes.TRANSPORT_TODO_FILTER, "");
		// wait and get the response
		Message<Transport> response = transportRequest.synchronRequest(session);
		long end = System.currentTimeMillis();

		System.out.println("Request took:" + (end - start) + " ms");
		System.out.println("Size: " + response.getObjects().size());
		System.out.println("Received: " + response.getObjects().get(0).getTransportId());

		// shutdown the client
		client.stop();
	}

	@After
	public void shutdown() {
		server.stop();
	}

}
