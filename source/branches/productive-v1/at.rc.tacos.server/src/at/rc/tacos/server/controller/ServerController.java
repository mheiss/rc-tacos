/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.server.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import at.rc.tacos.codec.AddressDecoder;
import at.rc.tacos.codec.AddressEncoder;
import at.rc.tacos.codec.CallerDecoder;
import at.rc.tacos.codec.CallerEncoder;
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
import at.rc.tacos.codec.LockDecoder;
import at.rc.tacos.codec.LockEncoder;
import at.rc.tacos.codec.LoginDecoder;
import at.rc.tacos.codec.LoginEncoder;
import at.rc.tacos.codec.LogoutDecoder;
import at.rc.tacos.codec.LogoutEncoder;
import at.rc.tacos.codec.MobilePhoneDecoder;
import at.rc.tacos.codec.MobilePhoneEncoder;
import at.rc.tacos.codec.PatientDecoder;
import at.rc.tacos.codec.PatientEncoder;
import at.rc.tacos.codec.PeriodDecoder;
import at.rc.tacos.codec.PeriodEncoder;
import at.rc.tacos.codec.RosterEntryDecoder;
import at.rc.tacos.codec.RosterEntryEncoder;
import at.rc.tacos.codec.ServiceTypeDecoder;
import at.rc.tacos.codec.ServiceTypeEncoder;
import at.rc.tacos.codec.SickPersonDecoder;
import at.rc.tacos.codec.SickPersonEncoder;
import at.rc.tacos.codec.StaffMemberDecoder;
import at.rc.tacos.codec.StaffMemberEncoder;
import at.rc.tacos.codec.SystemMessageDecoder;
import at.rc.tacos.codec.SystemMessageEncoder;
import at.rc.tacos.codec.TransportDecoder;
import at.rc.tacos.codec.TransportEncoder;
import at.rc.tacos.codec.VehicleDecoder;
import at.rc.tacos.codec.VehicleEncoder;
import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.socket.MyClient;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.ServerListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Link;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Lock;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Period;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.SickPerson;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.server.listener.AddressListener;
import at.rc.tacos.server.listener.AuthenticationListener;
import at.rc.tacos.server.listener.CompetenceListener;
import at.rc.tacos.server.listener.DayInfoListener;
import at.rc.tacos.server.listener.DialysisPatientListener;
import at.rc.tacos.server.listener.DiseaseListener;
import at.rc.tacos.server.listener.JobListener;
import at.rc.tacos.server.listener.LinkListener;
import at.rc.tacos.server.listener.LocationListener;
import at.rc.tacos.server.listener.LockListener;
import at.rc.tacos.server.listener.MobilePhoneListener;
import at.rc.tacos.server.listener.NotifyDetailListener;
import at.rc.tacos.server.listener.PeriodListener;
import at.rc.tacos.server.listener.RosterEntryListener;
import at.rc.tacos.server.listener.ServiceTypeListener;
import at.rc.tacos.server.listener.SickPersonListener;
import at.rc.tacos.server.listener.StaffMemberListener;
import at.rc.tacos.server.listener.TransportListener;
import at.rc.tacos.server.listener.VehicleDetailListener;

public class ServerController {

	// the shared instance
	private static ServerController serverController;

	// this pool contains all client connections form the application
	private final List<ClientSession> connClientPool = Collections.synchronizedList(new ArrayList<ClientSession>());

	// the logger
	private static Logger logger = Logger.getLogger(ServerController.class);

