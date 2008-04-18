package at.rc.tacos.server.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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
		final String sequenceId = xmlFactory.getSequenceId();
		final String contentType = xmlFactory.getContentType();
		final String queryString = xmlFactory.getQueryString();
		final QueryFilter queryFilter = xmlFactory.getQueryFilter();

		//the server controller,
		final ServerController server = ServerController.getDefault();
		final ServerListenerFactory factory = ServerListenerFactory.getInstance();
		final IServerListener listener = factory.getListener(contentType);

		//the client connection
		final ClientSession session = server.getSession(ne.getClient());
		
		//send back the keep alive package
		if(IModelActions.KEEP_ALIVE.equalsIgnoreCase(queryString))
		{
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.KEEP_ALIVE);
			server.sendMessage(session, info);
			return;
		}

		//the client is not authenticated, no login request -> not accepted
		if(!session.isAuthenticated() &! Login.ID.equalsIgnoreCase(contentType))
		{
			logger.warn("Client not authenticated, login first");
			SystemMessage system = new SystemMessage("Client not authenticated, login first",SystemMessage.TYPE_INFO);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			server.sendMessage(session,info);
			return;
		}

		//do we have a handler?
		if(listener == null)
		{
			logger.error("No listener found for the message type: "+contentType);
			//notify the sender
			SystemMessage system = new SystemMessage("No listener found for the message type: "+contentType,SystemMessage.TYPE_INFO);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			server.sendMessage(session,info);
			return;
		}
		try
		{
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(contentType);
			info.setQueryString(queryString);
			
			//login request
			if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
			{
				//get the result message
				Login loginResult = (Login)listener.handleLoginRequest(objects.get(0));
				//add the client to the authenticated list
				if(loginResult.isLoggedIn())
					session.setAuthenticated(loginResult.getUsername(),loginResult.isWebClient());
				//send back
				info.setMessage(loginResult);
				server.sendMessage(session,info);
				return;
			}
			//logout request
			if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
			{
				Logout logoutResult = (Logout)listener.handleLogoutRequest(objects.get(0));
				//remove the client form the list of authenticated clients if successfully
				if(logoutResult.isLoggedOut())
					session.setDeAuthenticated();
				server.clientDisconnected(session.getConnection());
				//send back to the client
				info.setMessage(logoutResult);
				server.sendMessage(session,info);
				return;
			}
			//add request
			if(IModelActions.ADD.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultAddMessage = listener.handleAddRequest(objects.get(0));
				info.setMessage(resultAddMessage);
				server.brodcastMessage(session,info);
				return;
			}
			//add all request
			if(IModelActions.ADD_ALL.equalsIgnoreCase(queryString))
			{
				List<AbstractMessage> resultAddList = listener.handleAddAllRequest(objects);
				info.setMessageList(resultAddList);
				server.brodcastMessage(session,info);
				return;
			}
			//remove request
			if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(objects.get(0));
				info.setMessage(resultRemoveMessage);
				server.brodcastMessage(session,info);
				return;
			}
			//update request
			if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(objects.get(0));
				info.setMessage(resultUpdateMessage);
				server.brodcastMessage(session,info);
				return;
			}
			//listing request
			if(IModelActions.LIST.equalsIgnoreCase(queryString))
			{
				List<AbstractMessage> resultMessageList = listener.handleListingRequest(queryFilter);
				info.setMessageList(resultMessageList);
				//send back to the client
				server.sendMessage(session, info);
				return;
			}
		}
		//catch all sql errors that occured during the operations with the listener classes
		catch(SQLException sqle)
		{
			logger.error("SQL-Error: "+sqle.getMessage(),sqle);
			SystemMessage system = new SystemMessage("SQL-Error:"+sqle.getMessage(),SystemMessage.TYPE_ERROR);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			server.sendMessage(session,info);
		}
		//catch all error during the operations with the dao listener classes
		catch(DAOException daoe)
		{
			logger.error("DAO-Exception: "+daoe.getMessage(),daoe);
			SystemMessage system = new SystemMessage(daoe.getMessage(),SystemMessage.TYPE_ERROR);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			server.sendMessage(session,info);  
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
