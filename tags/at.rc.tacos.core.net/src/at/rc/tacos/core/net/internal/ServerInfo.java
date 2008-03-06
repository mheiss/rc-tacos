package at.rc.tacos.core.net.internal;

/**
 * This class contains information about a server.
 * @author Michael
 */
public class ServerInfo 
{
	//properties
	private String id;
	private String host;
	private int port;
	private String description;
	
	/**
	 * Default constructor for a server info
	 * @param id the unique identification string
	 * @param host the ip address or host name
	 * @param port the port number of the server
	 * @param description the description to identify the server
	 */
	public ServerInfo(String id,String host,int port,String description)
	{
		this.id = id;
		this.host = host;
		this.port = port;
		this.description = description;
	}
	
	//GETTERS FOR THE VALUES
	
	/**
	 * Returns the uniqe identification string of this server
	 * @return the identification string
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Returns the description text of this server
	 * @return the description text to display
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the host name or the ip address of the server
	 * @return the hostname or ip address
	 */
	public String getHostName()
	{
		return host;
	}
	
	/**
	 * Returns the port to which the server is listening
	 * @return port the port number
	 */
	public int getPort()
	{
		return port;
	}
}
