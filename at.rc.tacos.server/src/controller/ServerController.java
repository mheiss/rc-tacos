package controller;

//java
import java.util.*;

//net
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.core.net.event.*;

public class ServerController implements INetListener
{
    //manage the clients
    private Vector<MyClient> connectedClients;   

    //the server object
    private MyServer myServer = null;

    //the port for the Server
    private int port;

    /**
     * Default class constructor, using the specified port
     * @param port the port number to listen to client requests
     */
    public ServerController(int port)
    {
        //the port that the server should listen to
        this.port = port;
        //init the list
        connectedClients = new Vector<MyClient>();
    }

    /**
     * Creates a new server in a own thread to handle 
     * incomming client connections.
     */
    public void startServer()
    {
        //construct a Server object using the given port and this class as controller
        myServer = new MyServer(port);
        myServer.addNetListener(this);
        //start the server thread to listen to client connections
        Thread t = new Thread(myServer);
        t.start();
    }

    /**
     * Notifcation that new data has arrived
     */
    @Override
    public synchronized void dataReceived(NetEvent ne)
    {
//        MyClient client = getClientBySocket(ne.getSocket());
//        //parse the message
//        XMLParser xml = XMLParser.getInstance();
//        RCNet net = xml.parseHeader(ne.getMessage());

//      //get the request
//      if (net.getAction() == RCNet.ACTION_LOGIN)
//      {
//      //parse the login data
//      //send response
//      client.setAuthenticated(true);
//      }
//      else
//      {
//      //check if the client is authenticated
//      if (!client.isAuthenticated())
//      {
//      //TODO: send info to client -> login required
//      return;
//      }

//      //parse the action and go on :)
//      }
        //test
        brodcastMessage(ne.getMessage());
    }

    /**
     * Notification that the status of the socket changed.<br>
     * <p>A new <code>MyClient</code> object with the connected socket will be created.<br>
     * @param status the new status of the socket.
     **/
    @Override
    public void socketStatusChanged(MyClient client, int status)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void dataTransferFailed(NetEvent ne)
    {
        
        
    }

    /**
     * Sends the given message to all authenticated clients
     * @param message the message to send
     */
    public void brodcastMessage(String message)
    {
        //loop over the clients
        for (MyClient client:connectedClients)
        {
            //if (client.isAuthenticated())
            client.getSocket().sendMessage(message);
        }
    }
}
