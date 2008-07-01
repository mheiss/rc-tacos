package at.rc.tacos.server.net;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.BundleContext;

import at.rc.tacos.model.OnlineUser;
import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.server.net.internal.jobs.ServerListenJob;

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
	private boolean listening;
	private MyServerSocket serverSocket;

	//the job definitions
	public final static String CLIENT_LISTEN_JOB = "clientListenJob";
	public final static String PROCESS_DATA_JOB = "processDataJob";
	public final static String SEND_DATA_JOB = "sendDataJob";
	public final static String SERVER_LISTEN_JOB = "serverListenJob";

	//the listeners
	private ArrayList<PropertyChangeListener> netListeners = new ArrayList<PropertyChangeListener>();

	//the fired properties
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
			serverSocket = new MyServerSocket(port);
			serverSocket.setSoTimeout(2000);
			monitor.subTask("Starte den Thread um Clientverbindungen anzunehmen");
			ServerListenJob listenJob = new ServerListenJob(serverSocket);
			listenJob.schedule();	

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
			Job.getJobManager().cancel(SERVER_LISTEN_JOB);
			Job jobs[] = Job.getJobManager().find(SERVER_LISTEN_JOB);
			monitor.subTask("Warte auf das Ende des Server Threads");
			for(Job job:jobs)
				job.join();

			//check if we have jobs that are proccessing data
			monitor.subTask("Versuche alle Threads Datenverarbeitungs Threads zu beenden");
			Job.getJobManager().cancel(PROCESS_DATA_JOB);
			jobs = Job.getJobManager().find(PROCESS_DATA_JOB);
			monitor.subTask("Warte auf das Ende des Datenverarbeitungs Threads");
			for(Job job:jobs)	
				job.join();

			//check if we have jobs that are listening for client data
			monitor.subTask("Versuche alle Client Threads zu beenden");
			Job.getJobManager().cancel(CLIENT_LISTEN_JOB);
			jobs = Job.getJobManager().find(CLIENT_LISTEN_JOB);
			monitor.subTask("Warte auf das Ende der Client Jobs");
			for(Job job:jobs)	
				job.join();

			//shut down all connections
			monitor.subTask("Trenne alle Client Verbindungen");
			ListIterator<OnlineUser> listIter = OnlineUserManager.getInstance().getOnlineUsers().listIterator();
			while(listIter.hasNext())
			{
				try
				{
					OnlineUser nextUser = listIter.next();
					nextUser.getSocket().cleanup();
					listIter.remove();
					OnlineUserManager.getInstance().userRemoved(nextUser);
				}
				catch(IOException ioe)
				{
					NetWrapper.log("Network error while closing the socket: "+ioe.getMessage(), Status.ERROR, ioe.getCause());
					ioe.printStackTrace();
				}
			}
			
			//close the socket
			if(serverSocket != null)
				serverSocket.close();

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

	// CHANGE LISTENERS
	/**
	 * Informs all the listeners that the property has changed
	 * @param propertyName the name of the property that has changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	private void firePropertyChangeEvent(String event,Object oldValue,Object newValue)
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
	public boolean isListening() {
		return listening;
	}
}
