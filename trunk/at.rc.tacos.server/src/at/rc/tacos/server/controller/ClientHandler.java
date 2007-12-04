package at.rc.tacos.server.controller;

import java.util.ArrayList;
import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.factory.*;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
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
        ServerController server = ServerController.getDefault();
        ServerListenerFactory factory = ServerListenerFactory.getInstance();
        IServerListener listener = null;
        String userId = null;
        
        //the client is not authenticated
        if(!isAuthenticated)
        {
            //login request are permitted
            if(Login.ID.equalsIgnoreCase(type))
                listener = factory.getListener(Login.ID);
            else
            {
                System.out.println("Client not authenticated, login first");
                //notify the sender
                server.sendSystemMessage(ne.getClient(), "Client not authenticated, login first");
            }
        }
        //the client is authenticated
        else
        {
            //the identification of the client
            userId = ServerController.getDefault().getAuthenticationString(ne.getClient());
            System.out.println(type + "->"+action + " request from user "+userId);
            listener = factory.getListener(type);                
        }
        //now handle the request
        if(listener != null)
        { 
            //login request
            if(IModelActions.LOGIN.equalsIgnoreCase(action))
            {
                //get the result message
                Login loginResult = (Login)listener.handleLoginRequest(objects.get(0));
                //add the client to the authenticated list
                if(loginResult.isLoggedIn())
                    ServerController.getDefault().setAuthenticated(
                            loginResult.getUsername(), 
                            ne.getClient());
                //send the response message
                server.sendMessage(userId, type, action, loginResult);
            }
            //logout request
            else if(IModelActions.LOGOUT.equalsIgnoreCase(action))
            {
                Logout logoutResult = (Logout)listener.handleLogoutRequest(objects.get(0));
                //remove the client form the list of authenticated clients
                if(logoutResult.isLoggedOut())
                    ServerController.getDefault().setDeAuthenticated(
                            logoutResult.getUsername());
                //send the response message
                server.sendMessage(userId, type, action, logoutResult);
            }
            //add request
            else if(IModelActions.ADD.equalsIgnoreCase(action))
            {
                AbstractMessage resultAddMessage = listener.handleAddRequest(objects.get(0));
                //send the added item
                server.brodcastMessage(userId, type, action, resultAddMessage);
            }
            //remove request
            else if(IModelActions.REMOVE.equalsIgnoreCase(action))
            {
                AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(objects.get(0));
                //send the removed item
                server.brodcastMessage(userId, type, action, resultRemoveMessage);
            }
            //update request
            else if(IModelActions.UPDATE.equalsIgnoreCase(action))
            {
                AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(objects.get(0));
                //send the updated item
                server.brodcastMessage(userId, type, action, resultUpdateMessage);
            }
            else if(IModelActions.LIST.equalsIgnoreCase(action))
            {
                ArrayList<AbstractMessage> resultMessageList = listener.handleListingRequest();
                //send the listing
                server.brodcastMessage(userId, type, action, resultMessageList);
            }
            else
            {   
                System.out.println("No action handler found for action type: "+action);
                //notify the sender
                server.sendSystemMessage(ne.getClient(), "No action handler found for action type: "+action);
            }
        }
        else
        {
            System.out.println("No listener found for the message type: "+type);
            //notify the sender
            server.sendSystemMessage(ne.getClient(), "No listener found for the message type: "+type);
        }      
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
