package at.rc.tacos.platform.net.request;

import junit.framework.Assert;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.net.request.AbstractMessage;
import at.rc.tacos.platform.net.request.AddMessage;
import at.rc.tacos.platform.net.request.LoginMessage;
import at.rc.tacos.platform.net.request.Message;

/**
 * Simple test to send a request to the server
 * 
 * @author mheiss
 */
public class EchoRequestTest {

    // client and server to test
    private SimpleServer server;
    private SimpleClient client;

    @Before
    public void setup() throws Exception {
        // create and setup a new listening server
        server = new SimpleServer(8080);
        server.listen(new ServerEchoHandler());

        // connect to the server
        client = new SimpleClient("localhost", 8080);
        client.connect(new ClientHandler());
    }

    @Test
    public void syncTest() throws Exception {
        AddMessage request = new AddMessage(new String("Hallo"));
        request.asnchronRequest(client.getSession());

        // wait for some time
        Thread.sleep(100);

        // get the received message
        ClientHandler handler = (ClientHandler) client.getSession().getHandler();
        Message response = handler.getRequest();

        // check the received message
        Assert.assertNotNull(response);
        Assert.assertEquals(request.getId(), response.getId());
        Assert.assertEquals(1, response.getObjects().size());
        Assert.assertEquals("Hallo", response.getObjects().get(0));
    }

    @Test
    public void asyncTest() throws Exception {
        // setup a new syncron request and wait for the response
        AddMessage request = new AddMessage(new String("Hallo"));
        Message response = request.synchronRequest(client.getSession());

        // validate the response
        Assert.assertNotNull(response);
        Assert.assertEquals(request.getId(), response.getId());
        Assert.assertEquals(1, response.getObjects().size());
        Assert.assertEquals("Hallo", response.getObjects().get(0));
    }

    @Test
    public void loginTest() throws Exception {
        // setup a new login message
        LoginMessage login = new LoginMessage("michael", "password", false);
        login.asnchronRequest(client.getSession());
    }

    @After
    public void tearDown() throws Exception {
        client.disconnect();
        server.close();
    }

    /**
     * Handler for the server, will echo back the received message
     */
    protected class ServerEchoHandler extends IoHandlerAdapter {

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            AbstractMessage request = (AbstractMessage) message;
            System.out.println("Request received:" + request);
            System.out.println("Request class:" + request.getClass());
            request.asnchronRequest(session);
            System.out.println("sending response to client");
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
            throwable.printStackTrace();
        }
    }

    /**
     * Handler for the client, will store the received message
     */
    protected class ClientHandler extends IoHandlerAdapter {

        private AbstractMessage request;

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            // store the message
            request = (AbstractMessage) message;
        }

        public AbstractMessage getRequest() {
            return request;
        }
    }
}
