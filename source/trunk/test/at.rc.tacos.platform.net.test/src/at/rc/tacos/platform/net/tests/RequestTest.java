package at.rc.tacos.platform.net.tests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.net.Command;
import at.rc.tacos.platform.net.request.AddMessage;
import at.rc.tacos.platform.net.request.Message;

/**
 * Simple test to send a request to the server
 * 
 * @author mheiss
 */
public class RequestTest {

	// client and server to test
	private SimpleServer server;
	private SimpleClient client;

	@Before
	public void setup() throws Exception {
		// create and setup a new listening server
		server = new SimpleServer(8080);
		server.listen();

		// connect to the server
		client = new SimpleClient("localhost", 8080);
		client.connect();
	}

	@Test
	public void syncTest() throws Exception {
		AddMessage request = new AddMessage(new String("Hallo"));
		request.asnchronRequest(client.getSession());
	}

	@Test
	public void asyncTest() throws Exception {
		// setup a new syncron request and wait for the response
		AddMessage request = new AddMessage(new String("Hallo"));
		Message response = request.synchronRequest(client.getSession());

		// validate the response
		Assert.assertNotNull(response);
		Assert.assertEquals(Command.ADD, response.getCommand());
		Assert.assertEquals(1, response.getObjects().size());
		Assert.assertEquals("Hallo", response.getObjects().get(0));
	}

	@After
	public void tearDown() throws Exception {
		client.disconnect();
		server.close();
	}

}
