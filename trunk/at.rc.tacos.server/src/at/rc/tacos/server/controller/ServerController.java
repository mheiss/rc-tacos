package at.rc.tacos.server.controller;

//java
import java.util.*;
//net
import at.rc.tacos.codec.ItemDecoder;
import at.rc.tacos.codec.ItemEncoder;
import at.rc.tacos.codec.MobilePhoneDecoder;
import at.rc.tacos.codec.MobilePhoneEncoder;
import at.rc.tacos.codec.NotifierDecoder;
import at.rc.tacos.codec.NotifierEncoder;
import at.rc.tacos.codec.PatientDecoder;
import at.rc.tacos.codec.PatientEncoder;
import at.rc.tacos.codec.RosterEntryDecoder;
import at.rc.tacos.codec.RosterEntryEncoder;
import at.rc.tacos.codec.StaffMemberDecoder;
import at.rc.tacos.codec.StaffMemberEncoder;
import at.rc.tacos.codec.TransportDecoder;
import at.rc.tacos.codec.TransportEncoder;
import at.rc.tacos.codec.VehicleDecoder;
import at.rc.tacos.codec.VehicleEncoder;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class ServerController 
{
    //the shared instance
    private static ServerController serverController;
    
    //manage the clients
    private Vector<MyClient> connectedClients;   

    //the server object
    private MyServer myServer = null;

    /**
     * Default class constructor, using the specified port
     * @param port the port number to listen to client requests
     */
    private ServerController()
    {
        //init the list
        connectedClients = new Vector<MyClient>();
        //register the encoders and decoders
        registerEncoderAndDecoder();
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
     * Creates a new server in a own thread to handle 
     * incomming client connections.
     */
    public void startServer(int port)
    {
        //construct a Server object using the given port and this class as controller
        myServer = new MyServer(port);
        myServer.addNetListener(new ClientHandler());
        //start the server thread to listen to client connections
        Thread t = new Thread(myServer);
        t.start();
    }
    
    /**
     * Adds the client to the list of connected clients
     * @param client the client to add
     */
    public void clientConnected(MyClient client)
    {
        connectedClients.addElement(client);
    }
    
    /**
     * Removes the client from the list.
     * @param client the client to remove
     */
    public void clientDisconnected(MyClient client)
    {
        connectedClients.removeElement(client);
    }

    /**
     * Sends the given message to all authenticated clients
     * @param message the message to send
     */
    public synchronized void brodcastMessage(String message)
    {
        //loop over the clients
        for (MyClient client:connectedClients)
            client.getSocket().sendMessage(message);
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
    }
}
