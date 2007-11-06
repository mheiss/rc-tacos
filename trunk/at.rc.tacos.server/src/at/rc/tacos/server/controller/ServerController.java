package at.rc.tacos.server.controller;

//java
import java.util.*;
//net
import at.rc.tacos.core.net.internal.*;

public class ServerController 
{
    //the shared instance
    private static ServerController serverController;
    
    //manage the clients
    private Vector<MyClient> connectedClients;   

    //the server object
    private MyServer myServer = null;

    /**
     * Default class constructor, using the specified port
     * @param port the port number to listen to client requests
     */
    private ServerController()
    {
        //init the list
        connectedClients = new Vector<MyClient>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ServerController getDefault()
    {
        //create a new instance or reuse
        if (serverController == null)
            serverController = new ServerController();
        return serverController;
    }

    /**
     * Creates a new server in a own thread to handle 
     * incomming client connections.
     */
    public void startServer(int port)
    {
        //construct a Server object using the given port and this class as controller
        myServer = new MyServer(port);
        myServer.addNetListener(new ClientHandler());
        //start the server thread to listen to client connections
        Thread t = new Thread(myServer);
        t.start();
    }
    
    /**
     * Adds the client to the list of connected clients
     * @param client the client to add
     */
    public void clientConnected(MyClient client)
    {
        connectedClients.addElement(client);
    }
    
    /**
     * Removes the client from the list.
     * @param client the client to remove
     */
    public void clientDisconnected(MyClient client)
    {
        connectedClients.removeElement(client);
    }

    /**
     * Sends the given message to all authenticated clients
     * @param message the message to send
     */
    public synchronized void brodcastMessage(String message)
    {
        //loop over the clients
        for (MyClient client:connectedClients)
        {
            client.getSocket().sendMessage(message);
        }
    }
}
