package at.rc.tacos.server.net;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;

import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.Message;
import at.rc.tacos.model.Helo;
import at.rc.tacos.model.Session;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.controller.DisoverController;
import at.rc.tacos.server.net.handler.SendHandler;
import at.rc.tacos.server.net.jobs.ClientListenJob;
import at.rc.tacos.server.net.jobs.ServerListenJob;
import at.rc.tacos.server.net.jobs.TSJ;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.server.net";

	// The shared instance
	private static NetWrapper plugin;

	//information about the server
	private Helo serverInfo;
	private boolean listening;

	//the server sockets
	private MyServerSocket serverSocket;
	private MyServerSocket failbackSocket;

	//the listeners
	private ArrayList<PropertyChangeListener> netListeners = new ArrayList<PropertyChangeListener>();

	//the message for the network connection status
	public final static String NET_CONNECTION_OPENED = "netConnectionOpened";
	public final static String NET_CONNECTION_CLOSED = "netConnectionClosed";
	public final static String NET_CONNECTION_ERROR = "netConnectionError";

	//The values from the property store
	private int clientListenPort;
	private int serverListenPort;
	//the failback server
	private String primaryHost;
	private int primaryServerPort; 

	/**
	 * The constructor
	 */
	public NetWrapper() { }

	/**
	 * Called when the plugin is started
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during startup
	 */
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
	public void stop(BundleContext context) throws Exception 
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static NetWrapper getDefault() 
	{
		return plugin;
	}

	/**
	 * Initalizes the server with the values form the preference store
	 */
	public void init(int clientListenPort,int serverListenPort,String failoverHost,int failoverServerPort)
	{
		this.clientListenPort = clientListenPort;
		this.serverListenPort = serverListenPort;
		//the second server
		this.primaryHost = failoverHost;
		this.primaryServerPort = failoverServerPort;
	}

	/**
	 * Creates and initializes the server socket so that clients can connect
	 */
	public void startServer(IProgressMonitor monitor) throws Exception
	{	
		monitor.beginTask("Starte die Netzwerkverbindungen", IProgressMonitor.UNKNOWN);
		
		//setup the server info for this server
		serverInfo = new Helo();
		serverInfo.setServerIp(InetAddress.getLocalHost().getHostName());
		serverInfo.setServerPort(clientListenPort);

		//setup the client server socket
		serverSocket = new MyServerSocket(clientListenPort);
		serverSocket.setSoTimeout(2000);			
		//start the listening thread
		ClientListenJob listenJob = new ClientListenJob(serverSocket);
		listenJob.schedule();	
		
		//setup a server socket to listen for other servers
		failbackSocket = new MyServerSocket(serverListenPort);
		failbackSocket.setSoTimeout(2000);
		ServerListenJob serverListenJob = new ServerListenJob(failbackSocket);
		serverListenJob.schedule();

		//STEP 1: DISCOVER OTHER SERVERS
		log("Checking for other running servers...",IStatus.INFO,null);
		DisoverController discover = new DisoverController(monitor);
		MySocket socket = discover.discoverServer(primaryHost,primaryServerPort);
		discover.disoverComplete(socket, serverInfo);
		
		//inform the viewers that the server is listening
		listening = true;
		NetWrapper.getDefault().startServer();
		firePropertyChangeEvent(NET_CONNECTION_OPENED, null, serverInfo.getServerPort());	
	}

	/**
	 * Shuts down the server socket and closes all connected clients
	 */
	public void shutdownServer(IProgressMonitor monitor) throws Exception
	{
		//shutdown the client server socket
		if(serverSocket != null)
			serverSocket.close();
		//shutdown the failback socket
		if(failbackSocket != null)
			failbackSocket.close();

		//shutdown the server listener
		monitor.subTask("Warte auf das Ende des Server Listen Jobs");
		Job.getJobManager().cancel(TSJ.SERVER_LISTEN_JOB);
		for(Job job:Job.getJobManager().find(TSJ.SERVER_LISTEN_JOB))	
			job.join();

		//shutdown all connected clients
		monitor.subTask("Versuche alle Client Verbindungen zu beenden");
		Job.getJobManager().cancel(TSJ.CLIENT_REQUEST_JOB);
		for(Job job:Job.getJobManager().find(TSJ.CLIENT_REQUEST_JOB))	
			job.join();
		
		//shutdown all connected servers
		monitor.subTask("Versuche alle Server Verbindungen zu beenden");
		Job.getJobManager().cancel(TSJ.SERVER_REQUEST_JOB);
		for(Job job:Job.getJobManager().find(TSJ.SERVER_REQUEST_JOB))
			job.join();
		
		//update the ui
		ServerManager.getInstance().primaryServerUpdate(null);
		ServerManager.getInstance().failbackServerUpdate(null);
		
		//do additional tasks
		stopServer();

		//inform the views
		listening = false;
		firePropertyChangeEvent(NET_CONNECTION_CLOSED, null, null);
	}

	//SERVER EVENTS	
	/**
	 * Called when the server was started to setup additonal jobs
	 */
	public void startServer()
	{
		//log the server startup
		NetWrapper.log("Startup network connection completed", IStatus.INFO, null);
	}

	/**
	 * Called when the server was stopped
	 */
	public void stopServer()
	{
		//log the shutdown
		NetWrapper.log("Shutdown network connection completed", IStatus.INFO, null);
	}

	/**
	 * Called when a new client connected to the server
	 */
	public void sessionCreated(MySocket socket)
	{
		//log the new session
		NetWrapper.log("Created new session", IStatus.INFO, null);

		//create and initialize the new session
		Session session = new Session(socket);
		session.setOnlineSince(Calendar.getInstance().getTimeInMillis());
		SessionManager.getInstance().addUser(session);
	}

	/**
	 * Called when the client session was destroyed
	 */
	public void sessionDestroyed(MySocket socket)
	{
		//log the end of the session
		NetWrapper.log("Session destroyed", IStatus.INFO, null);

		//cancel the current job and release the resources
		ServerContext.getCurrentInstance().release();
	}
	
	/**
	 * Called when the client liste job ended
	 */
	public void serverDestroyed()
	{
		//the primary server went down, so cleanup and shutdown the server
		Job job = new Job("Terminating the server") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try
				{
					//shutdown the server
					shutdownServer(monitor);
					return Status.OK_STATUS;
				}
				catch(Exception e)
				{
					log("Failed to shutdown the server: "+e.getMessage(),IStatus.ERROR,e.getCause());
					return Status.CANCEL_STATUS;
				}
			}
		};
		job.schedule();
	}	
	
	/**
	 * Called when the server request job went down
	 */
	public void serverFailure(MySocket socket)
	{
		//shutdown the socket
		try
		{
			socket.cleanup();
		}
		catch(Exception e)
		{
			NetWrapper.log("Failed to shutdown the socket to the server", IStatus.ERROR, null);
		}
		
		//we are the primary server
		if(ServerManager.getInstance().getPrimaryServer().equals(serverInfo))
		{
			NetWrapper.log("Disconnect of the failback server detected", IStatus.ERROR, null);
			Job infoJob = new Job("Client info job")
			{
				@Override
				protected IStatus run(IProgressMonitor monitor) 
				{
					try
					{
						//STEP 0: Update the ui
						ServerManager.getInstance().failbackServerUpdate(null);
						
						//STEP 1: Inform the clients that the failback went down	
						Message message = new Message();
						message.setUsername("SERVER");
						message.setSequenceId("SERVER_SEQUENCE");
						message.setContentType(SystemMessage.ID);
						message.setQueryString(IModelActions.SYSTEM);
						message.addMessage(new SystemMessage("Die Verbindung zum Failback Server wurde getrennt!\n" +
								"Bitte informieren Sie die Systemadministratoren",SystemMessage.TYPE_ERROR));
						
						//send the message to all clients
						SendHandler sendHandler = new SendHandler(message);
						sendHandler.brodcastMessage();

						return Status.OK_STATUS;
					}
					catch(Exception e)
					{
						NetWrapper.log("Failed to inform the connected clients that the failback went down : "+e.getMessage(), IStatus.ERROR, e.getCause());
						return Status.CANCEL_STATUS;
					}
					finally
					{
						monitor.done();
					}
				}
			};
			infoJob.schedule();
		}
		//we are the failback
		else
		{
			NetWrapper.log("Disconnect of the primary server detected", IStatus.ERROR, null);
			Job job = new Job("Discover server") 
			{
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try
					{
						//STEP 0: Update the ui
						ServerManager.getInstance().primaryServerUpdate(null);
						
						//STEP 1: Try to discover the server
						DisoverController discover = new DisoverController(monitor);
						MySocket socket = discover.discoverServer(primaryHost, primaryServerPort);
						discover.disoverComplete(socket, serverInfo);
						
						//STEP 2: Shutdown the ServerRequestJob cause we take controll
						Job.getJobManager().cancel(TSJ.SERVER_REQUEST_JOB);
						for(Job job:Job.getJobManager().find(TSJ.SERVER_REQUEST_JOB))
							job.join();
						
						//STEP 3: Update the ui
						ServerManager.getInstance().failbackServerUpdate(null);
						
						return Status.OK_STATUS;
					}
					catch(Exception e)
					{
						NetWrapper.log("Failed during the switchover from failback to primary server" , Status.ERROR, e.getCause());
						return Status.CANCEL_STATUS;
					}
					finally
					{
						monitor.done();
					}
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}

	// CHANGE LISTENERS
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

	/**
	 * Logs the error with the build in log
	 */
	public static void log(String message,int severity,Throwable cause)
	{
		NetWrapper.getDefault().getLog().log(new Status(severity,PLUGIN_ID,message,cause));
	}

	//GETTERS AND SETTERS
	/**
	 * Sets the listening state of the server
	 */
	public void setListening(boolean listening)
	{
		this.listening = listening;
	}

	/**
	 * Returns whether the server is listening for new client connections
	 * @return the listening
	 */
	public boolean isListening() 
	{
		return listening;
	}

	/**
	 * Sets the server info object for this server
	 */
	public void setServerInfo(Helo serverInfo)
	{
		this.serverInfo = serverInfo;
	}

	/**
	 * @return the serverInfo
	 */
	public Helo getServerInfo() {
		return serverInfo;
	}
}
