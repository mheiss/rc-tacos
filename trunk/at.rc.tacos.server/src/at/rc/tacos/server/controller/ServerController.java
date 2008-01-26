package at.rc.tacos.server.controller;

import java.sql.SQLException;
import java.util.*;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.model.*;
import at.rc.tacos.codec.*;
import at.rc.tacos.factory.*;
import at.rc.tacos.server.listener.*;

public class ServerController 
{
    //the shared instance
    private static ServerController serverController;

    //this pool contains all client connections form the application
    private List<ClientSession> connClientPool;

    /**
     * Default private class constructor.<br>
     * Setting up the client pools and registers the listener 
     * and codec classes.
     */
    private ServerController()
    {
        //init the pools
        connClientPool = new ArrayList<ClientSession>();
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
     * A new session will be created, the connection must first be
     * authenticated to interact with the server.
     * @param client the new conneted client
     */
    public void clientConnected(MyClient client)
    {
        System.out.println("Creating new session");
        ClientSession session = new ClientSession(client);
        connClientPool.add(session);
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
        //get the user session
        ClientSession session = getSession(client);
        
        System.out.println("Disconnect detected ( "+session+" ) ");

        //check the pool
        if(connClientPool.contains(session))
        {
            connClientPool.remove(session);
            //brodcast the message to all other authenticated clients
            brodcastMessage("system",SystemMessage.ID, IModelActions.SYSTEM,
                    new SystemMessage("Client "+session.getUsername() +" disconnected"));
        }
    }

    /**
     * Returns the session object for this client connection
     * @param connection the connection to get the session from
     * @return the session object or null if no session is available
     */
    public ClientSession getSession(MyClient connection)
    {
        for(ClientSession session:connClientPool)
        {
            if(session.getConnection() == connection)
                return session;
        }
        return null;
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
        for(ClientSession session:connClientPool)
        {
            //Send the message to all authenticated clients, except the web clients
            if(session.isAuthenticated() &! session.isWebClient())
            {
                MyClient client = session.getConnection();
                client.sendMessage(message);
                System.out.println("Sending reply to "+session.getUsername()+" -> "+userId+" : "+contentType+"->"+queryString);
            }
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
     *  @param session the session to send the message to
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param objectList a list of objects to send
     */
    public synchronized void sendMessage(ClientSession session,String contentType,String queryString, ArrayList<AbstractMessage> objectList)
    {
        MyClient connection = session.getConnection();
        String userId = session.getUsername();
        //When we have no user id then set it to system
        if(userId == null)
            userId = "system";
        //set up the factory
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(userId,contentType,queryString);
        String xml = factory.encode(objectList);
        //encode and send
        connection.sendMessage(xml);
        System.out.println("Sending reply "+userId+" : "+contentType+"->"+queryString);
    }

    /**
     *  Encodes the message into xml and sends the message to the client.
     *  @param session the session to send the message to
     *  @param contentType the type of the message content
     *  @param queryString the type of the query
     *  @param object the message to send
     */
    public synchronized void sendMessage(ClientSession session,String contentType,String queryString, AbstractMessage object)
    {
        //create list
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.add(object);
        //delegate
        sendMessage(session, contentType, queryString, list);
    }

    /**
     * Convenience method to registers the encoders and decoders.
     */
    private void registerEncoderAndDecoder()
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
        factory.registerModelListener(MobilePhoneDetail.ID, new MobilePhoneListener());
        factory.registerModelListener(CallerDetail.ID, new NotifyDetailListener());
        factory.registerModelListener(RosterEntry.ID, new RosterEntryListener());
        factory.registerModelListener(StaffMember.ID, new StaffMemberListener());
        factory.registerModelListener(Transport.ID, new TransportListener());
        factory.registerModelListener(VehicleDetail.ID, new VehicleDetailListener());
        factory.registerModelListener(Login.ID, new AuthenticationListener());
        factory.registerModelListener(Logout.ID, new AuthenticationListener());
        factory.registerModelListener(DialysisPatient.ID, new DialysisPatientListener());
        factory.registerModelListener(DayInfoMessage.ID, new DayInfoListener());
        factory.registerModelListener(Job.ID, new JobListener());
        factory.registerModelListener(Location.ID, new LocationListener());
        factory.registerModelListener(Competence.ID, new CompetenceListener());
        factory.registerModelListener(ServiceType.ID, new ServiceTypeListener());
    }
}
