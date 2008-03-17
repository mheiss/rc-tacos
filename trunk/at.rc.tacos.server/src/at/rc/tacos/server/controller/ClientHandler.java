package at.rc.tacos.server.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.factory.*;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.util.MyUtils;
import at.rc.tacos.common.*;

/**
 * Handles the communication with a client
 * @author Michael
 */
public class ClientHandler implements INetListener
{
    //the logger
    private static Logger logger = Logger.getLogger(ClientHandler.class);
	
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

		logger.debug("QUERY from "+userId+": "+contentType+"->"+queryString);

		//the client is not authenticated, no login request -> not accepted
		if(!session.isAuthenticated() &! Login.ID.equalsIgnoreCase(contentType))
		{
			logger.warn("Client not authenticated, login first");
			SystemMessage system = new SystemMessage("Client not authenticated, login first",SystemMessage.TYPE_INFO);
			server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,system);
			return;
		}

		//do we have a handler?
		if(listener == null)
		{
			logger.error("No listener found for the message type: "+contentType);
			//notify the sender
			SystemMessage sysMess = new SystemMessage("No listener found for the message type: "+contentType,SystemMessage.TYPE_INFO);
			server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,sysMess);
			return;
		}
		try
		{
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
				server.clientDisconnected(session.getConnection());
				server.sendMessage(session, contentType, queryString, logoutResult);
			}
			//add request
			else if(IModelActions.ADD.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultAddMessage = listener.handleAddRequest(objects.get(0));
				//send the added item to all connected clients
				server.brodcastMessage(userId, contentType, queryString, resultAddMessage);
				//if we have a web client -> send the request back
				if(session.isWebClient())
					server.sendMessage(session, contentType, queryString, resultAddMessage); 
			}
			//add all request
			else if(IModelActions.ADD_ALL.equalsIgnoreCase(queryString))
			{
				List<AbstractMessage> resultAddList = listener.handleAddAllRequest(objects);
				//send the added item to all connected clients
				server.brodcastMessage(userId, contentType, queryString, resultAddList);
				//if we have a web client -> send the request back
				if(session.isWebClient())
					server.sendMessage(session, contentType, queryString, resultAddList); 
			}
			//remove request
			else if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(objects.get(0));
				//send the removed item
				server.brodcastMessage(userId, contentType, queryString, resultRemoveMessage);
				//if we have a web client -> send the request back
				if(session.isWebClient())
					server.sendMessage(session, contentType, queryString, resultRemoveMessage);
			}
			//update request
			else if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(objects.get(0));
				//send the updated item
				server.brodcastMessage(userId, contentType, queryString, resultUpdateMessage);
				//if we have a web client -> send the request back
				if(session.isWebClient())
					server.sendMessage(session, contentType, queryString, resultUpdateMessage);
			}
			else if(IModelActions.LIST.equalsIgnoreCase(queryString))
			{
				long startQuery = Calendar.getInstance().getTimeInMillis();
				List<AbstractMessage> resultMessageList = listener.handleListingRequest(queryFilter);
				long endQuery = Calendar.getInstance().getTimeInMillis();
				logger.info("OPERATION TIME for "+contentType +" listing: "+MyUtils.timestampToString(endQuery-startQuery, new SimpleDateFormat("mm:ss"))+ "("+(endQuery-startQuery)+"ms)");
				//send the listing
				server.sendMessage(session, contentType, queryString, resultMessageList);
			}
			else
			{   
				logger.error("No handler found for queryString: "+queryString);
				SystemMessage sysMes = new SystemMessage("No handler found for queryString: "+queryString,SystemMessage.TYPE_ERROR);
				server.sendMessage(session,SystemMessage.ID,IModelActions.SYSTEM,sysMes);
			}  
		}
		//catch all sql errors that occured during the operations with the listener classes
		catch(SQLException sqle)
		{
			logger.error("SQL-Error: "+sqle.getMessage(),sqle);
			SystemMessage system = new SystemMessage("SQL-Error:"+sqle.getMessage(),SystemMessage.TYPE_ERROR);
			server.sendMessage(session, SystemMessage.ID, IModelActions.SYSTEM, system);
		}
		//catch all error during the operations with the dao listener classes
		catch(DAOException daoe)
		{
			logger.error("DAO-Exception: "+daoe.getMessage(),daoe);
			SystemMessage system = new SystemMessage(daoe.getMessage(),SystemMessage.TYPE_ERROR);
			server.sendMessage(session, SystemMessage.ID, IModelActions.SYSTEM, system);  
		}
	}

	@Override
	public synchronized void dataTransferFailed(NetEvent ne)
	{
		logger.error("Failed to send the message to the client: "+ne.getClient());
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
