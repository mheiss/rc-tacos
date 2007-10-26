package at.rc.tacos.core.net;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.core.net.event.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.net";

	// The shared instance
	private static NetWrapper plugin;
	
	//the connection
    private MyClient myClient;
	
	/**
	 * The constructor
	 */
	public NetWrapper() 
	{
	    myClient = new MyClient();
        plugin = this;
	}

	/**
     * Called when the plugin is started
     */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
	}

	/**
	 * Called when the plugin is stopped
	 */
	public void stop(BundleContext context) throws Exception 
	{
	    super.stop(context);
		plugin = null;
        //close the netowrk connection
        myClient.requestStop();
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
     * Adds a listener class to the client object
     * @param the listern class
     */
    public void addListener(INetListener listener)
    {
        myClient.addNetListener(listener);
    }
    
    /**
     * Creates a <code>MyClient</code> object with the given ip address and port number.
     * The communication with the server will be handled in an own thread.
     * @param host the host name to connect to
     * @param port the port of the server to connect to
     */
    public void connect(String host,int port)
    {
        myClient.setServerAddress(host);
        myClient.setServerPort(port);
        myClient.connect();
   }
    
    /**
     * Sends the given message to the remote host
     * @param message the message to send
     */
    public void sendMessage(String message)
    {
        myClient.getSocket().sendMessage(message);
    }
}
