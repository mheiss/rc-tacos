package at.rc.tacos.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;

import at.rc.tacos.common.IServerInfo;
import at.rc.tacos.model.ServerInfo;
import at.rc.tacos.net.MySocket;

/**
 * This class is responsible for loading the server configuration
 * and managing the connection.
 * @author Michael
 */
public class NetSource
{    
	//config file
	public static final String NET_SETTINGS_BUNDLE_PATH = "at.rc.tacos.client.net.config.server";

	//the connected socket
	private MySocket client;
	
	//the current server
	private ServerInfo currentServer;

	//the server pool
	private static NetSource instance;
	private ArrayList<ServerInfo> serverPool;

	/**
	 * Default class constructor
	 */
	private NetSource() 
	{ 
		//create the server list
		serverPool = new ArrayList<ServerInfo>();

		//init the network and load the data
		loadServerList();
	}

	/**
	 * Returns the instance of the network source
	 * @return the shared instance
	 */
	public static NetSource getInstance()
	{
		if (instance == null)
			instance = new NetSource();
		return instance;
	}

	/**
	 * Loads the configuration for the servers from the config file
	 * @return true if the initialisation was successfully
	 */
	protected void loadServerList()
	{    
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle(NetSource.NET_SETTINGS_BUNDLE_PATH);
			//primary server         
			String primHost = bundle.getString("server.host");
			int primPort = Integer.parseInt(bundle.getString("server.port"));
			String primDesc = bundle.getString("server.description");
			//failover
			String failHost = bundle.getString("failover.host");
			int failPort = Integer.parseInt(bundle.getString("failover.port"));
			String failDesc = bundle.getString("failover.description");

			//add servers
			serverPool.add(new ServerInfo(IServerInfo.PRIMARY_SERVER,primHost,primPort,primDesc));
			serverPool.add(new ServerInfo(IServerInfo.FAILOVER_SERVER,failHost,failPort,failDesc));
		}
		catch(MissingResourceException mre)
		{
			NetWrapper.log("Missing resource, cannot init network: "+mre.getMessage(),IStatus.ERROR,mre.getCause());
		}
		catch(NumberFormatException nfe)
		{
			NetWrapper.log("Port number must be a integer: "+nfe.getMessage(),IStatus.ERROR,nfe.getCause());
		}
		catch(NullPointerException npe)
		{
			NetWrapper.log("Configuration file for the server is missing: "+npe.getMessage(),IStatus.ERROR,npe.getCause());
		}
	}

	/**
	 * Returns the currently established connection to the server
	 * @param the opened connection or null in case of an error
	 */
	public MySocket getConnection()
	{
		return client;
	}

	/**
	 * Opens or reuses the existing connection to the server.<br>
	 * When the connection cannot be established this method will return null.
	 * @param connectionName the name of the connection to use
	 * @param forceNew a flag to indicate whether a new connection should be established or a old one can be used
	 * @return the connection or null if it can't be established
	 */
	public MySocket openConnection(ServerInfo info)
	{  	
		//try to open a socket to the server
		try
		{
			NetWrapper.log("Open a new connection to: "+info.getHostName()+":"+info.getPort(),IStatus.INFO,null);
			client = new MySocket(info.getHostName(),info.getPort());
			client.setSoTimeout(2000);
			
			//save the server
			currentServer = info;
			
			return client;
		}
		catch(UnknownHostException uhe)
		{
			NetWrapper.log("Failed to open a connection to the host: "+info.getHostName(),IStatus.ERROR,uhe.getCause());
			return null;
		}
		catch(IOException ioe)
		{
			NetWrapper.log("IO-Error during the socket creation: "+ioe.getMessage(),IStatus.ERROR,ioe.getCause());
			return null;
		}
	}

	/**
	 * Closes the current connection
	 */
	public void closeConnection() throws IOException
	{
		//assert valid
		if(client == null)
			return;
		client.cleanup();
		client = null;
	}

	/**
	 * Returns the output stream to write data to the server
	 * @return the print writer to write to the server
	 * @throws IOException if a communication (TCP) error occurs
	 * @throws IllegalStateException if the socket is not connected or the output stream of the socket is invalid
	 */
	public PrintWriter getOutputStream() throws IOException,IllegalStateException
	{
		//assert valid
		if(client == null)
			throw new IllegalStateException("The socket is not connected to a server. Open a connection to the server first");
		if(client.getBufferedOutputStream() == null)
			throw new IllegalStateException("Failed to get the output stream to the server");
		//return the writer
		return client.getBufferedOutputStream();
	}

	/**
	 * Returns the input stream to read data from the server
	 * @return the buffered reader to read data
	 * @throws 
	 */
	public BufferedReader getInputStream() throws IOException,IllegalStateException
	{
		//assert valid
		if(client == null)
			throw new IllegalStateException("The socket is not connected to a server. Open a connection to the server first");
		if(client.getBufferedInputStream() == null)
			throw new IllegalStateException("This socket has no connected input stream");
		//return the reader
		return client.getBufferedInputStream();
	}

	/**
	 *  Returns a list of all server informations entries
	 *  @return the server list
	 */
	public List<ServerInfo> getServerList()
	{
		return serverPool;
	}

	/**
	 * Helper method to get the server info by the id.
	 * @return the server information entry found or null
	 */
	public ServerInfo getServerInfoById(String id)
	{
		for(ServerInfo info:serverPool)
		{
			if(info.getId().equalsIgnoreCase(id))
				return info;
		}
		//nothing found
		return null;
	}
	
	//GETTERS AND SETTERS
	/**
	 * Returns the server info record of the last connected server.
	 */
	public ServerInfo getCurrentServer() 
	{
		return currentServer;
	}

	/**
	 * Sets the server info record of the current connected server.
	 */
	public void setCurrentServer(ServerInfo currentServer) 
	{
		this.currentServer = currentServer;
	}
}
