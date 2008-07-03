package at.rc.tacos.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import at.rc.tacos.codec.CompetenceDecoder;
import at.rc.tacos.codec.CompetenceEncoder;
import at.rc.tacos.codec.DayInfoMessageDecoder;
import at.rc.tacos.codec.DayInfoMessageEncoder;
import at.rc.tacos.codec.DialysisDecoder;
import at.rc.tacos.codec.DialysisEncoder;
import at.rc.tacos.codec.DiseaseDecoder;
import at.rc.tacos.codec.DiseaseEncoder;
import at.rc.tacos.codec.JobDecoder;
import at.rc.tacos.codec.JobEncoder;
import at.rc.tacos.codec.LinkDecoder;
import at.rc.tacos.codec.LinkEncoder;
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
import at.rc.tacos.common.Message;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Link;
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
			socket.setSoTimeout(10000);
			return true;
		}
		catch(UnknownHostException uhe)
		{
			System.out.println("Cannot resole the host name "+serverAddress+".\n");
			return false;
		}
		catch (IOException ioe)
		{
			System.out.println("Error cannot connect to the server "+serverAddress+":"+serverPort+".\n");
			return false;
		}
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(Login.ID);
		message.setQueryString(IModelActions.LOGIN);
		message.addMessage(login);
		//send the request
		List<AbstractMessage> result = sendRequest(message);
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(Logout.ID);
		message.setQueryString(IModelActions.LOGOUT);
		message.addMessage(logout);
		//send the request
		List<AbstractMessage> result = sendRequest(message);
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(contentType);
		message.setQueryString(IModelActions.ADD);
		message.addMessage(addObject);
		//send the request
		List<AbstractMessage> result = sendRequest(message);
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(contentType);
		message.setQueryString(IModelActions.REMOVE);
		message.addMessage(removeObject);
		//send the request
		List<AbstractMessage> result = sendRequest(message);
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(contentType);
		message.setQueryString(IModelActions.UPDATE);
		message.addMessage(updateObject);
		//send the request
		List<AbstractMessage> result = sendRequest(message);
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
		//prepare and setup the message
		Message message = new Message();
		message.setUsername(sessionUserId);
		message.setContentType(contentType);
		message.setQueryString(IModelActions.LIST);
		//send the request
		return sendRequest(message);
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
	private List<AbstractMessage> sendRequest(Message message)
	{
		try
		{
			//set up the factory
			XMLFactory factory = new XMLFactory();
			String xmlRequest = factory.encode(message);
			//send the message
			socket.getBufferedOutputStream().println(xmlRequest);
			String xmlResponse = socket.getBufferedInputStream().readLine();
			//assert valid
			if(xmlResponse == null)
				throw new SocketException("Failed to read data from the socket.");
			Message responseMessage = factory.decode(xmlResponse);
			//save the returned info
			this.userId = responseMessage.getUsername();
			this.contentType = responseMessage.getContentType();
			this.queryString = responseMessage.getQueryString();
			this.timestamp = responseMessage.getTimestamp();
			System.out.println("Received: "+message+"\n");
			//return the decoded objects
			return message.getMessageList();
		}
		catch(Exception e)
		{
			System.out.println("Failed to send the message "+message +" to the server. (" +e.getMessage() +")");
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
		protFactory.registerDecoder(Disease.ID, new DiseaseDecoder());
		protFactory.registerEncoder(Disease.ID, new DiseaseEncoder());
		protFactory.registerDecoder(Link.ID, new LinkDecoder());
		protFactory.registerEncoder(Link.ID, new LinkEncoder());
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

	public MySocket getSocket() {
		return socket;
	}
}
