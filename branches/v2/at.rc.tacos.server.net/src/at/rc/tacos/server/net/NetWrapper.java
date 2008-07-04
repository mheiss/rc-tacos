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

import at.rc.tacos.model.Helo;
import at.rc.tacos.model.Session;
import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.controller.DisoverController;
import at.rc.tacos.server.net.jobs.ClientListenJob;
import at.rc.tacos.server.net.jobs.ServerListenJob;
import at.rc.tacos.server.net.jobs.ServerRequestJob;
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
	private int listenPort;
	private String primaryHost;
	private int primaryPort;

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
	public void init(int listenPort,String failoverHost,int failoverPort)
	{
		this.listenPort = listenPort;
		this.primaryHost = failoverHost;
		this.primaryPort = failoverPort;
	}

	/**
	 * Creates and initializes the server socket so that clients can connect
	 */
	public void startServer(IProgressMonitor monitor)
	{	
		monitor.beginTask("Starte die Netzwerkverbindungen", IProgressMonitor.UNKNOWN);

		try
		{
			//the server info about this server
			serverInfo = new Helo();
			serverInfo.setServerIp(InetAddress.getLocalHost().getHostName());
			serverInfo.setServerPort(listenPort);

			//setup this server
			serverSocket = new MyServerSocket(listenPort);
			serverSocket.setSoTimeout(2000);			
			//start the listening thread
			ClientListenJob listenJob = new ClientListenJob(serverSocket);
			listenJob.schedule();	

			//STEP 1: DISCOVER OTHER SERVERS
			log("Checking for other running servers...",IStatus.INFO,null);
			DisoverController discover = new DisoverController(monitor);
			MySocket socket = discover.discoverServer(serverInfo,primaryHost,primaryPort);
			if(socket != null)
			{
				//STEP2: Start the thread to listen to data from the server
				ServerRequestJob serverRequestJob = new ServerRequestJob(socket);
				serverRequestJob.schedule();
			}
			else
			{
				//setup a server socket to listen for other servers
				failbackSocket = new MyServerSocket(primaryPort);
				failbackSocket.setSoTimeout(2000);
				ServerListenJob serverListenJob = new ServerListenJob(failbackSocket);
				serverListenJob.schedule();
				//this server will be the primary server
				serverInfo = new Helo();
				serverInfo.setServerIp(InetAddress.getLocalHost().getHostName());
				serverInfo.setServerPort(listenPort);
				serverInfo.setServerPrimary(true);
				ServerManager.getInstance().primaryServerUpdate(serverInfo);			
				log("No other running server found, this server will be the primary",IStatus.INFO,null);
			}
			//inform the viewers that the server is listening
			listening = true;
			firePropertyChangeEvent(NET_CONNECTION_OPENED, null, serverInfo.getServerPort());	
		}
		catch(Exception e)
		{
			listening = false;
			NetWrapper.log("Network error while trying to create the server socket", Status.ERROR, e.getCause());
			firePropertyChangeEvent(NET_CONNECTION_CLOSED, null, e.getMessage());
		}
	}

	/**
	 * Shuts down the server socket and closes all connected clients
	 */
	public void shutdownServer(IProgressMonitor monitor)
	{
		try
		{	
			//shutdown the server listener
			monitor.subTask("Warte auf das Ende des Server Listen Jobs");
			Job.getJobManager().cancel(TSJ.SERVER_LISTEN_JOB);
			Job[] jobs = Job.getJobManager().find(TSJ.SERVER_LISTEN_JOB);
			for(Job job:jobs)	
				job.join();

			//shutdown all connected clients
			monitor.subTask("Versuche alle Client Verbindungen zu beenden");
			Job.getJobManager().cancel(TSJ.CLIENT_REQUEST_JOB);
			jobs = Job.getJobManager().find(TSJ.CLIENT_REQUEST_JOB);
			for(Job job:jobs)	
				job.join();

			//shutdown the server socket
			if(serverSocket != null)
				serverSocket.close();
			//shutdown the failback socket
			if(failbackSocket != null)
				failbackSocket.close();

			//update the manager
			ServerManager.getInstance().failbackServerUpdate(null);
			ServerManager.getInstance().primaryServerUpdate(null);

			//inform the views
			listening = false;
			firePropertyChangeEvent(NET_CONNECTION_CLOSED, null, null);
		}
		catch(Exception e)
		{
			listening = true;
			NetWrapper.log("Network error while trying to close the server socket", Status.ERROR, e.getCause());
			firePropertyChangeEvent(NET_CONNECTION_ERROR, null, e.getMessage());
			e.printStackTrace();
		}
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
