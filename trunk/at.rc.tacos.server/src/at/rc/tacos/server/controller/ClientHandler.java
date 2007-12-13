package at.rc.tacos.server.controller;

import java.util.ArrayList;
import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.factory.*;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SystemMessage;
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
        //set up the factory and decode decode
        XMLFactory xmlFactory = new XMLFactory();
        xmlFactory.setupDecodeFactory(ne.getMessage());
        ArrayList<AbstractMessage> objects = xmlFactory.decode();

        //get the type of the item
        final String contentType = xmlFactory.getContentType();
        final String queryString = xmlFactory.getQueryString();
        final String userId = xmlFactory.getUserId();
        final QueryFilter queryFilter = xmlFactory.getQueryFilter();
        //check if the client is authenticated or not
        final boolean isAuthenticated = ServerController.getDefault().isAuthenticated(ne.getClient());

        //the listener class to handle the request
        ServerController server = ServerController.getDefault();
        ServerListenerFactory factory = ServerListenerFactory.getInstance();
        IServerListener listener = factory.getListener(contentType);

        System.out.println("Got a new request from "+userId+" to handle: "+contentType + "->"+queryString);  
        
        //the client is not authenticated, no login request -> not accepted
        if(!isAuthenticated &! Login.ID.equalsIgnoreCase(contentType))
        {
            System.out.println("Client not authenticated, login first");
            SystemMessage system = new SystemMessage("Client not authenticated, login first");
            server.sendMessage(ne.getClient(),SystemMessage.ID,IModelActions.SYSTEM,system);
            return;
        }
        
        //do we have a handler?
        if(listener == null)
        {
            System.out.println("No listener found for the message type: "+contentType);
            //notify the sender
            SystemMessage sysMess = new SystemMessage("No listener found for the message type: "+contentType);
            server.sendMessage(userId,SystemMessage.ID,IModelActions.SYSTEM,sysMess);
            return;
        }

        //login request
        if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
        {
            //get the result message
            Login loginResult = (Login)listener.handleLoginRequest(objects.get(0));
            //add the client to the authenticated list
            if(loginResult.isLoggedIn())
            {
                //check if the user is already logged in?
                if(server.hasOpenConnections(userId))
                {
                    //send a logout message
                    Logout logout = new Logout(userId);
                    logout.setErrorMessage("Dieser Account wird auf einem anderen Computer benutzt.");
                    server.sendMessage(userId, Logout.ID, IModelActions.LOGOUT, logout);
                }
                //authenticate the user with the new socket
                ServerController.getDefault().setAuthenticated(
                        loginResult.getUsername(), 
                        ne.getClient());
                //notify the sender that the login was successfully
                server.sendMessage(userId, contentType, queryString, loginResult);
            }
            else
            {
                //notify the sender that the login failed
                server.sendMessage(ne.getClient(),contentType,queryString,loginResult);
            }
        }
        //logout request
        else if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
        {
            Logout logoutResult = (Logout)listener.handleLogoutRequest(objects.get(0));
            server.sendMessage(userId, contentType, queryString, logoutResult);
            //remove the client form the list of authenticated clients if successfully
            if(logoutResult.isLoggedOut())
                ServerController.getDefault().setDeAuthenticated(logoutResult.getUsername());
        }
        //add request
        else if(IModelActions.ADD.equalsIgnoreCase(queryString))
        {
            AbstractMessage resultAddMessage = listener.handleAddRequest(objects.get(0));
            //send the added item
            server.brodcastMessage(userId, contentType, queryString, resultAddMessage);
        }
        //remove request
        else if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
        {
            AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(objects.get(0));
            //send the removed item
            server.brodcastMessage(userId, contentType, queryString, resultRemoveMessage);
        }
        //update request
        else if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
        {
            AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(objects.get(0));
            //send the updated item
            server.brodcastMessage(userId, contentType, queryString, resultUpdateMessage);
        }
        else if(IModelActions.LIST.equalsIgnoreCase(queryString))
        {
            try
            {
                ArrayList<AbstractMessage> resultMessageList = listener.handleListingRequest(queryFilter);
                //send the listing
                server.sendMessage(userId, contentType, queryString, resultMessageList);
            }
            catch(Exception e)
            {
                SystemMessage system = new SystemMessage("Error while listing of "+contentType+" entries: \n"+e.getMessage());
                server.sendMessage(userId, contentType, queryString, system);  
            }
        }
        else
        {   
            System.out.println("No handler found for queryString: "+queryString);
            SystemMessage sysMes = new SystemMessage("No handler found for queryString: "+queryString);
            server.sendMessage(userId,SystemMessage.ID,IModelActions.SYSTEM,sysMes);
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
            //create the streams and start the receive thread
            client.connect();
            //set the listener for netEvents
            client.addNetListener(this);
            //add the client to the list
            ServerController.getDefault().clientConnected(client);
        }
        if (status == IConnectionStates.STATE_DISCONNECTED)
        {
            ServerController.getDefault().clientDisconnected(client);
        }
    }
}
