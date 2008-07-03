package at.rc.tacos.server.net;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import at.rc.tacos.server.net.internal.jobs.ClientListenJob;
import at.rc.tacos.server.net.internal.jobs.TSJ;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.server.net";

	// The shared instance
	private static NetWrapper plugin;	

	//the status of the server connection
	private Helo serverInfo;
	private boolean listening;
	private MyServerSocket clientSocket;
	
	//the socket to listen for other servers
	private MyServerSocket serverSocket1;

	//the listeners
	private ArrayList<PropertyChangeListener> netListeners = new ArrayList<PropertyChangeListener>();

	//the message for the network connection status
	public final static String NET_CONNECTION_OPENED = "netConnectionOpened";
	public final static String NET_CONNECTION_CLOSED = "netConnectionClosed";
	public final static String NET_CONNECTION_ERROR = "netConnectionError";

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
	 * Creates and initializes the server socket so that clients can connect
	 */
	public void initServerSocket(IProgressMonitor monitor)
	{	
		//set the port where the server should listen
		final int port = 4711;

		try
		{
			monitor.subTask("Initialisiere ServerSocket, Port: "+port);
			//create and setup a new server socket
			clientSocket = new MyServerSocket(port);
			clientSocket.setSoTimeout(2000);
			monitor.subTask("Starte den Thread um Clientverbindungen anzunehmen");
			ClientListenJob listenJob = new ClientListenJob(clientSocket);
			listenJob.schedule();	
			
			//create the server info for this server
			serverInfo = new Helo();
			serverInfo.setServerIp(clientSocket.getInetAddress().getHostName());
			serverInfo.setServerPort(clientSocket.getLocalPort());
			serverInfo.setServerPrimary(true);
			
			//update the manager
			ServerManager.getInstance().primaryServerUpdate(serverInfo);

			//inform the viewers that the server is listening
			listening = true;
			firePropertyChangeEvent(NET_CONNECTION_OPENED, null, port);	
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
	public void shutdownServerSocket(IProgressMonitor monitor)
	{
		try
		{			
			//check if we have a running thread that is listening for connections
			Job.getJobManager().cancel(TSJ.SERVER_LISTEN_JOB);
			Job jobs[] = Job.getJobManager().find(TSJ.SERVER_LISTEN_JOB);
			monitor.subTask("Warte auf das Ende des Server Threads");
			for(Job job:jobs)
				job.join();

			//check if we have jobs that are listening for client data
			monitor.subTask("Versuche alle Client Threads zu beenden");
			Job.getJobManager().cancel(TSJ.CLIENT_LISTEN_JOB);
			jobs = Job.getJobManager().find(TSJ.CLIENT_LISTEN_JOB);
			monitor.subTask("Warte auf das Ende der Client Jobs");
			for(Job job:jobs)	
				job.join();
			
			//close the socket
			if(clientSocket != null)
				clientSocket.close();
			
			//update the manager
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
	 * Returns whether the server is listening for new client connections
	 * @return the listening
	 */
	public boolean isListening() 
	{
		return listening;
	}
	
	/**
	 * @return the serverInfo
	 */
	public Helo getServerInfo() {
		return serverInfo;
	}
}
