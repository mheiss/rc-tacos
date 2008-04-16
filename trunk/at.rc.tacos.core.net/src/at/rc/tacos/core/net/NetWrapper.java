package at.rc.tacos.core.net;

import java.io.BufferedReader;
import java.io.PrintWriter;
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

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.IModelListener;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SystemMessage;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.net";
	private static NetWrapper plugin;

	//the username of the logged in user
	private String netSessionUserName;

	//the list of send items, which have currently no answer from the server
	//if a answere from the server is recevied the items is deleted from the list
	private ConcurrentHashMap<String,AbstractMessageInfo> messageList;

	//the running jobs
	private ListenJob listenJob;
	private KeepAliveJob keepAliveJob;
	private MonitorJob monitorJob;

	/**
	 * The constructor
	 */
	public NetWrapper() 
	{
		messageList = new ConcurrentHashMap<String, AbstractMessageInfo>();
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
		//start the thread to listen to new data
		startListenJob();
		//start the thread to check the server connection
		startKeepAliveJob();
		//the monitor thread
		startMonitorThread();
		//log to the client
		IModelListener systemListener = ListenerFactory.getDefault().getListener(SystemMessage.ID);
		systemListener.log("Starting up the network jobs", Status.INFO);
	}

	/**
	 * Stops all running jobs
	 */
	public void cancelJobs()
	{
		listenJob.cancel();
		keepAliveJob.cancel();
		monitorJob.cancel();
		//log
		IModelListener systemListener = ListenerFactory.getDefault().getListener(SystemMessage.ID);
		systemListener.log("Setting all running network jobs to sleep", Status.INFO);
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
		if(listenJob == null)
			listenJob = new ListenJob();
		//wake up if he is sleeping
		listenJob.schedule();
		listenJob.shouldRun();
	}

	/**
	 * Sends KEEP_ALIVE messages to the server to check the connection regularly
	 */
	private void startKeepAliveJob()
	{
		//create the job if we do not have one
		if(keepAliveJob == null)
			keepAliveJob = new KeepAliveJob();
		//wake up if he is sleeping
		keepAliveJob.schedule();
		keepAliveJob.shouldRun();
	}

	/**
	 * Monitors the send packages and informs when the server sends no reply
	 */
	private void startMonitorThread()
	{
		//create the job if we do not have one
		if(monitorJob == null)
			monitorJob = new MonitorJob();	
		//wake up if he is sleeping
		monitorJob.schedule();
		monitorJob.shouldRun();
	}

	// METHODS TO SEND MESSAGES
	/**
	 * Sends a request to the server to login the user.
	 * @param login the authentication information to use
	 */
	public void sendLoginMessage(Login login)
	{
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(Login.ID);
		info.setQueryString(IModelActions.LOGIN);
		info.setMessage(login);
		sheduleAndSend(info);
	}

	/**
	 * Sends a request to the server to logout the user.
	 * @param logout the logut message
	 */
	public void sendLogoutMessage(Logout logout)
	{
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(Logout.ID);
		info.setQueryString(IModelActions.LOGOUT);
		info.setMessage(logout);
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
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.ADD);
		info.setMessage(addMessage);
		sheduleAndSend(info);
	}

	/**
	 * Same as the send add message method but allows to send a list of data instead of a single object.
	 * @param contentType the type of the message
	 * @param addList the list of object to send
	 */
	public void sendAddAllMessage(String contentType,List<AbstractMessage> addList)
	{
		AbstractMessageInfo info = new AbstractMessageInfo();
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
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.REMOVE);
		info.setMessage(removeMessage);
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
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.UPDATE);
		info.setMessage(updateMessage);
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
		AbstractMessageInfo info = new AbstractMessageInfo();
		info.setContentType(contentType);
		info.setQueryString(IModelActions.LIST);
		info.setQueryFilter(filter);
		sheduleAndSend(info);
	}

	// PRIVATE METHODS
	/**
	 * Shedules and sends the given message to the server
	 * @param info the message info object containing the data to send
	 */
	public void sheduleAndSend(final AbstractMessageInfo info)
	{
		//generate a sequence number for the message
		Random random = new Random();
		info.setSequenceId(netSessionUserName+"_"+String.valueOf(random.nextInt(10000)));
		info.setTimestamp(Calendar.getInstance().getTimeInMillis());
		//shedule and start the job
		SendJob sendJob = new SendJob(info);
		sendJob.schedule();
		sendJob.addJobChangeListener(new JobChangeAdapter()
		{
			//check the result of the job
			@Override
			public void done(IJobChangeEvent event) 
			{
				//check if the message was successfully send
				if(event.getResult() == Status.OK_STATUS)
				{
					messageList.put(info.getSequenceId(), info);
					System.out.println("Adding package number:"+info.getSequenceId());
				}
			}
		});
		//do not watch KEEP_ALIVE packages
		if(info.getQueryString() == IModelActions.KEEP_ALIVE)
			return;

		//setup the progress monitor job
		ProgressJob progressJob = new ProgressJob(info.getSequenceId());
		progressJob.setUser(true);
		progressJob.schedule();
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
			super("ListenJob");
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
			try
			{
				//get the reader to get new data
				BufferedReader in = client.getBufferedInputStream();

				String newData = null;
				//wait and read the next new line from the stream
				try
				{
					newData = in.readLine();
				}
				catch(Exception e)
				{
					//read timeout so go on;
				}

				//assert valid
				if(newData == null)
					return Status.CANCEL_STATUS;

				//set up the factory to decode
				XMLFactory xmlFactory = new XMLFactory();
				ListenerFactory listenerFactory = ListenerFactory.getDefault();
				ArrayList<AbstractMessage> newObjects = new ArrayList<AbstractMessage>();

				//replace all characters
				String message = newData.replaceAll("&lt;br/&gt;", "\n");
				xmlFactory.setupDecodeFactory(message);
				//decode the message
				newObjects = xmlFactory.decode();
				//get the type of the item
				final String contentType = xmlFactory.getContentType();
				final String queryString = xmlFactory.getQueryString();
				final String userId = xmlFactory.getUserId();
				final String sequenceId = xmlFactory.getSequenceId();
				System.out.println("Received: "+ userId+","+contentType+","+queryString);

				//try to get a listener for this message
				if(!listenerFactory.hasListeners(contentType))
				{
					System.out.println("No listener found for the message type: "+contentType);
					return Status.CANCEL_STATUS;
				}

				IModelListener listener = listenerFactory.getListener(contentType);
				//now pass the message to the listener
				if(IModelActions.ADD.equalsIgnoreCase(queryString))
					listener.add(newObjects.get(0));
				if(IModelActions.ADD_ALL.endsWith(queryString))
					listener.addAll(newObjects);
				if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
					listener.remove(newObjects.get(0));
				if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
					listener.update(newObjects.get(0));
				if(IModelActions.LIST.equalsIgnoreCase(queryString))
					listener.list(newObjects);
				if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
					listener.loginMessage(newObjects.get(0));
				if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
					listener.logoutMessage(newObjects.get(0));
				if(IModelActions.SYSTEM.equalsIgnoreCase(queryString))
					listener.systemMessage(newObjects.get(0));

				//remove the package from the list
				if(messageList.containsKey(sequenceId))
					messageList.remove(sequenceId);
			}
			catch(final Exception e)
			{
				System.out.println("Exception during the network read");
				e.printStackTrace();				
			}
			finally
			{
				if(!monitor.isCanceled())
					schedule();
				monitor.done();
			}
			return Status.OK_STATUS;
		}
	}

	/**
	 * Threads that sends the new data to the server
	 */
	public class SendJob extends Job
	{
		//properties
		private AbstractMessageInfo messageInfo;

		/**
		 * Default class constructor to set up the send job
		 * @param messageInfo the message information object to send to the server
		 */
		public SendJob(AbstractMessageInfo messageInfo)
		{
			super("SendJob");
			this.messageInfo = messageInfo;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			//setup the facotry
			XMLFactory factory = new XMLFactory();
			factory.setUserId(netSessionUserName);
			factory.setTimestamp(messageInfo.getTimestamp());
			factory.setContentType(messageInfo.getContentType());
			factory.setQueryString(messageInfo.getQueryString());
			factory.setSequenceId(messageInfo.getSequenceId());
			factory.setFilter(messageInfo.getQueryFilter());

			//get the network connection to send data
			final MySocket client = NetSource.getInstance().getConnection();
			monitor.beginTask("Sending data", IProgressMonitor.UNKNOWN);
			try
			{
				String message = factory.encode(messageInfo.getMessageList());
				PrintWriter writer = client.getBufferedOutputStream();
				writer.println(message);
				writer.flush();
			}
			catch(Exception e)
			{
				System.out.println("Failed to send the data");
				e.printStackTrace();
				return Status.CANCEL_STATUS;
			}
			finally
			{
				monitor.done();
			}
			//everything is ok :)
			return Status.OK_STATUS;
		}
	}

	/**
	 * The keep alive thrad that sends regularly system messages
	 */
	public class KeepAliveJob extends Job
	{
		/**
		 * Default class constructor
		 */
		public KeepAliveJob()
		{
			super("KeepAlive");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				monitor.beginTask("Sending keep alive message", IProgressMonitor.UNKNOWN);
				//setup the keep alive package
				AbstractMessageInfo info = new AbstractMessageInfo();
				info.setContentType(SystemMessage.ID);
				info.setQueryString(IModelActions.KEEP_ALIVE);
				sheduleAndSend(info);
			}
			catch(Exception e)
			{
				System.out.println("Failed to send the data");
				e.printStackTrace();
			}
			finally
			{
				if(!monitor.isCanceled())
					schedule(5000);
				monitor.done();
			}
			//everything is ok :)
			return Status.OK_STATUS;
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
			super("MonitorJob");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				monitor.beginTask("Checking send packages", messageList.size());
				//loop and chek the send packages
				for(Entry<String, AbstractMessageInfo> entry:messageList.entrySet())
				{
					AbstractMessageInfo info = entry.getValue();
					if(info == null)
						continue;
					//the time difference between now and the send time
					long time = Calendar.getInstance().getTimeInMillis() - info.getTimestamp();
					int seconds = (int)(time/1000) % 60;
					//check if the time is greater than 5 seconds
					if(seconds > 5)
					{
						IModelListener systemListener = ListenerFactory.getDefault().getListener(SystemMessage.ID);
						systemListener.log("WARNING: Package has not been answered by the server:"+info, IStatus.ERROR);
						info.setTransmitted(info.getTransmitted()+1);
					}
					if(info.getTransmitted() >= 2)
					{
						IModelListener systemListener = ListenerFactory.getDefault().getListener(SystemMessage.ID);
						if(info.getQueryString().equalsIgnoreCase(IModelActions.KEEP_ALIVE))
						{
							netSessionUserName = null;
							//close the current socket
							NetSource.getInstance().closeConnection();
							//cancel the running jobs and inform the user
							cancelJobs();
							systemListener.connectionChange(IConnectionStates.STATE_DISCONNECTED);
						}
						else
						{
							systemListener.transferFailed(info);
							systemListener.log("The package #"+info.getSequenceId() +" cannot be transmitted to the server. This is a permanent error, I gave up", IStatus.ERROR);
						}
						messageList.remove(info.getSequenceId());
					}
					monitor.worked(1);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(!monitor.isCanceled())
					schedule(1000);
				monitor.done();
			}
			return Status.OK_STATUS;
		}
	}

	/**
	 * Shows that a request is currently in progress
	 */
	public class ProgressJob extends Job
	{
		//the number of the package waiting for
		private String sequenceId;

		/**
		 * Default class constructor defining the package to wait for
		 */
		public ProgressJob(String sequenceId)
		{
			super("ProgressJob@"+sequenceId);
			this.sequenceId = sequenceId;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				monitor.beginTask("Daten werden vom Server abgerufen", IProgressMonitor.UNKNOWN);
				while(messageList.containsKey(sequenceId))
				{
					Thread.sleep(1000);
				}
				return Status.OK_STATUS;
			}
			catch(Exception e)
			{
				System.out.println("Failed to watch the progress for the package: "+sequenceId);
				return Status.CANCEL_STATUS;
			}
			finally
			{
				monitor.done();
			}
		}
	}
}
