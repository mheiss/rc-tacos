package at.rc.tacos.server.controller;

import java.util.*;
import java.util.Map.Entry;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.model.*;
import at.rc.tacos.codec.*;
import at.rc.tacos.factory.*;
import at.rc.tacos.server.dao.DaoService;
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
        //prepare and add the dummy data
        DaoService.getInstance().initData();
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
        System.out.println("Adding new client to pool");
        connClientPool.add(client);
    }

    /**
     * Invoked when a client was disconnected throught abnormal 
     * programm termination or a network fault.<br>
     * If the client was authenticated the other authenticated clients
     * will be informed with a <code>SystemMessage</code>
     * @param client the disconnected client
     */
    public void clientDisconnected(MyClient client)
    {
        //check the connClientPool
        if(connClientPool.contains(client))
        {
            System.out.println("Removing client from pool");
            connClientPool.remove(client);
            return;
        }
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
                //remove the client
                authClientPool.remove(clientId);
                //brodcast the message to all other authenticated clients
                brodcastMessage(
                        "systen",
                        SystemMessage.ID,
                        IModelActions.SYSTEM,
                        new SystemMessage("Client "+clientId +" disconnected"));
            }
        }

    }

    /**
     *  Authentication of the client was successfully, the client
     *  will be moved to the authenticated client pool.
     *  @param userId the identification of the authenticated user
     *  @param client the client connection
     */
    public void setAuthenticated(String userId,MyClient client)
    {
        System.out.println("Adding "+ userId+" to the authenticated pool");
        //remove the client form the conClientPool
        connClientPool.remove(client);
        //add to the authenticated pool
        authClientPool.put(userId, client);
    }

    /**
     * Logout of the client was successfully, the client will be removed
     * from the authenticated pool.
     * @param userId the identification of the user who logged out
     */
    public void setDeAuthenticated(String userId)
    {
        authClientPool.remove(userId);
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
     * Returns wheter or not the given userid has already a open connection.
     * @param userId the username of the connection to check
     * @return true if a connection has been found
     */
    public boolean hasOpenConnections(String userId)
    {
        if(authClientPool.containsKey(userId))
            return true;
        else
            return false;
    }

    /**
     * Returns wheter or not the given client is authenticated or not
     * @param client the client connection to check
     * @return true if the client is authenticated
     */
    public boolean isAuthenticated(MyClient client)
    {
        return authClientPool.containsValue(client);
    }

    /**
     *  Encodes the list of message into xml and brodcasts the message all
     *  authenticated clients.
     *  @param userId the identification of the user.
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param objectList a list of objects to send
     */
    public synchronized void brodcastMessage(String userId,String contentType,String queryString, ArrayList<AbstractMessage> objectList)
    {
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(userId,contentType,queryString);
        String message = factory.encode(objectList);
        //loop over the client pool and send the message
        for(MyClient client:authClientPool.values()) 
        {
            client.sendMessage(message);
            System.out.println("Sending brodcast reply for "+userId+" : "+contentType+"->"+queryString);
        }
    }

    /**
     *  Encodes the message into xml and brodcasts the message all
     *  authenticated clients.
     *  @param userId the identification of the user.
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param object the message to send
     */
    public synchronized void brodcastMessage(String userId,String contentType,String queryString, AbstractMessage object)
    {
        //create list
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.add(object);
        //delegate
        brodcastMessage(userId, contentType, queryString, list);
    }

    /**
     *  Encodes the message into xml and sends the message to the client.
     *  @param userId the identification of the target client.
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param objectList a list of objects to send
     */
    public synchronized void sendMessage(String userId,String contentType,String queryString, ArrayList<AbstractMessage> objectList)
    {
        //get the client socket to send the message
        if(authClientPool.containsKey(userId))
        {
            MyClient client = authClientPool.get(userId);
            //set up the factory
            XMLFactory factory = new XMLFactory();
            factory.setupEncodeFactory(userId,contentType,queryString);
            //encode and send
            client.sendMessage(factory.encode(objectList));
            System.out.println("Sending reply for "+userId+" : "+contentType+"->"+queryString);
        }   
        else
            System.out.println("Failed to get the client connection for: "+userId);
    }

    /**
     *  Encodes the message into xml and sends the message to the client.
     *  @param userId the identification of the target client.
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param object the message to send
     */
    public synchronized void sendMessage(String userId,String contentType,String queryString, AbstractMessage object)
    {
        //create list
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.add(object);
        //delegate
        sendMessage(userId, contentType, queryString, list);
    }
    
    /**
     * Sends a message to the given client object.<br>
     * This method should only be used to communicate with
     * unregistered clients.
     * @param client the client to send the message to
     * @param contentType the type of the content
     * @param queryString the query string to send
     * @param message the message to send
     */
    public synchronized void sendMessage(MyClient client,String contentType,String queryString,AbstractMessage message)
    {
        //create list
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.add(message);
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory("server",contentType,queryString);
        //send the message
        client.sendMessage(factory.encode(list));
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
        factory.registerModelListener(CallerDetail.ID, new NotifyDetailListener());
        factory.registerModelListener(Patient.ID, new PatientListener());
        factory.registerModelListener(RosterEntry.ID, new RosterEntryListener());
        factory.registerModelListener(StaffMember.ID, new StaffMemberListener());
        factory.registerModelListener(Transport.ID, new TransportListener());
        factory.registerModelListener(VehicleDetail.ID, new VehicleDetailListener());
        factory.registerModelListener(Login.ID, new AuthenticationListener());
        factory.registerModelListener(Logout.ID, new AuthenticationListener());
    }
}
