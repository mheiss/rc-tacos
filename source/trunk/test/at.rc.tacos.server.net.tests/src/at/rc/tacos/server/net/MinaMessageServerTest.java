package at.rc.tacos.server.net;

import org.apache.mina.core.session.IoSession;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.GetMessage;

public class MinaMessageServerTest extends BaseTestcase {

	@BeforeClass
	public static void setup() throws Exception {
		startServer();
		startClient();
	}

	@Test
	public void connectTest() throws Exception {
		IoSession session = getSession();

		// send a request
		long start = System.currentTimeMillis();
		Login login = new Login("test", "test", false);
		ExecMessage<Login> loginMessage = new ExecMessage<Login>("login", login);
		Message<Login> response = loginMessage.synchronRequest(session);
		long end = System.currentTimeMillis();

		System.out.println("Request took:" + (end - start) + " ms");
		System.out.println("Received: " + response.getObjects().get(0).getUsername());
		System.out.println("logged in: " + response.getObjects().get(0).isLoggedIn());

	}

	@Test
	public void connectTest1() throws Exception {
		IoSession session = getSession();

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
	}

	@AfterClass
	public static void shutdown() throws Exception {
		stopClient();
		stopServer();
	}

}
