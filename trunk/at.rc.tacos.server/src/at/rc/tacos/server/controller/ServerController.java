package at.rc.tacos.server.controller;

import java.util.*;
import java.util.Map.Entry;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.model.*;
import at.rc.tacos.codec.*;
import at.rc.tacos.factory.*;
import at.rc.tacos.server.listener.*;

public class ServerController 
{
    //the shared instance
    private static ServerController serverController;

    //this pool contains all connections that are not authenticated
    private List<MyClient> connClientPool;  
    //authenticated clients (loged in clients)
    private Map<String,MyClient> authClientPool;

    /**
     * Default private class constructor.<br>
     * Setting up the client pools and registers the listener 
     * and codec classes.
     */
    private ServerController()
    {
        //init the pools
        connClientPool = new ArrayList<MyClient>();
        authClientPool = new HashMap<String, MyClient>();
        //register the encoders and decoders
        registerEncoderAndDecoder();
        registerModelListeners();
    }

    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ServerController getDefault()
    {
        //create a new instance or reuse
        if (serverController == null)
            serverController = new ServerController();
        return serverController;
    }

    /**
     * Invoked when a new client is connected to the server.<br>
     * The client will be put into the connected client pool 
     * and cannot interact with other clients until the authentication
     * is successfully.
     * @param client the connected client
     */
    public void clientConnected(MyClient client)
    {
        connClientPool.add(client);
    }

    /**
     * Invoked when a client was disconnected throught abnormal 
     * programm termination or a netowrk fault.<br>
     * If the client was authenticated the other authenticated clients
     * will be informed with a <code>SystemMessage</code>
     * @param client the disconnected client
     */
    public void clientDisconnected(MyClient client)
    {

        //check the connClientPool
        if(connClientPool.contains(client))
            connClientPool.remove(client);
        //check the authClientPool
        if(!authClientPool.containsValue(client))
            return;
        
        String clientId = "";
        //loop and serarch for the id
        for(Entry<String,MyClient> entry:authClientPool.entrySet())
        {
            //check the client
            if(client == entry.getValue())
            {
                clientId = entry.getKey();
                //create a SystemMessage to inform the other clients
                SystemMessage message = new SystemMessage("Client "+clientId +" disconnected");
                ArrayList<AbstractMessage> objectList = new ArrayList<AbstractMessage>();
                objectList.add(message);
                //brodcast the message to all authenticated clients
                brodcastMessage(
                        "systen",
                        SystemMessage.ID,
                        IModelActions.NOTIFY,
                        objectList);
                //TODO: logout from the database
            }
        }
        //remove the client
        authClientPool.remove(clientId);
    }

    /**
     *  Authentication of the client was successfully, the client
     *  will be moved to the authenticated client pool.
     *  @param userId the identification of the authenticated user
     *  @param client the client connection
     */
    public void setAuthenticated(String userId,MyClient client)
    {
        //remove the client form the conClientPool
        connClientPool.remove(client);
        //add to the authenticated pool
        authClientPool.put(userId, client);
    }

    /**
     * Returns the authentication id of the client object
     * @param client the client connection
     * @return the identification string
     */
    public String getAuthenticationString(MyClient client)
    {
        //loop and serarch for the id
        for(Entry<String,MyClient> entry:authClientPool.entrySet())
        {
            //check the client
            if(client == entry.getValue())
                return entry.getKey();
        }
        //nothing found
        return null;
    }

    /**
     * Returns wheter or not the given client is authenticated or not
     * @return true if the client is authenticated
     */
    public boolean isAuthenticated(MyClient client)
    {
        return authClientPool.containsValue(client);
    }

    /**
     *  Encodes the message into xml and brodcasts the message all
     *  authenticated clients.
     *  @param userId the identification of the user.
     *  @param type the content type
     *  @param action the action to invoke at the client
     *  @param objectList a list of objects to send
     */
    public synchronized void brodcastMessage(String userId,String type,String action, ArrayList<AbstractMessage> objectList)
    {
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(userId,new Date().getTime(),type,action);
        String message = factory.encode(objectList);
        //loop over the client pool and send the message
        for(MyClient client:authClientPool.values())   
            client.sendMessage(message);
    }

    /**
     *  Encodes the message into xml and sends the message to the client.
     *  @param userId the identification of the target client.
     *  @param type the content type
     *  @param action the action to invoke at the client
     *  @param objectList a list of objects to send
     */
    public synchronized void sendMessage(String userId,String type,String action, ArrayList<AbstractMessage> objectList)
    {
        //get the client socket to send the message
        if(authClientPool.containsKey(userId))
        {
            MyClient client = authClientPool.get(userId);
            //set up the factory
            XMLFactory factory = new XMLFactory();
            factory.setupEncodeFactory(userId,new Date().getTime(),type,action);
            //encode and send
            client.sendMessage(factory.encode(objectList));
        }       
    }

    /**
     * Convenience method to registers the encoders and decoders.
     */
    private void registerEncoderAndDecoder()
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
     * Convinience method to registers the ui model listeners 
     * to get updates from the network layer.
     */
    private void registerModelListeners()
    {
        ServerListenerFactory factory = ServerListenerFactory.getInstance();
        //register the listeners
        factory.registerModelListener(Item.ID, new ItemListener());
        factory.registerModelListener(MobilePhoneDetail.ID, new MobilePhoneListener());
        factory.registerModelListener(NotifierDetail.ID, new NotifyDetailListener());
        factory.registerModelListener(Patient.ID, new PatientListener());
        factory.registerModelListener(RosterEntry.ID, new RosterEntryListener());
        factory.registerModelListener(StaffMember.ID, new StaffMemberListener());
        factory.registerModelListener(Transport.ID, new TransportListener());
        factory.registerModelListener(VehicleDetail.ID, new VehicleDetailListener());
        factory.registerModelListener(Login.ID, new AuthenticationListener());
        factory.registerModelListener(Logout.ID, new AuthenticationListener());
    }
}
