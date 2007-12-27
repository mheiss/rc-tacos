package at.rc.tacos.core.net;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import at.rc.tacos.core.net.internal.IServerInfo;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.core.net.internal.ServerInfo;

/**
 * This class is responsible for loading the server configuration
 * and managing the connection.
 * @author Michael
 */
public class NetSource
{    
    //config file
    public static final String NET_SETTINGS_BUNDLE_PATH = "at.rc.tacos.core.net.config.server";

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
        initNetwork();
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
    protected void initNetwork()
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
            System.out.println("Missing resource, cannot init network");
            System.out.println(mre.getMessage());
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("Port number must be a integer");
            System.out.println(nfe.getMessage());
        }
        catch(NullPointerException npe)
        {
            System.out.println("Configuration file for the server is missing");
            System.out.println(npe.getMessage());
        }
    }
    
    /**
     * Opens and returns a connection to the server.<br>
     * When the connection cannot be established this method will return null.
     * @param connectionName the name of the connection to use
     * @return the connection or null if it can't be established
     */
    public MyClient getConnection(ServerInfo info)
    {  	
    	//create a new client object
    	MyClient client = new MyClient();
    	client.setServerAddress(info.getHostName());
    	client.setServerPort(info.getPort());
    	//connect and return the connection
    	if(client.connect())
    		return client;
    	//cannot establish a connection
    	return null;
    }
    
    /**
     * Helper method to get the server info by the id
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
    
    
    /**
     *  Returns a list of all server informations
     *  @return the server list
     */
    public List<ServerInfo> getServerList()
    {
    	return serverPool;
    }
}
