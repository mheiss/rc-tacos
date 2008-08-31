package at.rc.tacos.client.net;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.osgi.framework.BundleContext;

import at.rc.tacos.client.net.jobs.SendJobRule;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.IModelListener;
import at.rc.tacos.common.Message;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.net.MySocket;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client.net";
	private static NetWrapper plugin;

	//the username of the logged in user
	private String netSessionUserName;

	//the list of send items, which have currently no answer from the server
	//if a answere from the server is recevied the items is deleted from the list
	private ConcurrentHashMap<String,Message> messageList;
	private String lastSendSequence;
	
	//the listeners for events from the network
	private ArrayList<PropertyChangeListener> netListeners = new ArrayList<PropertyChangeListener>();

	//the running jobs
	private final static String JOB_MONITOR = "MonitorJob";
	private final static String JOB_LISTEN = "ListenJob";
	private final static String JOB_SEND = "SendJob";

	//flag to show whether the plugin is initialized or not
	private boolean initialized;

	/**
	 * The constructor
	 */
	public NetWrapper() 
	{
		messageList = new ConcurrentHashMap<String, Message>();
	}

	/**
	 * Called when the plugin is started
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during startup
	 */
	@Override
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
	}

	/**
	 * Called when the plugin is stopped
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during shutdown
	 */
	@Override
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
	 * Initializes and sets up the needed jobs to watch the network status
	 */
	public void init()
	{
		if(initialized)
			return;

		//start the thread to listen to new data
		startListenJob();
		//the monitor thread
		startMonitorThread();
		//log to the client
		log("Starting up the network jobs", Status.INFO,null);
	}

	/**
	 * Logs the error with the build in log
	 */
	public static void log(String message,int severity,Throwable cause)
	{
		NetWrapper.getDefault().getLog().log(new Status(severity,PLUGIN_ID,message,cause));
	}

	/**
	 * Sets the name of the currently authenticated user
	 * @param name the username
	 */
	public void setNetSessionUsername(String username)
	{
		this.netSessionUserName = username;
	}

	/**
	 * Returns whether or not there is a logged in user
	 */
	public boolean isAuthenticated()
	{
		//check if we have a user
		if(netSessionUserName == null)
			return false;
		return true;
	}

	// METHODS TO START THE THREADS
	/**
	 * Starts the receive job to listen to new data
	 */
	private void startListenJob()
	{
		//create a new job if we do not have one
		ListenJob listenJob = new ListenJob();
		listenJob.setSystem(true);
		listenJob.setPriority(Job.LONG);
		listenJob.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event) 
			{
				//check if the job was successful
				if(event.getResult() == Status.CANCEL_STATUS)
				{
					requestNetworkStop();
					firePropertyChangeEvent("NET_CONNECTION_LOST", null, NetSource.getInstance().getCurrentServer());
				}
			}
		});
		listenJob.schedule();
	}

	/**
	 * Monitors the send packages and informs when the server sends no reply
	 */
	private void startMonitorThread()
	{
		//create the job if we do not have one
		MonitorJob monitorJob = new MonitorJob();	
		monitorJob.setSystem(true);
		monitorJob.setPriority(Job.SHORT);
		monitorJob.schedule();
	}

	// METHODS TO SEND MESSAGES
	/**
	 * Sends a request to the server to login the user.
	 * @param login the authentication information to use
	 */
	public void sendLoginMessage(Login login)
	{
		Message info = new Message();
		info.setContentType(Login.ID);
		info.setQueryString(IModelActions.LOGIN);
		info.addMessage(login);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to logout the user.
	 * @param logout the logut message
	 */
	public void sendLogoutMessage(Logout logout)
	{
		Message info = new Message();
		info.setContentType(Logout.ID);
		info.setQueryString(IModelActions.LOGOUT);
		info.addMessage(logout);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to add the object to the database.<br>
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the add message contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param addMessage the object to add
	 */
	public void sendAddMessage(String contentType,AbstractMessage addMessage)
	{
		Message info = new Message();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.ADD);
		info.addMessage(addMessage);
		sheduleAndSend(info);
	}

	/**
	 * Same as the send add message method but allows to send a list of data instead of a single object.
	 * @param contentType the type of the message
	 * @param addList the list of object to send
	 */
	public void sendAddAllMessage(String contentType,List<AbstractMessage> addList)
	{
		Message info = new Message();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.ADD_ALL);
		info.setMessageList(addList);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to remove the object from the database.
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the remove request contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param removeMessage the object to remove
	 */
	public void sendRemoveMessage(String contentType,AbstractMessage removeMessage)
	{
		Message info = new Message();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.REMOVE);
		info.addMessage(removeMessage);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to update the object in the database.
	 * To identify the type of the content a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the update request contains a <code>RosterEntry</code> object.
	 * @param contentType the type of the content
	 * @param updateMessage the object to update
	 */
	public void sendUpdateMessage(String contentType,AbstractMessage updateMessage)
	{
		Message info = new Message();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.UPDATE);
		info.addMessage(updateMessage);
		sheduleAndSend(info);
	}

	/**
	 * Sends a listing request for the given object to the server.<br>
	 * To identify the type of the listing request a content type must be provided.<br>
	 * A example of a content type would be <code>RosterEntry.ID</cod> that would mean
	 * that the lisitng request is for <code>RosterEntry</code> objects.
	 * @param contentType the type of the listing request
	 * @param filter the filter for the query
	 */
	public void requestListing(String contentType,QueryFilter filter)
	{
		Message info = new Message();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.LIST);
		info.setQueryFilter(filter);
		sheduleAndSend(info);
	}

	// PRIVATE METHODS
	/**
	 * Shedules and sends the given message to the server
	 * @param message the message info object containing the data to send
	 */
	public void sheduleAndSend(final Message message)
	{
		//generate a sequence number for the message
		Random random = new Random();
		message.setSequenceId(netSessionUserName+"_"+String.valueOf(random.nextInt(10000)));
		message.setTimestamp(Calendar.getInstance().getTimeInMillis());
		//create the send kob and put it to wait
		SendJob sendJob = new SendJob(message);
		sendJob.setRule(new SendJobRule());
		sendJob.setPriority(Job.SHORT); 
		sendJob.setSystem(true);
		sendJob.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event) 
			{
				//check if the job was successful
				if(event.getResult() == Status.CANCEL_STATUS)
				{
					firePropertyChangeEvent("NET_TRANSFER_FAILED", null, message);
					log("Failed to send the message: "+message,IStatus.ERROR,null);
				}
			}
		});
		sendJob.schedule();

		//save the last generated sequence
		lastSendSequence = message.getSequenceId();
	}

	/**
	 * Stops all network traffic and opens the wizard to establish a new connection to the server
	 * @param showWizard true if the connection wizard should be shown to reconnect
	 */
	public void requestNetworkStop()
	{
		try
		{
			log("Setting all running network jobs to sleep", Status.INFO,null);
			
			//set the default values
			netSessionUserName = null;
			initialized = false;

			//wait for the listen job to complete
			Job.getJobManager().cancel(JOB_LISTEN);
			for(Job job:Job.getJobManager().find(JOB_LISTEN))
				job.join();

			//wait for the monitor job to complete
			Job.getJobManager().cancel(JOB_MONITOR);
			for(Job job:Job.getJobManager().find(JOB_MONITOR))
				job.join();

			//close the current socket
			NetSource.getInstance().closeConnection();
		}
		catch(Exception e)
		{
			log("Failed to close the network connection: "+e.getMessage(), Status.INFO,e.getCause());
		}
	}

	//THREADS FOR THE NETWORK
	/**
	 * The thread that listens to new data and informs the listeners. 
	 */
	public class ListenJob extends Job
	{
		/**
		 * Default class constructor
		 */
		public ListenJob()
		{
			super(JOB_LISTEN);
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) 
		{
			return JOB_LISTEN.equals(family);
		}

		/**
		 * Loops and receives data.
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			//get the network connection to read data from
			final MySocket client = NetSource.getInstance().getConnection();
			monitor.beginTask("Listening to new data on the network", IProgressMonitor.UNKNOWN);
			while(!monitor.isCanceled())
			{
				try
				{
					String newData = null;
					try
					{
						//get the new data from the socket
						BufferedReader in = client.getBufferedInputStream();
						newData = in.readLine();
						if(newData == null)
							throw new SocketException("Failed to read data from the socket");
					}
					catch(SocketTimeoutException ste)
					{
						//read timeout, ignore and start over
						continue;
					}

					//set up the factory to decode
					XMLFactory xmlFactory = new XMLFactory();
					Message resultMessage = xmlFactory.decode(newData.replaceAll("&lt;br/&gt;", "\n"));

					//get the type of the item
					final String contentType = resultMessage.getContentType();
					final String queryString = resultMessage.getQueryString();
					final String sequenceId = resultMessage.getSequenceId();
					final List<AbstractMessage> messages = resultMessage.getMessageList();

					//check if the sequence is a error message
					if(sequenceId.equalsIgnoreCase("ERROR"))
					{
						//remove the last send message from the list
						messageList.remove(lastSendSequence);
						log("Removed the last send sequenceId, the server reported a error", IStatus.ERROR,null);
					}

					//remove the package from the list
					if(messageList.containsKey(sequenceId))
						messageList.remove(sequenceId);

					//try to get a listener for this message
					ListenerFactory listenerFactory = ListenerFactory.getDefault();
					if(!listenerFactory.hasListeners(contentType))
					{
						log("No listener found for the message type: "+contentType,IStatus.WARNING,null);
						return Status.CANCEL_STATUS;
					}

					IModelListener listener = listenerFactory.getListener(contentType);
					//now pass the message to the listener
					if(IModelActions.ADD.equalsIgnoreCase(queryString))
						listener.add(messages.get(0));
					if(IModelActions.ADD_ALL.endsWith(queryString))
						listener.addAll(messages);
					if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
						listener.remove(messages.get(0));
					if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
						listener.update(messages.get(0));
					if(IModelActions.LIST.equalsIgnoreCase(queryString))
						listener.list(messages);
					if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
						listener.loginMessage(messages.get(0));
					if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
						listener.logoutMessage(messages.get(0));
					if(IModelActions.SYSTEM.equalsIgnoreCase(queryString))
						listener.systemMessage(messages.get(0));		
				}
				catch(SocketException se)
				{
					log("Critical error while listening to new data: "+se.getMessage(), Status.ERROR,se.getCause());
					log("Starting the network wizard from the listen job. Reason:"+se.getMessage(), Status.ERROR,null);
					return Status.CANCEL_STATUS;
				}
				catch(Exception e)
				{
					log("Failed to handle the received data: "+e.getMessage(),IStatus.ERROR,null);
				}
			}
			//the job has ended
			return Status.OK_STATUS;
		}
	}

	/**
	 * Threads that sends the new data to the server and waits for the server reply
	 */
	public class SendJob extends Job
	{
		//properties
		private Message messageInfo;

		/**
		 * Default class constructor to set up the send job
		 * @param messageInfo the message information object to send to the server
		 */
		public SendJob(Message messageInfo)
		{
			super(JOB_SEND);
			this.messageInfo = messageInfo;
			setName("Sende "+messageInfo+" an den Server");
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) 
		{
			return JOB_SEND.equals(family);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				//the package is send now
				messageInfo.setTimestamp(Calendar.getInstance().getTimeInMillis());

				//get the network connection to send data
				final MySocket client = NetSource.getInstance().getConnection();

				monitor.beginTask("Send data to the server", IProgressMonitor.UNKNOWN);
				//setup the facotry
				XMLFactory factory = new XMLFactory();
				String message = factory.encode(messageInfo);
				PrintWriter writer = client.getBufferedOutputStream();
				writer.println(message);
				writer.flush();

				//put the message to the list of packages that are waiting for server reply
				messageList.put(messageInfo.getSequenceId(), messageInfo);

				//wait until the server sends a response
				while(messageList.containsKey(messageInfo.getSequenceId()))
				{
					monitor.beginTask("Warte auf Antwort vom Server", IProgressMonitor.UNKNOWN);
					Thread.sleep(10);
				}
				//everything is ok
				return Status.OK_STATUS;
			}
			catch(Exception e)
			{
				log("Failed to send the package #"+messageInfo.getSequenceId() +" content: " +messageInfo.getContentType(),Status.ERROR,e.getCause());
				log("Starting the network wizard from the send job. Reason:"+e.getMessage(), Status.ERROR,e.getCause());
				return Status.CANCEL_STATUS;
			}
			finally
			{
				monitor.done();
			}
		}
	}

	/**
	 * Monitors the other threads and has the main task to watch whether or not the requests to the server are successfully.
	 * The thread should runn every second to watch the send items.
	 */
	public class MonitorJob extends Job
	{
		/**
		 * Default class constructor 
		 */
		public MonitorJob()
		{
			super(JOB_MONITOR);
		}

		/**
		 * Returns the family to which the job belongs to
		 */
		@Override
		public boolean belongsTo(Object family) 
		{
			return JOB_MONITOR.equals(family);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				monitor.beginTask("Überprüfe gesendete Pakete", messageList.size());
				//loop and chek the send packages
				for(Entry<String, Message> entry:messageList.entrySet())
				{
					Message info = entry.getValue();
					if(info == null)
						continue;
					monitor.subTask("Prüfe Paket Nummer:"+info.getSequenceId());
					//check if the time is greater than 5 seconds and log a warning
					if(info.getCounter() == 5)
						log("WARNING: The package #"+info.getSequenceId()  +" content: " + info.getContentType()+" not been answered by the server in time. Waiting . . .", IStatus.WARNING,null);
					//show a message box to retransmitt the package
					if(info.getCounter() == 10)
					{
						messageList.remove(info.getSequenceId());
						log("The package #"+info.getSequenceId() +" cannot be transmitted to the server. This is a permanent error, I gave up", IStatus.ERROR,null);
						firePropertyChangeEvent("NET_TRANSFER_FAILED", null, info.getMessageList().get(0));				
					}
					//increment the counter by one
					info.setCounter(info.getCounter() +1);
					monitor.worked(1);
				}
			}
			catch(Exception e)
			{
				log("Critical error while monitoring the status of the packages: "+e.getMessage(), Status.ERROR,e.getCause());
			}
			finally
			{
				//if the job is not cancled restart it again
				if(!monitor.isCanceled())
					schedule(1000);
				monitor.done();
			}
			return Status.OK_STATUS;
		}
	}
	
	// LISTENER AND PROPERTY CHANGE METHODS
	/**
	 * Informs all the listeners that the property has changed
	 * @param propertyName the name of the property that has changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	protected void firePropertyChangeEvent(String event,Object oldValue,Object newValue)
	{
		//iterate over the listeners and inform them
		ListIterator<PropertyChangeListener> listIter = netListeners.listIterator();
		while(listIter.hasNext())
		{
			PropertyChangeListener element = (PropertyChangeListener)listIter.next();
			element.propertyChange(new PropertyChangeEvent(this,event,oldValue,newValue));
		}
	}
	
	/**
	 * A public method that allows property to registerproperty change listeners
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) 
	{
		if(!netListeners.contains(listener))
			netListeners.add(listener);
	}

	/**
	 * A public method that allows the listeners to the removed
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
		netListeners.remove(listener);
	}
}
