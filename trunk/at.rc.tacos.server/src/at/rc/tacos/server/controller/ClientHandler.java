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
            else
                System.out.println("Client not authenticated, login first");
        }
        //the client is authenticated
        else
        {
            //the identification of the client
            clientId = ServerController.getDefault().getAuthenticationString(ne.getClient());
            System.out.println(type + "->"+action + " request from user "+clientId);
            listener = factory.getListener(type);                
        }
        //now handle the request
        if(listener != null)
        {
            //login request
            if(IModelActions.LOGIN.equalsIgnoreCase(action))
                listener.handleLogin(objects.get(0));
            //logout request
            else if(IModelActions.LOGOUT.equalsIgnoreCase(action))
                listener.handleLogout(objects.get(0));
            //add request
            else if(IModelActions.ADD.equalsIgnoreCase(action))
                listener.handleAddRequest(objects.get(0));
            //remove request
            else if(IModelActions.REMOVE.equalsIgnoreCase(action))
                listener.handleRemoveRequest(objects.get(0));
            //update request
            else if(IModelActions.UPDATE.equalsIgnoreCase(action))
                listener.handleUpdateRequest(objects.get(0));
            else if(IModelActions.LIST.equalsIgnoreCase(action))
                listener.handleListingRequest();
            else
                System.out.println("No action handler found for action type: "+action);
        }
        else
            System.out.println("No listener found for the message type: "+type);
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
