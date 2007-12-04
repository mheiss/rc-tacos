package at.rc.tacos.core.net.internal;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * The web client 
 * @author Michael
 */
public class WebClient
{
    //the shared instance
    private static WebClient instance;
    
    //connection to the server
    private MySocket socket;
    
   /**
    * Default class constructr
    */
    private WebClient() { }
    
    /**
     * Returns the shared instance of the client
     * @return the instance
     */
    public static WebClient getInstance()
    {
        if( instance == null)
            instance = new WebClient();
        return instance;
    }
    
    /**
     * Connects to a given server address.
     * @param serverAddress the host name or ip address of the remote host
     * @param serverPort the port number to connect to
     */
    public boolean connect(String serverAddress,int serverPort)
    {
        try
        {
            socket = new MySocket(serverAddress,serverPort);
            socket.createInputStream();
            socket.createOutputStream(); 
            return true;
        }
        catch(UnknownHostException uhe)
        {
            System.out.println("Cannot resole the host name "+serverAddress);
            return false;
        }
        catch (IOException ioe)
        {
            System.out.println("Error cannot connect to the server "+serverAddress+":"+serverPort);
            return false;
        }
    }
    
    /**
     * Sends the given message to the server.<br>
     * The method will wait for a reply from the server
     * @param message the message to send
     */
    public String queryServer(String message)
    {
        try
        {
            //try to send
            socket.sendMessage(message);
            return socket.receiveMessage();
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to get the reply from the server");
            return null;
        }
    }
}
