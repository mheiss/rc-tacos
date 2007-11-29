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
     */
    public void start(BundleContext context) throws Exception 
    {
        super.start(context);
        plugin = this;
    }

    /**
     * Called when the plugin is stopped
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
        //set the listener
        NetSource.getInstance().addNetEventListener(this);
        if(NetSource.getInstance().initNetwork())
            connected = NetSource.getInstance().connect();
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
        //system events
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
     * Sends a request to the server to add 
     * the message to the database.
     * @param addMessage the object to add
     */
    public void sendAddMessage(AbstractMessage addMessage)
    {
        sendMessage(addMessage, IModelListener.ADD);
    }

    /**
     * Sends a request to the server to remove
     * the message from the database.
     * @param removeMessage the object to remove
     */
    public void sendRemoveMessage(AbstractMessage removeMessage)
    {
        sendMessage(removeMessage, IModelListener.REMOVE);
    }

    /**
     * Sends a request to the server to update
     * the message in the database.
     * @param updateMessage the object to update
     */
    public void sendUpdateMessage(AbstractMessage updateMessage)
    {
        sendMessage(updateMessage,IModelListener.UPDATE);
    }

    /**
     * Sends a listing request for the given message to the server.<br>
     * Example:<br>
     * To get a listing of all <b>roster entries</b> set the id to <code>RosterEntry.ID</code>
     * @param id the type of the listing request
     */
    public void requestListing(String id)
    {
        sendMessage(null,IModelListener.LIST);
    }

    /**
     * Convenience method to send the message to the server.
     * @param message the message to send
     * @param type the type of the message
     */
    private void sendMessage(AbstractMessage message,String type)
    {
        long now = new Date().getTime();
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(
                session, 
                now, 
                AbstractMessage.ID, 
                type);
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
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
        final String type = xmlFactory.getType(); 
        final String action = xmlFactory.getAction();

        //try to get a listener for this message
        ListenerFactory listenerFactory = ListenerFactory.getDefault();
        if(listenerFactory.hasModelListeners(type))
        {
            IModelListener listener = listenerFactory.getModelListener(type);
            //now pass the message to the listener
            if(IModelListener.ADD.equalsIgnoreCase(action))
                listener.add(objects.get(0));
            if(IModelListener.REMOVE.equalsIgnoreCase(action))
                listener.remove(objects.get(0));
            if(IModelListener.UPDATE.equalsIgnoreCase(action))
                listener.update(objects.get(0));
            if(IModelListener.LIST.equalsIgnoreCase(action))
                listener.list(objects);
        }
        if(listenerFactory.hasEventListeners(type))
        {
            IEventListener listener = listenerFactory.getEventListener(type);
            if(IEventListener.LOGIN.equalsIgnoreCase(action))
                listener.loginMessage(objects.get(0));
            if(IEventListener.LOGOUT.equalsIgnoreCase(action))
                listener.logoutMessage(objects.get(0));
            if(IEventListener.NOTIFY.equalsIgnoreCase(action))
                listener.statusMessage(objects.get(0));
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
