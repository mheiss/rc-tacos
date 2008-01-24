package at.rc.tacos.core.net.internal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.codec.CompetenceDecoder;
import at.rc.tacos.codec.CompetenceEncoder;
import at.rc.tacos.codec.DayInfoMessageDecoder;
import at.rc.tacos.codec.DayInfoMessageEncoder;
import at.rc.tacos.codec.DialysisDecoder;
import at.rc.tacos.codec.DialysisEncoder;
import at.rc.tacos.codec.JobDecoder;
import at.rc.tacos.codec.JobEncoder;
import at.rc.tacos.codec.LocationDecoder;
import at.rc.tacos.codec.LocationEncoder;
import at.rc.tacos.codec.LoginDecoder;
import at.rc.tacos.codec.LoginEncoder;
import at.rc.tacos.codec.LogoutDecoder;
import at.rc.tacos.codec.LogoutEncoder;
import at.rc.tacos.codec.MobilePhoneDecoder;
import at.rc.tacos.codec.MobilePhoneEncoder;
import at.rc.tacos.codec.CallerDecoder;
import at.rc.tacos.codec.CallerEncoder;
import at.rc.tacos.codec.PatientDecoder;
import at.rc.tacos.codec.PatientEncoder;
import at.rc.tacos.codec.RosterEntryDecoder;
import at.rc.tacos.codec.RosterEntryEncoder;
import at.rc.tacos.codec.ServiceTypeDecoder;
import at.rc.tacos.codec.ServiceTypeEncoder;
import at.rc.tacos.codec.StaffMemberDecoder;
import at.rc.tacos.codec.StaffMemberEncoder;
import at.rc.tacos.codec.SystemMessageDecoder;
import at.rc.tacos.codec.SystemMessageEncoder;
import at.rc.tacos.codec.TransportDecoder;
import at.rc.tacos.codec.TransportEncoder;
import at.rc.tacos.codec.VehicleDecoder;
import at.rc.tacos.codec.VehicleEncoder;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
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
    //connection to the server
    private MySocket socket;
    //the factory
    private XMLFactory factory;
    
    //the user to send the request
    private String sessionUserId;

    //result of the decode
    private String userId;
    private String contentType;
    private String queryString;
    private long timestamp;

    /**
     * Default class constructr
     */
    public WebClient() 
    { 
        //initialize the codec factory
        registerEncoderAndDecoder();
        factory = new XMLFactory();
    }

    /**
     * Connects to a given server address.
     * @param serverAddress the host name or ip address of the remote host
     * @param serverPort the port number to connect to
     * @return true if the connection was established successfully, otherwise false
     */
    public boolean connect(String serverAddress,int serverPort)
    {
        try
        {
            socket = new MySocket(serverAddress,serverPort);
            socket.createInputStream();
            socket.createOutputStream(); 
            socket.setSoTimeout(5000);
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
     *  Sets the username for the request.
     *  @param sessionUserId the authenticated user
     */
    public void setSessionUser(String sessionUserId)
    {
        this.sessionUserId = sessionUserId;
    }
    
    /**
     * Sends a request to login to the server.
     * @param username the username to authenticate
     * @param password the password for the username
     * @return the result of the login process or a system message in case of an error
     */
    public AbstractMessage sendLoginRequest(String username,String password)
    {
        //create a login object
        Login login = new Login(username,password,true);
        //store the username
        sessionUserId = username;
        //send the request
        List<AbstractMessage> result = sendRequest(sessionUserId,Login.ID,IModelActions.LOGIN,null,login);
        return result.get(0);
    }
    
    /**
     * Sends a request to logout to the server.
     * @return the result of the logout process or a system message in case of an error
     */
    public AbstractMessage sendLogoutRequest()
    {
        //create a login object
        Logout logout = new Logout(sessionUserId);
        //send the request
        List<AbstractMessage> result = sendRequest(sessionUserId,Logout.ID,IModelActions.LOGOUT,null,logout);
        return result.get(0);
    }
    
    /**
     * Sends a request to add the given object to the server.
     * If the object was added successfully the server will send
     * the object back.
     * @param contentType the type of the <code>messageObject</code>. 
     *        Example <code>RosterEntry.ID</code>
     * @param addObject the object to be added to the server
     *  To get the more details about the response the following methods 
     *  can be used.
     *  <ul>
     *  <li><code>getContentType</code> returns the type of the content </li>
     *  <li><code>getQueryString</code> returns the queryString </li>
     *  <li><code>getUserId</code> returns the user name who send the query </li>
     *  <li><code>getTimestamp</code> returns the timestamp of the query </li>
     *  </ul>
     * @return the added object or a system message in the case of an error
     */
    public AbstractMessage sendAddRequest(String contentType,AbstractMessage addObject)
    {
        List<AbstractMessage> result = sendRequest(sessionUserId,contentType,IModelActions.ADD,null,addObject);
        return result.get(0);
    }
    
    /**
     * Sends a request to remove the given object from the server.
     * If the object was removed successfully the server will send
     * the old object back.
     * @param contentType the type of the <code>messageObject</code>. 
     *        Example <code>RosterEntry.ID</code>
     * @param removeObject the object to be removed from the server
     *  To get the more details about the response the following methods 
     *  can be used.
     *  <ul>
     *  <li><code>getContentType</code> returns the type of the content </li>
     *  <li><code>getQueryString</code> returns the queryString </li>
     *  <li><code>getUserId</code> returns the user name who send the query </li>
     *  <li><code>getTimestamp</code> returns the timestamp of the query </li>
     *  </ul>
     * @return the removed object or a system message in the case of an error
     */
    public AbstractMessage sendRemoveRequest(String contentType,AbstractMessage removeObject)
    {
        List<AbstractMessage> result = sendRequest(sessionUserId,contentType,IModelActions.REMOVE,null,removeObject);
        return result.get(0);
    }
    
    /**
     * Sends a request to update the given object to the server.
     * If the object was added successfully the server will send
     * the updated object back.
     * @param contentType the type of the <code>messageObject</code>. 
     *        Example <code>RosterEntry.ID</code>
     * @param updateObject the object to be updated on to the server
     *  To get the more details about the response the following methods 
     *  can be used.
     *  <ul>
     *  <li><code>getContentType</code> returns the type of the content </li>
     *  <li><code>getQueryString</code> returns the queryString </li>
     *  <li><code>getUserId</code> returns the user name who send the query </li>
     *  <li><code>getTimestamp</code> returns the timestamp of the query </li>
     *  </ul>
     * @return the updated object or a system message in the case of an error
     */
    public AbstractMessage sendUpdateRequest(String contentType,AbstractMessage updateObject)
    {
        List<AbstractMessage> result = sendRequest(sessionUserId,contentType,IModelActions.UPDATE,null,updateObject);
        return result.get(0);
    }
    
    /**
     * Sends a request to get a list of the given object from the server.
     * @param contentType the type of the object to get a listing from
     * @param queryFilter the filter to apply to this request.
     *        Example: Request a listing of objects from a specifiy id
     *  To get the more details about the response the following methods 
     *  can be used.
     *  <ul>
     *  <li><code>getContentType</code> returns the type of the content </li>
     *  <li><code>getQueryString</code> returns the queryString </li>
     *  <li><code>getUserId</code> returns the user name who send the query </li>
     *  <li><code>getTimestamp</code> returns the timestamp of the query </li>
     *  </ul>
     * @return the result of the listing request
     */
    public List<AbstractMessage> sendListingRequest(String contentType,QueryFilter queryFilter)
    {
        return sendRequest(sessionUserId,contentType,IModelActions.LIST,queryFilter,null);
    }
    
    /**
     *  Encodes and sends the request to the server. 
     *  @param userId the username of the authenticated user
     *  @param contentType the type of the <code>messageObject</code>. 
     *  @param queryString the query that should be done on the server.
     *  @param queryFilter the filter to apply
     *  @param messageObject the object to send to the server. 
     *  @return the result list from the server
     */
    private List<AbstractMessage> sendRequest(
            String userId,
            String contentType,
            String queryString,
            QueryFilter queryFilter,
            AbstractMessage messageObject)
    {
        //set up the factory
        factory = new XMLFactory();
        factory.setupEncodeFactory(userId, contentType, queryString);
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        //wrapp into a list
        if(messageObject != null)
            list.add(messageObject);
        if(queryFilter != null)
            factory.setFilter(queryFilter);
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
            System.out.println("Received: "+ this.userId+","+this.contentType+","+this.queryString+","+this.timestamp);
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
     * 
     */

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
