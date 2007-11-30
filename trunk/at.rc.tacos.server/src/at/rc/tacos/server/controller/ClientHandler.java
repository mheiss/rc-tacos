package at.rc.tacos.server.controller;

import java.util.ArrayList;
import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.factory.*;
import at.rc.tacos.model.Login;
import at.rc.tacos.common.*;

/**
 * Handles the communication with a client
 * @author Michael
 */
public class ClientHandler implements INetListener
{
    @Override
    public synchronized void dataReceived(NetEvent ne)
    {
        //set up the factory to decode
        XMLFactory xmlFactory = new XMLFactory();
        xmlFactory.setupDecodeFactory(ne.getMessage());
        //decode the message
        ArrayList<AbstractMessage> objects = xmlFactory.decode();
        //get the type of the item
        final String type = xmlFactory.getType(); 
        final String action = xmlFactory.getAction();
        
        //check if the client is authenticated or not
        final boolean isAuthenticated = ServerController.getDefault().isAuthenticated(ne.getClient());
                
        //the listener class to handle the request
        ServerListenerFactory factory = ServerListenerFactory.getInstance();
        IServerListener listener = null;
        String clientId = null;
        
        //the client is not authenticated
        if(!isAuthenticated)
        {
            //login request are permitted
            if(Login.ID.equalsIgnoreCase(type))
                listener = factory.getListener(Login.ID);
        }
        //the client is authenticated
        else
        {
            //the identification of the client
            clientId = ServerController.getDefault().getAuthenticationString(ne.getClient());
            listener = factory.getListener(type);                
        }
        //now handle the request
        if(listener != null)
            listener.handleRequest(clientId, action, objects);
    }

    @Override
    public synchronized void dataTransferFailed(NetEvent ne)
    {
        System.out.println("Failed to send the message to the client");
        System.out.println("Message: "+ne.getMessage());
        System.out.println("Client: "+ne.getClient());
    }

    @Override
    public synchronized void socketStatusChanged(MyClient client, int status)
    {
        //check the status
        if (status == IConnectionStates.STATE_CONNECTED)
        {
            System.out.println("New client connected");
            //create the streams and start the receive thread
            client.connect();
            //set the listener for netEvents
            client.addNetListener(this);
            //add the client to the list
            ServerController.getDefault().clientConnected(client);
        }
        if (status == IConnectionStates.STATE_DISCONNECTED)
        {
            System.out.println("Client quit");
            ServerController.getDefault().clientDisconnected(client);
        }
    }
}
