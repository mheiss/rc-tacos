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

    // Session information
    private String session;

    //flag to show the network status
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
     * Sets the username for this network session.
     * @param username the username to set
     */
    public void setSessionUsername(String username)
    {
        this.session = username;
    }

    /**
     *  Opens a connection to the primary server
     */
    public void connectNetwork()
    {
        if(NetSource.getInstance().initNetwork())
        {
            System.out.println("Connecting to server: "+NetSource.getInstance().getServerIp());
            //set the listener
            NetSource.getInstance().addNetEventListener(this);
            connected = NetSource.getInstance().connect();
        }
    }

    /**
     * Convenience method to registers the encoders and decoders.
     */
    public void registerEncoderAndDecoder()
    {
        //register the needed model types with the decoders and encoders
        ProtocolCodecFactory protFactory = ProtocolCodecFactory.getDefault();
        protFactory.registerDecoder(Item.ID, new ItemDecoder());
        protFactory.registerEncoder(Item.ID, new ItemEncoder());
        protFactory.registerDecoder(MobilePhoneDetail.ID, new MobilePhoneDecoder());
        protFactory.registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
        protFactory.registerDecoder(NotifierDetail.ID, new NotifierDecoder());
        protFactory.registerEncoder(NotifierDetail.ID, new NotifierEncoder());
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
    }

    /**
     * Returns the current status of the network connection
     * @return true if the network connection is established
     */
    public boolean getNetworkConnectionStatus()
    {
        return connected;
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
     */
    public void requestListing(String contentType)
    {
        sendMessage(contentType,IModelActions.LIST,null);
    }

    /**
     * Convenience method to send the message to the server.
     * @param contentType the type of the content.
     * @param queryString the type of the query
     * @param message the message to send
     */
    private void sendMessage(String contentType,String queryString,AbstractMessage message)
    {
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(
                session, 
                contentType, 
                queryString);
        System.out.println("Send: "+ session+","+contentType+","+queryString);
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        //wrapp into a list
        if(message != null)
            list.add(message);
        //encode the message
        String xmlMessage = factory.encode(list);
        //send the message
        NetSource.getInstance().sendMessage(xmlMessage);    
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
        xmlFactory.setupDecodeFactory(ne.getMessage());
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
        }
    }

    /**
     * Notification that the status of the socket changed.<br>
     * @param status the new status.
     **/
    @Override
    public void socketStatusChanged(MyClient client, int status)
    {
        System.out.println("Connection to the server lost: "+status);
        connected = false;
    }

    /**
     * Norification that the transfer of the data failed.
     * @param ne the triggered net event
     */
    @Override
    public void dataTransferFailed(NetEvent ne)
    {    
        System.out.println("failed to send the data:"+ne.getMessage());
    }
}
