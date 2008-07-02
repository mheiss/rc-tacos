package at.rc.tacos.server.net.handler;

import java.util.List;

import org.eclipse.core.runtime.Status;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.IServerListener;
import at.rc.tacos.common.Message;
import at.rc.tacos.factory.ServerListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerContext;

/**
 * This handler is responsible for handling the request to the server
 * @author Michael
 */
public class MessageHandler 
{	
	//the properties of the handler
	private String xmlData;
	
	/**
	 * Default class constructor passing the xmlData to proccess
	 */
	public MessageHandler(String xmlData)
	{
		this.xmlData = xmlData;
	}
	
	/**
	 *  Handles the request
	 */
	public void handleRequest() throws Exception
	{
		//decode the received data
		XMLFactory xmlFactory = new XMLFactory();
		Message receivedMessage = xmlFactory.decode(xmlData);
		
		//now handle the request
		final String contentType = receivedMessage.getContentType();
		final String queryString = receivedMessage.getQueryString();
		final String sequenceId = receivedMessage.getSequenceId();
		final QueryFilter queryFilter = receivedMessage.getQueryFilter();
		final List<AbstractMessage> messageList = receivedMessage.getMessageList();

		//the factory and listeners
		final ServerListenerFactory listenerFactory = ServerListenerFactory.getInstance();
		final IServerListener listener = listenerFactory.buildListener(contentType);
		
		//the client is not authenticated, no login request -> not accepted
		if(!ServerContext.getCurrentInstance().getSession().isAuthenticated() &! Login.ID.equalsIgnoreCase(contentType))
			throw new IllegalStateException("Client not authenticated, login first");

		//do we have a handler?
		if(listener == null)
			throw new UnsupportedOperationException("No listener found for the content type "+contentType);
		
		//setup a new default message object for the response
		Message responseMessage = new Message();
		responseMessage.setSequenceId(sequenceId);
		responseMessage.setUsername(ServerContext.getCurrentInstance().getSession().getUsername());
		responseMessage.setContentType(contentType);
		responseMessage.setQueryString(queryString);
		
		//login request
		if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
		{
			//get the result message
			Login loginResult = (Login)listener.handleLoginRequest(messageList.get(0));
			//add the client to the authenticated list
			if(loginResult.isLoggedIn())
			{
				ServerContext.getCurrentInstance().getSession().setLogin(loginResult);
				NetWrapper.log("Login successful for user "+loginResult.getUsername(),Status.OK,null);
			}
			//add the response to the list of messages
			responseMessage.addMessage(loginResult);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
		//logout request
		if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
		{
			Logout logoutResult = (Logout)listener.handleLogoutRequest(messageList.get(0));
			//remove the client form the list of authenticated clients if successfully
			if(logoutResult.isLoggedOut())
			{
				NetWrapper.log("Logout from user: "+logoutResult.getUsername()+". Have a nice day :)",Status.OK,null);
				ServerContext.getCurrentInstance().release();
			}
			return;
		}
		
		//add request
		if(IModelActions.ADD.equalsIgnoreCase(queryString))
		{
			AbstractMessage resultAddMessage = listener.handleAddRequest(messageList.get(0));
			//add the response to the list of messages
			responseMessage.addMessage(resultAddMessage);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
		//add all request
		if(IModelActions.ADD_ALL.equalsIgnoreCase(queryString))
		{
			List<AbstractMessage> resultAddList = listener.handleAddAllRequest(messageList);
			//add the response to the list of messages
			responseMessage.setMessageList(resultAddList);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
		//remove request
		if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
		{
			AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(messageList.get(0));
			//add the response to the list of messages
			responseMessage.addMessage(resultRemoveMessage);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
		//update request
		if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
		{
			AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(messageList.get(0));
			//add the response to the list of messages
			responseMessage.addMessage(resultUpdateMessage);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
		//listing request
		if(IModelActions.LIST.equalsIgnoreCase(queryString))
		{
			List<AbstractMessage> resultMessageList = listener.handleListingRequest(queryFilter);
			//add the response to the list of messages
			responseMessage.setMessageList(resultMessageList);
			ServerContext.getCurrentInstance().addMessage(responseMessage);
			return;
		}
	}
}
