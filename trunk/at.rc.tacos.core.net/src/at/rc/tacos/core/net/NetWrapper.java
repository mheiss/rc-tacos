package at.rc.tacos.core.net;

import java.util.*;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.core.net.event.*;
import at.rc.tacos.core.net.internal.*;

import at.rc.tacos.model.*;
import at.rc.tacos.common.*;
import at.rc.tacos.codec.*;
import at.rc.tacos.factory.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin implements INetListener
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.net";
	// The shared instance
	private static NetWrapper plugin;

	//properties
	private ClientSession clientSession;
	private ServerInfo connectionInfo;
	private boolean connected;

	/**
	 * The constructor
	 */
	public NetWrapper() { }

	/**
	 * Called when the plugin is started
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during startup
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
	}

	/**
	 * Called when the plugin is stopped
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during shutdown
	 */
	public void stop(BundleContext context) throws Exception 
	{
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static NetWrapper getDefault() 
	{
		if (plugin == null)
			plugin = new NetWrapper();
		return plugin;
	}

	/**
	 *  Opens a connection to the given server.
	 *  @param connectionName the name of the server to connect
	 */
	public void connectNetwork(String connectionName)
	{
		//get the server
		connectionInfo = NetSource.getInstance().getServerInfoById(connectionName);
		if(connectionInfo == null)
		{
			System.out.println("Cannot resolve server info: "+connectionName);
			//reset the info
			connected = false;
			return;
		}
		System.out.println("Connection to: "+connectionInfo.getHostName()+":"+connectionInfo.getPort());
		MyClient connection = NetSource.getInstance().getConnection(connectionInfo);
		//check the status
		if(connection != null)
		{
			//add this class as target for network messages
			connection.addNetListener(this);
			//create a new client session
			clientSession = new ClientSession(connection);
			//we have a connection
			connected = true;
		}
		else 
		{
			//not connected
			connected = false;
			//destroy the session
			clientSession = null;
			//destory the server info
			connectionInfo = null;
		}
	}

	/**
	 * Returns current client session used for the nework communication
	 * @param the client session
	 */
	public ClientSession getClientSession()
	{
		return clientSession;
	}
	
	/**
	 *  Returns the current status of the network connection
	 *  @return true if the network connection is established
	 */
	public boolean isConnected()
	{
		return connected;
	}
	
	/**
	 * Closes the current connection and cleanup.
	 */
	public void closeConnection()
	{
		//get the client session
		if(clientSession == null)
			return;
		//get the connection
		MyClient connection = clientSession.getConnection();
		//assert we have a connection
		if(connection == null)
			return;
		//close
		NetSource.getInstance().closeConnection(connection);
	}

	/**
	 * Convenience method to registers the encoders and decoders.
	 */
	public void registerEncoderAndDecoder()
	{
		//register the needed model types with the decoders and encoders
		ProtocolCodecFactory protFactory = ProtocolCodecFactory.getDefault();
		protFactory.registerDecoder(MobilePhoneDetail.ID, new MobilePhoneDecoder());
		protFactory.registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
		protFactory.registerDecoder(CallerDetail.ID, new CallerDecoder());
		protFactory.registerEncoder(CallerDetail.ID, new CallerEncoder());
		protFactory.registerDecoder(Patient.ID, new PatientDecoder());
		protFactory.registerEncoder(Patient.ID, new PatientEncoder());
		protFactory.registerDecoder(RosterEntry.ID, new RosterEntryDecoder());
		protFactory.registerEncoder(RosterEntry.ID, new RosterEntryEncoder());
		protFactory.registerDecoder(StaffMember.ID, new StaffMemberDecoder());
		protFactory.registerEncoder(StaffMember.ID, new StaffMemberEncoder());
		protFactory.registerDecoder(Transport.ID, new TransportDecoder());
		protFactory.registerEncoder(Transport.ID, new TransportEncoder());
		protFactory.registerDecoder(VehicleDetail.ID, new VehicleDecoder());
		protFactory.registerEncoder(VehicleDetail.ID, new VehicleEncoder()); 
		protFactory.registerDecoder(Login.ID, new LoginDecoder());
		protFactory.registerEncoder(Login.ID, new LoginEncoder());
		protFactory.registerDecoder(Logout.ID, new LogoutDecoder());
		protFactory.registerEncoder(Logout.ID, new LogoutEncoder());
		protFactory.registerDecoder(SystemMessage.ID, new SystemMessageDecoder());
		protFactory.registerEncoder(SystemMessage.ID, new SystemMessageEncoder());
		protFactory.registerDecoder(DialysisPatient.ID, new DialysisDecoder());
		protFactory.registerEncoder(DialysisPatient.ID, new DialysisEncoder());
		protFactory.registerDecoder(DayInfoMessage.ID, new DayInfoMessageDecoder());
		protFactory.registerEncoder(DayInfoMessage.ID, new DayInfoMessageEncoder());
        protFactory.registerDecoder(Job.ID, new JobDecoder());
        protFactory.registerEncoder(Job.ID, new JobEncoder());
        protFactory.registerDecoder(Location.ID, new LocationDecoder());
        protFactory.registerEncoder(Location.ID, new LocationEncoder());
        protFactory.registerDecoder(Competence.ID, new CompetenceDecoder());
        protFactory.registerEncoder(Competence.ID, new CompetenceEncoder());
        protFactory.registerDecoder(ServiceType.ID, new ServiceTypeDecoder());
        protFactory.registerEncoder(ServiceType.ID, new ServiceTypeEncoder());
        protFactory.registerDecoder(Disease.ID,new DiseaseDecoder());
        protFactory.registerEncoder(Disease.ID, new DiseaseEncoder());
        protFactory.registerDecoder(Address.ID, new AddressDecoder());
        protFactory.registerEncoder(Address.ID, new AddressEncoder());
	}

	// METHODS TO SEND MESSAGES
	/**
	 * Sends a request to the server to login the user.
	 * @param login the authentication information to use
	 */
	public void sendLoginMessage(Login login)
	{
		sendMessage(Login.ID, IModelActions.LOGIN, login);
	}
	
	/**
	 * Sends a request to the server to logout the user.
	 * @param logout the logut message
	 */
	public void sendLogoutMessage(Logout logout)
	{
		sendMessage(Logout.ID, IModelActions.LOGOUT, logout);
	}

	/**
	 * Sends a request to the server to add the object to the database.<br>
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the add message contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param addMessage the object to add
	 */
	public void sendAddMessage(String contentType,AbstractMessage addMessage)
	{
		sendMessage(contentType,IModelActions.ADD,addMessage);
	}
	
	public void sendAddAllMessage(String contentType,List<AbstractMessage> addList)
	{
		sendMessage(contentType,IModelActions.ADD_ALL,null,addList);
	}

	/**
	 * Sends a request to the server to remove the object from the database.
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the remove request contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param removeMessage the object to remove
	 */
	public void sendRemoveMessage(String contentType,AbstractMessage removeMessage)
	{
		sendMessage(contentType,IModelActions.REMOVE,removeMessage);
	}

	/**
	 * Sends a request to the server to update the object in the database.
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the update request contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param updateMessage the object to update
	 */
	public void sendUpdateMessage(String contentType,AbstractMessage updateMessage)
	{
		sendMessage(contentType,IModelActions.UPDATE,updateMessage);
	}

	/**
	 * Sends a listing request for the given object to the server.<br>
	 * To identify the type of the listing request a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the lisitng request is for <code>RosterEntry</code> objects.
	 * @param contentType the type of the listing request
	 * @param filter the filter for the query
	 */
	public void requestListing(String contentType,QueryFilter filter)
	{
		sendMessage(contentType,IModelActions.LIST,filter,null);
	}

	/**
	 * Convenience method to send the message to the server.
	 * @param contentType the type of the content.
	 * @param queryString the type of the query
	 * @param queryFilter the filter to apply
	 * @param message the message to send
	 */
	private void sendMessage(String contentType,String queryString,AbstractMessage message)
	{
		//create a new list
		List<AbstractMessage> messageList = new ArrayList<AbstractMessage>();
		messageList.add(message);
		sendMessage(contentType, queryString, null, messageList);
	}
	
	/**
	 * Convenience method to send the message to the server.<br />
	 * ASSERT: The messages in the list MUST have the same type!.
	 * @param contentType the type of the content.
	 * @param queryString the type of the query
	 * @param queryFilter the filter to apply
	 * @param messageList the messages to send
	 */
	private void sendMessage(String contentType,String queryString,QueryFilter queryFilter,List<AbstractMessage> messageList)
	{
		//check if we have a session
		if(clientSession == null)
		{
			System.out.println("Failed to send the message");
			System.out.println("No client session available to send the message");
			return;
		}
		//check if we have a connection
		if(clientSession.getConnection() == null)
		{
			System.out.println("Failed to send the message");
			System.out.println("No connection to a server available");
			return;
		}
		
		//set up the factory
		XMLFactory factory = new XMLFactory();
		factory.setupEncodeFactory(
				clientSession.getUsername(), 
				contentType, 
				queryString);
		System.out.println("Send: "+clientSession.getUsername()+","+contentType+","+queryString);
		//appply filter if we have one
		if(queryFilter != null)
			factory.setFilter(queryFilter);
		//encode the message
		String xmlMessage = factory.encode(messageList);
		
		//replace all new lines
		xmlMessage = xmlMessage.replaceAll("\\s\\s+|\\n|\\r", "<![CDATA[<br/>]]>");
		
		//get the connection out of the session and send the message
		MyClient connection = clientSession.getConnection();
		connection.sendMessage(xmlMessage);
	}

	// LISTENER METHODS
	/**
	 * Notification that new data is available.<br>
	 * @param ne the triggered net event
	 */
	@Override
	public void dataReceived(NetEvent ne)
	{
		//set up the factory to decode
		XMLFactory xmlFactory = new XMLFactory();
		String message = ne.getMessage().replaceAll("&lt;br/&gt;", "\n");
		xmlFactory.setupDecodeFactory(message);
		//decode the message
		ArrayList<AbstractMessage> objects = xmlFactory.decode();
		//get the type of the item
		final String contentType = xmlFactory.getContentType();
		final String queryString = xmlFactory.getQueryString();
		final String userId = xmlFactory.getUserId();
		System.out.println("Received: "+ userId+","+contentType+","+queryString);

		//try to get a listener for this message
		ListenerFactory listenerFactory = ListenerFactory.getDefault();
		if(listenerFactory.hasListeners(contentType))
		{
			IModelListener listener = listenerFactory.getListener(contentType);
			//now pass the message to the listener
			if(IModelActions.ADD.equalsIgnoreCase(queryString))
				listener.add(objects.get(0));
			if(IModelActions.ADD_ALL.endsWith(queryString))
				listener.addAll(objects);
			if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
				listener.remove(objects.get(0));
			if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
				listener.update(objects.get(0));
			if(IModelActions.LIST.equalsIgnoreCase(queryString))
				listener.list(objects);
			if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
				listener.loginMessage(objects.get(0));
			if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
				listener.logoutMessage(objects.get(0));
			if(IModelActions.SYSTEM.equalsIgnoreCase(queryString))
				listener.systemMessage(objects.get(0));
		}
		else
		{
			System.out.println("No listener found for the message type: "+contentType);
			IModelListener listener = listenerFactory.getListener("sessionManager");
			listener.transferFailed(contentType,queryString,objects.get(0));
		}
	}

	/**
	 * Notification that the status of the socket changed.<br>
	 * @param status the new status.
	 **/
	@Override
	public void socketStatusChanged(MyClient client, int status)
	{
		//reset the connection status
		if(status == IConnectionStates.STATE_DISCONNECTED)
		{
			//not connected
			connected = false;
			//destroy the session
			clientSession = null;
			//destory the server info
			connectionInfo = null;
		}
		//inform the listeners
		ListenerFactory listenerFactory = ListenerFactory.getDefault();
		IModelListener listener = listenerFactory.getListener("sessionManager");
		listener.connectionChange(status);
	}

	/**
	 * Norification that the transfer of the data failed.
	 * @param ne the triggered net event
	 */
	@Override
	public void dataTransferFailed(NetEvent ne)
	{    
		System.out.println("failed to send the data:"+ne.getMessage());
		//decode the message
		XMLFactory xmlFactory = new XMLFactory();
		xmlFactory.setupDecodeFactory(ne.getMessage());
		//inform the listeners
		ListenerFactory listenerFactory = ListenerFactory.getDefault();
		IModelListener listener = listenerFactory.getListener("sessionManager");
		listener.transferFailed(
				xmlFactory.getContentType(),
				xmlFactory.getQueryString(),
				xmlFactory.decode().get(0));
	}
}