	/**
	 * Default private class constructor.<br>
	 * Setting up the client pools and registers the listener and codec classes.
	 */
	private ServerController() {
		// register the encoders and decoders
		registerEncoderAndDecoder();
		registerModelListeners();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ServerController getDefault() {
		// create a new instance or reuse
		if (serverController == null)
			serverController = new ServerController();
		return serverController;
	}

	/**
	 * Invoked when a new client is connected to the server.<br>
	 * A new session will be created, the connection must first be authenticated
	 * to interact with the server.
	 * 
	 * @param client
	 *            the new conneted client
	 */
	public void clientConnected(MyClient client) {
		logger.info("Creating new session for client: " + client.getSocket().getInetAddress());
		ClientSession session = new ClientSession(client);
		synchronized (connClientPool) {
			connClientPool.listIterator().add(session);
		}
	}

	/**
	 * Invoked when a client was disconnected throught abnormal programm
	 * termination or a network fault.<br>
	 * If the client was authenticated the other authenticated clients will be
	 * informed with a <code>SystemMessage</code>
	 * 
	 * @param client
	 *            the disconnected client
	 */
	public void clientDisconnected(MyClient client) {
		// get the user session
		ClientSession session = getSession(client);
		logger.info("Disconnect detected ( " + session + " ) ");
		// close the connection if we have one
		if (session.getConnection() != null) {
			session.getConnection().removeAllNetListeners();
			session.getConnection().requestStop();
			// close the socket when it is open
			try {
				session.getConnection().getSocket().cleanup();
			}
			catch (Exception e) {
				logger.error("Failed to close the input and output streams");
			}
		}

		// remove the user session
		synchronized (connClientPool) {
			ListIterator<ClientSession> listIter = connClientPool.listIterator();
			while (listIter.hasNext()) {
				ClientSession listItem = listIter.next();
				if (listItem.equals(session))
					listIter.remove();
			}
		}

		// assert valid
		String user = session.getUsername();
		if (user == null)
			return;

		logger.info("Checking locked objects from user:" + user);
		LockListener listener = (LockListener) ServerListenerFactory.getInstance().getListener(Lock.ID);

		// get the managed lock objects
		synchronized (listener.getLockObjects()) {
			ListIterator<Lock> iter = listener.getLockObjects().listIterator();
			while (iter.hasNext()) {
				Lock lock = iter.next();
				if (lock.getLockedBy().equalsIgnoreCase(user)) {
					logger.info("Removing the lock from the object: " + lock.getContentId() + ":" + lock.getLockedId());
					// remove the lock from the object
					lock.setHasLock(false);
					// create a new message info
					AbstractMessageInfo info = new AbstractMessageInfo();
					info.setSequenceId("1");
					info.setContentType(Lock.ID);
					info.setQueryString(IModelActions.REMOVE);
					info.setMessage(lock);
					// brodcast to all connected clients
					brodcastMessage(session, info);
					iter.remove();
				}
			}
		}
	}

	/**
	 * Returns the session object for this client connection
	 * 
	 * @param connection
	 *            the connection to get the session from
	 * @return the session object or null if no session is available
	 */
	public ClientSession getSession(MyClient connection) {
		synchronized (connClientPool) {
			ListIterator<ClientSession> listIter = connClientPool.listIterator();
			while (listIter.hasNext()) {
				ClientSession nextSession = listIter.next();
				if (nextSession.getConnection() == connection)
					return nextSession;
			}
			return null;
		}
	}

	/**
	 * Encodes the list of message into xml and brodcasts the message all
	 * authenticated clients.
	 * 
	 * @param username
	 *            the source of the message
	 * @param messageInfo
	 *            the information to send to the clients
	 */
	public void brodcastMessage(ClientSession session, AbstractMessageInfo messageInfo) {
		// set up the factory
		XMLFactory factory = new XMLFactory();
		factory.setUserId(session.getUsername());
		factory.setTimestamp(Calendar.getInstance().getTimeInMillis());
		factory.setContentType(messageInfo.getContentType());
		factory.setQueryString(messageInfo.getQueryString());
		factory.setSequenceId(messageInfo.getSequenceId());
		factory.setFilter(messageInfo.getQueryFilter());
		String message = factory.encode(messageInfo.getMessageList());

		// loop over the client pool and send the message
		synchronized (connClientPool) {
			ListIterator<ClientSession> listIter = connClientPool.listIterator();
			while (listIter.hasNext()) {
				ClientSession nextSession = listIter.next();
				// Send the message to all authenticated clients, except the web
				// clients
				if (nextSession.isAuthenticated() & !nextSession.isWebClient()) {
					MyClient client = nextSession.getConnection();
					client.sendMessage(message);
				}
				// send the message back to the web client
				if (nextSession.equals(session) & session.isWebClient()) {
					MyClient client = nextSession.getConnection();
					client.sendMessage(message);
				}
			}
		}
	}

	/**
	 * Encodes the message into xml and sends the message back to the source
	 * client.
	 * 
	 * @param session
	 *            the client session to send the message back to
	 * @param messageInfo
	 *            the information to send to the clients
	 */
	public void sendMessage(ClientSession session, AbstractMessageInfo messageInfo) {
		MyClient connection = session.getConnection();
		String userId = session.getUsername();
		// When we have no user id then set it to system
		if (userId == null)
			userId = "system";
		// set up the factory
		XMLFactory factory = new XMLFactory();
		factory.setUserId(userId);
		factory.setTimestamp(Calendar.getInstance().getTimeInMillis());
		factory.setContentType(messageInfo.getContentType());
		factory.setQueryString(messageInfo.getQueryString());
		factory.setSequenceId(messageInfo.getSequenceId());
		String xml = factory.encode(messageInfo.getMessageList());
		// encode and send
		connection.sendMessage(xml);
	}

	/**
	 * Convenience method to registers the encoders and decoders.
	 */
	private void registerEncoderAndDecoder() {
		// register the needed model types with the decoders and encoders
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
		protFactory.registerDecoder(Disease.ID, new DiseaseDecoder());
		protFactory.registerEncoder(Disease.ID, new DiseaseEncoder());
		protFactory.registerDecoder(Address.ID, new AddressDecoder());
		protFactory.registerEncoder(Address.ID, new AddressEncoder());
		protFactory.registerDecoder(Lock.ID, new LockDecoder());
		protFactory.registerEncoder(Lock.ID, new LockEncoder());
		protFactory.registerDecoder(SickPerson.ID, new SickPersonDecoder());
		protFactory.registerEncoder(SickPerson.ID, new SickPersonEncoder());
		protFactory.registerDecoder(Period.ID, new PeriodDecoder());
		protFactory.registerEncoder(Period.ID, new PeriodEncoder());
		// system events
		protFactory.registerDecoder(Login.ID, new LoginDecoder());
		protFactory.registerEncoder(Login.ID, new LoginEncoder());
		protFactory.registerDecoder(Logout.ID, new LogoutDecoder());
		protFactory.registerEncoder(Logout.ID, new LogoutEncoder());
		protFactory.registerDecoder(SystemMessage.ID, new SystemMessageDecoder());
		protFactory.registerEncoder(SystemMessage.ID, new SystemMessageEncoder());
		protFactory.registerDecoder(Link.ID, new LinkDecoder());
		protFactory.registerEncoder(Link.ID, new LinkEncoder());
	}

	/**
	 * Convinience method to registers the ui model listeners to get updates
	 * from the network layer.
	 */
	private void registerModelListeners() {
		ServerListenerFactory factory = ServerListenerFactory.getInstance();
		// register the listeners
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
		factory.registerModelListener(Link.ID, new LinkListener());
		factory.registerModelListener(Job.ID, new JobListener());
		factory.registerModelListener(Location.ID, new LocationListener());
		factory.registerModelListener(Competence.ID, new CompetenceListener());
		factory.registerModelListener(ServiceType.ID, new ServiceTypeListener());
		factory.registerModelListener(Address.ID, new AddressListener());
		factory.registerModelListener(Disease.ID, new DiseaseListener());
		factory.registerModelListener(Lock.ID, new LockListener());
		factory.registerModelListener(SickPerson.ID, new SickPersonListener());
		factory.registerModelListener(Period.ID, new PeriodListener());
	}
}
