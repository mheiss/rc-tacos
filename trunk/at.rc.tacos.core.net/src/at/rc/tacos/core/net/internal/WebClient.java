package at.rc.tacos.core.net.internal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.codec.ItemDecoder;
import at.rc.tacos.codec.ItemEncoder;
import at.rc.tacos.codec.LoginDecoder;
import at.rc.tacos.codec.LoginEncoder;
import at.rc.tacos.codec.LogoutDecoder;
import at.rc.tacos.codec.LogoutEncoder;
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
import at.rc.tacos.codec.SystemMessageDecoder;
import at.rc.tacos.codec.SystemMessageEncoder;
import at.rc.tacos.codec.TransportDecoder;
import at.rc.tacos.codec.TransportEncoder;
import at.rc.tacos.codec.VehicleDecoder;
import at.rc.tacos.codec.VehicleEncoder;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * The web client to communicate with the server.<br>
 * Provides convienient methods to prevent the encoding and decoding of the objects
 * from and to xml.<br>
 * The class provides methods to send an receive objects. 
 * @author Michael
 */
public class WebClient
{
    //the shared instance
    private static WebClient instance;

    //connection to the server
    private MySocket socket;
    //the factory
    private XMLFactory factory;

    //result of the decode
    private String userId;
    private String contentType;
    private String queryString;
    private long timestamp;

    /**
     * Default class constructr
     */
    private WebClient() 
    { 
        //initialize the codec factory
        registerEncoderAndDecoder();
        factory = new XMLFactory();
    }

    /**
     * Returns the shared instance of the client
     * @return the instance
     */
    public static WebClient getInstance()
    {
        if( instance == null)
            instance = new WebClient();
        return instance;
    }

    /**
     * Connects to a given server address.
     * @param serverAddress the host name or ip address of the remote host
     * @param serverPort the port number to connect to
     */
    public boolean connect(String serverAddress,int serverPort)
    {
        try
        {
            socket = new MySocket(serverAddress,serverPort);
            socket.createInputStream();
            socket.createOutputStream(); 
            return true;
        }
        catch(UnknownHostException uhe)
        {
            System.out.println("Cannot resole the host name "+serverAddress);
            return false;
        }
        catch (IOException ioe)
        {
            System.out.println("Error cannot connect to the server "+serverAddress+":"+serverPort);
            return false;
        }
    }
    
    /**
     * Closes the socket and ends the communication
     */
    public void quit()
    {
        socket.cleanup();
    }

    /**
     *  Encodes and sends the request to the server. 
     *  First the objects will be encoded into xml and then 
     *  they will be send to the server.<br>
     *  The response from the server will be decoded and returned.
     *  To get the more details about the response the following methods 
     *  can be used.
     *  <ul>
     *  <li><code>getContentType</code> returns the type of the content </li>
     *  <li><code>getQueryString</code> returns the queryString </li>
     *  <li><code>getUserId</code> returns the user name who send the query </li>
     *  <li><code>getTimestamp</code> returns the timestamp of the query </li>
     *  </ul>
     *  @param userId the username of the authenticated user
     *  @param contentType the type of the <code>messageObject</code>. 
     *         Example <code>RosterEntry.ID</code>
     *  @param queryString the query that should be done on the server.
     *         Example <code>IModelActions.LIST</code> to get a listing.
     *  @param messageObject the object to send to the server. 
     *         This can be null if you want a listing only.
     */
    public List<AbstractMessage> sendRequest(String userId,
            String contentType,
            String queryString,
            AbstractMessage messageObject)
    {
        //set up the factory
        factory = new XMLFactory();
        factory.setupEncodeFactory(userId, contentType, queryString);
        //wrapp into a list
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.add(messageObject);
        //encode, send and get the result
        String result = queryServer(factory.encode(list));
        //assert valid
        if(result != null)
        {
            //decode the message
            factory.setupDecodeFactory(result);
            list = factory.decode();
            //get the information
            this.userId = factory.getUserId();
            this.contentType = factory.getContentType();
            this.queryString = factory.getQueryString();
            this.timestamp = factory.getTimestamp();
            System.out.println(userId+","+contentType+","+queryString+","+timestamp);
            //return the decoded objects
            return list;
        }
        
        //failed to decode
        return null;
    }


    /**
     * Sends the given message to the server.<br>
     * The method will wait for a reply from the server
     * @param message the message to send
     */
    private String queryServer(String message)
    {
        try
        {
            //try to send
            System.out.println("Sending: "+socket.sendMessage(message));
            return socket.receiveMessage();
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to get the reply from the server");
            return null;
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
     * Returns the user identification string.<br>
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Returns the type of this xml message.<br>
     * The type specifies the content of the message.<br>
     * Examples for the type: <code>RosterEntry.ID</code><br>
     * This means that the returned content contains <code>RosterEntry</code> objects.
     * @return the messageType
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Returns the queryString used in this xml message.<br>
     * The queryString helps to categorize the message.<br>
     * Examples for the query string <code>IModelActions.add</code><br>
     * With this string you can process the data and set the actions needed.
     * @return the queryString
     */
    public String getQueryString()
    {
        return queryString;
    }

    /**
     * Returns the timestamp of the xml message.<br>
     * The time is in seconds since 1.1.1970.
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }
}
