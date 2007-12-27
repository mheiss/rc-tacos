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
        final String userId = xmlFactory.getUserId();
        final String contentType = xmlFactory.getContentType();
        final String queryString = xmlFactory.getQueryString();
        final QueryFilter queryFilter = xmlFactory.getQueryFilter();
        
        //the server controller,
        final ServerController server = ServerController.getDefault();
        final ServerListenerFactory factory = ServerListenerFactory.getInstance();
        final IServerListener listener = factory.getListener(contentType);
        
        //the client connection
        final ClientSession session = server.getSession(ne.getClient());
        System.out.println("Got a new request from "+userId+" to handle: "+contentType + "->"+queryString);  
        
        //the client is not authenticated, no login request -> not accepted
        if(!session.isAuthenticated() &! Login.ID.equalsIgnoreCase(contentType))
        {
            System.out.println("Client not authenticated, login first");
            SystemMessage system = new SystemMessage("Client not authenticated, login first");
            server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,system);
            return;
        }
        
        //do we have a handler?
        if(listener == null)
        {
            System.out.println("No listener found for the message type: "+contentType);
            //notify the sender
            SystemMessage sysMess = new SystemMessage("No listener found for the message type: "+contentType);
            server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,sysMess);
            return;
        }

        //login request
        if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
        {
            //get the result message
            Login loginResult = (Login)listener.handleLoginRequest(objects.get(0));
            //add the client to the authenticated list
            if(loginResult.isLoggedIn())
                session.setAuthenticated(loginResult.getUsername(),loginResult.isWebClient());
            //notify the sender about the result
            server.sendMessage(session,contentType,queryString,loginResult);
        }
        //logout request
        else if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
        {
            Logout logoutResult = (Logout)listener.handleLogoutRequest(objects.get(0));
            //remove the client form the list of authenticated clients if successfully
            if(logoutResult.isLoggedOut())
                session.setDeAuthenticated();
            server.sendMessage(session, contentType, queryString, logoutResult);
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
                server.sendMessage(session, contentType, queryString, resultMessageList);
            }
            catch(Exception e)
            {
                SystemMessage system = new SystemMessage("Error while listing of "+contentType+" entries: "+e.getMessage());
                e.printStackTrace();
                server.sendMessage(session, SystemMessage.ID, IModelActions.SYSTEM, system);  
            }
        }
        else
        {   
            System.out.println("No handler found for queryString: "+queryString);
            SystemMessage sysMes = new SystemMessage("No handler found for queryString: "+queryString);
            server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,sysMes);
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
