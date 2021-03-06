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
package at.rc.tacos.core.net;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.osgi.framework.BundleContext;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.IModelListener;
import at.rc.tacos.core.net.jobs.SendJobRule;
import at.rc.tacos.core.net.socket.MySocket;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SystemMessage;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.net";
	private static NetWrapper plugin;

	// the username of the logged in user
	private String netSessionUserName;

	// the list of send items, which have currently no answer from the server
	// if a answere from the server is recevied the items is deleted from the
	// list
	private ConcurrentHashMap<String, AbstractMessageInfo> messageList;
	private String lastSendSequence;

	// the system listener for logging messages
	private IModelListener logService = ListenerFactory.getDefault().getListener(SystemMessage.ID);

	// the running jobs
	private final static String JOB_MONITOR = "MonitorJob";
	private final static String JOB_LISTEN = "ListenJob";
	private final static String JOB_SEND = "SendJob";

	/**
	 * The constructor
	 */
	public NetWrapper() {
		messageList = new ConcurrentHashMap<String, AbstractMessageInfo>();
	}

	/**
	 * Called when the plugin is started
	 * 
	 * @param context
	 *            lifecyle informations
	 * @throws Exception
	 *             when a error occures during startup
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * Called when the plugin is stopped
	 * 
	 * @param context
	 *            lifecyle informations
	 * @throws Exception
	 *             when a error occures during shutdown
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static NetWrapper getDefault() {
		if (plugin == null)
			plugin = new NetWrapper();
		return plugin;
	}

	/**
	 * Initializes and sets up the needed jobs to watch the network status
	 */
	public void init() {
		// start the thread to listen to new data
		startListenJob();
		// the monitor thread
		startMonitorThread();
		// log to the client
		logService.log("Starting up the network jobs", Status.INFO);
	}

	/**
	 * Sets the name of the currently authenticated user
	 * 
	 * @param name
	 *            the username
	 */
	public void setNetSessionUsername(String username) {
		this.netSessionUserName = username;
	}

	/**
	 * Returns whether or not there is a logged in user
	 */
	public boolean isAuthenticated() {
		// check if we have a user
		if (netSessionUserName == null)
			return false;
		return true;
	}

	// METHODS TO START THE THREADS
	/**
	 * Starts the receive job to listen to new data
	 */
	private void startListenJob() {
		// create a new job if we do not have one
		ListenJob listenJob = new ListenJob();
		listenJob.setSystem(true);
		listenJob.setPriority(Job.LONG);
		listenJob.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				// check if the job was successful
				if (event.getResult() == Status.CANCEL_STATUS)
					requestNetworkStop(true);
			}
		});
		listenJob.schedule();
	}

	/**
	 * Monitors the send packages and informs when the server sends no reply
	 */
	private void startMonitorThread() {
		// create the job if we do not have one
		MonitorJob monitorJob = new MonitorJob();
		monitorJob.setSystem(true);
		monitorJob.setPriority(Job.SHORT);
		monitorJob.schedule();
	}

	// METHODS TO SEND MESSAGES
	/**
	 * Sends a request to the server to login the user.
	 * 
	 * @param login
	 *            the authentication information to use
	 */
	public void sendLoginMessage(Login login) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(Login.ID);
		info.setQueryString(IModelActions.LOGIN);
		info.setMessage(login);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to logout the user.
	 * 
	 * @param logout
	 *            the logut message
	 */
	public void sendLogoutMessage(Logout logout) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(Logout.ID);
		info.setQueryString(IModelActions.LOGOUT);
		info.setMessage(logout);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to add the object to the database.<br>
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be
	 * <code>RosterEntry.ID</cod> that would mean
	 * that the add message contains a <code>RosterEntry</code> object.
	 * 
	 * @param contentType
	 *            the type of the content
	 * @param addMessage
	 *            the object to add
	 */
	public void sendAddMessage(String contentType, AbstractMessage addMessage) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.ADD);
		info.setMessage(addMessage);
		sheduleAndSend(info);
	}

	/**
	 * Same as the send add message method but allows to send a list of data
	 * instead of a single object.
	 * 
	 * @param contentType
	 *            the type of the message
	 * @param addList
	 *            the list of object to send
	 */
	public void sendAddAllMessage(String contentType, List<AbstractMessage> addList) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.ADD_ALL);
		info.setMessageList(addList);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to remove the object from the database. To
	 * identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be
	 * <code>RosterEntry.ID</cod> that would mean
	 * that the remove request contains a <code>RosterEntry</code> object.
	 * 
	 * @param contentType
	 *            the type of the content
	 * @param removeMessage
	 *            the object to remove
	 */
	public void sendRemoveMessage(String contentType, AbstractMessage removeMessage) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.REMOVE);
		info.setMessage(removeMessage);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to update the object in the database. To
	 * identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be
	 * <code>RosterEntry.ID</cod> that would mean
	 * that the update request contains a <code>RosterEntry</code> object.
	 * 
	 * @param contentType
	 *            the type of the content
	 * @param updateMessage
	 *            the object to update
	 */
	public void sendUpdateMessage(String contentType, AbstractMessage updateMessage) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.UPDATE);
		info.setMessage(updateMessage);
		sheduleAndSend(info);
	}

	/**
	 * Sends a listing request for the given object to the server.<br>
	 * To identify the type of the listing request a content type must be
	 * provided.<br>
	 * A example of a content type would be
	 * <code>RosterEntry.ID</cod> that would mean
	 * that the lisitng request is for <code>RosterEntry</code> objects.
	 * 
	 * @param contentType
	 *            the type of the listing request
	 * @param filter
	 *            the filter for the query
	 */
	public void requestListing(String contentType, QueryFilter filter) {
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.LIST);
		info.setQueryFilter(filter);
		sheduleAndSend(info);
	}

	// PRIVATE METHODS
	/**
	 * Shedules and sends the given message to the server
	 * 
	 * @param info
	 *            the message info object containing the data to send
	 */
	public void sheduleAndSend(final AbstractMessageInfo info) {
		// generate a sequence number for the message
		Random random = new Random();
		info.setSequenceId(netSessionUserName + "_" + String.valueOf(random.nextInt(10000)));
		info.setTimestamp(Calendar.getInstance().getTimeInMillis());
		// create the send kob and put it to wait
		SendJob sendJob = new SendJob(info);
		sendJob.setRule(new SendJobRule());
		sendJob.setPriority(Job.SHORT);
		sendJob.setSystem(true);
		sendJob.schedule();

		// save the last generated sequence
		lastSendSequence = info.getSequenceId();
	}

	/**
	 * Stops all network traffic and opens the wizard to establish a new
	 * connection to the server
	 * 
	 * @param showWizard
	 *            true if the connection wizard should be shown to reconnect
	 */
	public void requestNetworkStop(boolean showWizard) {
		try {
			netSessionUserName = null;

			logService.log("Setting all running network jobs to sleep", Status.INFO);

			// request all running jobs to stop
			IJobManager jobManager = Job.getJobManager();
			// wait for the send job to complete
			Job.getJobManager().cancel(JOB_SEND);
			for (Job job : jobManager.find(JOB_SEND))
				job.join();

			// wait for the listen job to complete
			Job.getJobManager().cancel(JOB_LISTEN);
			for (Job job : jobManager.find(JOB_LISTEN))
				job.join();

			// wait for the monitor job to complete
			Job.getJobManager().cancel(JOB_MONITOR);
			for (Job job : jobManager.find(JOB_MONITOR))
				job.join();

			// close the current socket
			NetSource.getInstance().closeConnection();

			// show the connection wizard
			if (showWizard) {
				logService.log("Starting network wizard", Status.INFO);
				logService.connectionChange(IConnectionStates.STATE_DISCONNECTED);
			}
		}
		catch (Exception e) {
			logService.log("Failed to close the network connection", Status.INFO);
			logService.log(e.getMessage(), Status.ERROR);
		}
	}

	/**
	 * Logs the error with the build in log
	 */
	public static void log(String message, int severity, Throwable cause) {
		NetWrapper.getDefault().getLog().log(new Status(severity, PLUGIN_ID, message, cause));
	}

	// THREADS FOR THE NETWORK
	/**
	 * The thread that listens to new data and informs the listeners.
	 */
	public class ListenJob extends Job {

		/**
		 * Default class constructor
		 */
		public ListenJob() {
			super(JOB_LISTEN);
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) {
			return JOB_LISTEN.equals(family);
		}

		/**
		 * Loops and receives data.
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				// get the network connection to read data from
				final MySocket client = NetSource.getInstance().getConnection();
				monitor.beginTask("Listening to new data on the network", IProgressMonitor.UNKNOWN);
				while (!monitor.isCanceled()) {
					String newData = null;
					try {
						// read new data
						BufferedReader in = client.getBufferedInputStream();
						newData = in.readLine();
						if (newData == null)
							throw new SocketException("Failed to read data from the socket");
					}
					catch (SocketTimeoutException timeout) {
						// timeout, just go on . . .
						continue;
					}

					try {
						// set up the factory to decode
						XMLFactory xmlFactory = new XMLFactory();
						// replace all characters
						String message = newData.replaceAll("&lt;br/&gt;", "\n");
						xmlFactory.setupDecodeFactory(message);
						// decode the message
						final List<AbstractMessage> receivedMessage = xmlFactory.decode();
						// get the type of the item
						final String contentType = xmlFactory.getContentType();
						final String queryString = xmlFactory.getQueryString();
						final String sequenceId = xmlFactory.getSequenceId();

						// check if the sequence is a error message
						if (sequenceId.equalsIgnoreCase("ERROR")) {
							// remove the last send message from the list
							messageList.remove(lastSendSequence);
							logService.log("Removed the last send sequenceId, the server reported a error", IStatus.ERROR);
						}

						// remove the package from the list
						if (messageList.containsKey(sequenceId))
							messageList.remove(sequenceId);

						Job job = new Job("ProccessData") {

							@Override
							protected IStatus run(IProgressMonitor monitor) {
								try {
									// start the job to proccess the data
									monitor.beginTask("Processing the received data:" + contentType, IProgressMonitor.UNKNOWN);

									// try to get a listener for this message
									ListenerFactory listenerFactory = ListenerFactory.getDefault();
									if (!listenerFactory.hasListeners(contentType)) {
										logService.log("No listener found for the message type: " + contentType, IStatus.WARNING);
										return Status.CANCEL_STATUS;
									}

									IModelListener listener = listenerFactory.getListener(contentType);
									// now pass the message to the listener
									if (IModelActions.ADD.equalsIgnoreCase(queryString))
										listener.add(receivedMessage.get(0));
									if (IModelActions.ADD_ALL.endsWith(queryString))
										listener.addAll(receivedMessage);
									if (IModelActions.REMOVE.equalsIgnoreCase(queryString))
										listener.remove(receivedMessage.get(0));
									if (IModelActions.UPDATE.equalsIgnoreCase(queryString))
										listener.update(receivedMessage.get(0));
									if (IModelActions.LIST.equalsIgnoreCase(queryString))
										listener.list(receivedMessage);
									if (IModelActions.LOGIN.equalsIgnoreCase(queryString))
										listener.loginMessage(receivedMessage.get(0));
									if (IModelActions.LOGOUT.equalsIgnoreCase(queryString))
										listener.logoutMessage(receivedMessage.get(0));
									if (IModelActions.SYSTEM.equalsIgnoreCase(queryString))
										listener.systemMessage(receivedMessage.get(0));

									// everything is ok
									return Status.OK_STATUS;
								}
								catch (Exception e) {
									log("Failed to process the received data: " + e.getMessage(), IStatus.ERROR, e.getCause());
									return Status.CANCEL_STATUS;
								}
								finally {
									monitor.done();
								}
							}
						};
						job.setSystem(true);
						job.schedule();
					}
					catch (Exception e) {
						log("Failed to process the received data: " + e.getMessage(), IStatus.ERROR, e.getCause());
					}
				}
				return Status.OK_STATUS;
			}
			catch (Exception e) {
				log("Critical error while listening to new data: " + e.getMessage(), Status.ERROR, e.getCause());
				log("Starting the network wizard from the listen job. Reason:" + e.getMessage(), Status.ERROR, e.getCause());
				return Status.CANCEL_STATUS;
			}
			finally {
				monitor.done();
			}
		}
	}

	/**
	 * Threads that sends the new data to the server and waits for the server
	 * reply
	 */
	public class SendJob extends Job {

		// properties
		private AbstractMessageInfo messageInfo;

		/**
		 * Default class constructor to set up the send job
		 * 
		 * @param messageInfo
		 *            the message information object to send to the server
		 */
		public SendJob(AbstractMessageInfo messageInfo) {
			super(JOB_SEND);
			this.messageInfo = messageInfo;
			setName("Sende " + messageInfo.getContentType() + " " + messageInfo.getQueryString() + " an den Server");
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) {
			return JOB_SEND.equals(family);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				// the package is send now
				messageInfo.setTimestamp(Calendar.getInstance().getTimeInMillis());

				// get the network connection to send data
				final MySocket client = NetSource.getInstance().getConnection();

				monitor.beginTask("Initialisierung", IProgressMonitor.UNKNOWN);
				// setup the facotry
				XMLFactory factory = new XMLFactory();
				factory.setUserId(netSessionUserName);
				factory.setTimestamp(messageInfo.getTimestamp());
				factory.setContentType(messageInfo.getContentType());
				factory.setQueryString(messageInfo.getQueryString());
				factory.setSequenceId(messageInfo.getSequenceId());
				factory.setFilter(messageInfo.getQueryFilter());

				// send the encoded data to the server
				monitor.beginTask("Sende Daten and den Server", IProgressMonitor.UNKNOWN);
				String message = factory.encode(messageInfo.getMessageList());
				PrintWriter writer = client.getBufferedOutputStream();
				writer.println(message);
				writer.flush();

				// put the message to the list of packages that are waiting for
				// server reply
				messageList.put(messageInfo.getSequenceId(), messageInfo);

				// wait until the server sends a response
				while (messageList.containsKey(messageInfo.getSequenceId())) {
					monitor.beginTask("Warte auf Antwort vom Server", IProgressMonitor.UNKNOWN);
					Thread.sleep(10);
				}
				// everything is ok
				return Status.OK_STATUS;
			}
			catch (Exception e) {
				logService.log("Failed to send the package #" + messageInfo.getSequenceId() + " content: " + messageInfo.getContentType(),
						Status.ERROR);
				logService.log("Starting the network wizard from the send job. Reason:" + e.getMessage(), Status.ERROR);
				return Status.CANCEL_STATUS;
			}
			finally {
				monitor.done();
			}
		}
	}

	/**
	 * Monitors the other threads and has the main task to watch whether or not
	 * the requests to the server are successfully. The thread should runn every
	 * second to watch the send items.
	 */
	public class MonitorJob extends Job {

		/**
		 * Default class constructor
		 */
		public MonitorJob() {
			super(JOB_MONITOR);
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) {
			return JOB_MONITOR.equals(family);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				monitor.beginTask("�berpr�fe gesendete Pakete", messageList.size());
				// loop and chek the send packages
				for (Entry<String, AbstractMessageInfo> entry : messageList.entrySet()) {
					AbstractMessageInfo info = entry.getValue();
					if (info == null)
						continue;
					monitor.subTask("Pr�fe Paket Nummer:" + info.getSequenceId());
					// check if the time is greater than 5 seconds and log a
					// warning
					if (info.getCounter() == 5)
						logService.log("WARNING: The package #" + info.getSequenceId() + " content: " + info.getContentType()
								+ " not been answered by the server in time. Waiting . . .", IStatus.WARNING);
					// show a message box to retransmitt the package
					if (info.getCounter() == 10) {
						messageList.remove(info.getSequenceId());
						logService.log("The package #" + info.getSequenceId()
								+ " cannot be transmitted to the server. This is a permanent error, I gave up", IStatus.ERROR);
						logService.transferFailed(info);
					}
					// increment the counter by one
					info.setCounter(info.getCounter() + 1);
					monitor.worked(1);
				}
			}
			catch (Exception e) {
				logService.log("Critical error while monitoring the status of the packages: " + e.getMessage(), Status.ERROR);
			}
			finally {
				// if the job is not cancled restart it again
				if (!monitor.isCanceled())
					schedule(1000);
				monitor.done();
			}
			return Status.OK_STATUS;
		}
	}
}
