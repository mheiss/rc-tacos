package at.rc.tacos.core.net.internal;

import java.io.*;
import java.net.*;
import java.util.Vector;

import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.core.net.event.*;

/**
 * The client object provides a thread to wrap its functionality.
 * With the listener interface other classes can be notfied 
 * if the status of the client has changed or if new data has been received.
 * @author Michael
 */
public class MyClient implements Runnable,IConnectionStates
{
    //properties of a client
    private String serverAddress;
    private int serverPort;

    private MySocket socket;

    //indicates if the thread should be stopped
    protected volatile boolean running;

    /** The <code>netListener</code> for this client */
    protected Vector<INetListener> listenerList = new Vector<INetListener>();

    /**
     * Default constructor
     */
    public MyClient() { }

    /**
     * Constructor for new client, when only the socket is known.
     * @param socket the socket that is connected to the client
     */
    public MyClient(MySocket socket)
    {
        this.socket = socket;
    }

    /**
     * Connects the socket to the given server.
     * @return Returns true if the connection is established, otherwise false
     */
    public boolean connect()
    {
        try
        {
            if (socket == null)
                socket = new MySocket(serverAddress,serverPort);
            socket.createInputStream();
            socket.createOutputStream(); 
            start();
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
     * The main thread to execute the receiving
     */
    public void run()
    {
        //start
        running = true;
        while (running)
        {
            //assert that the socket is valid and connected
            if (socket == null)
            {
                System.out.println("No socket connection available");
                fireSocketStatusChanged(this,IConnectionStates.STATE_DISCONNECTED);
                return;
            }
            if (!socket.isConnected())
            {
                System.out.println("Not connected to a server");
                fireSocketStatusChanged(this,IConnectionStates.STATE_DISCONNECTED);
                return;
            }

            String message = null;
            try
            {
                message = socket.receiveMessage();
                //Create and fire the event
                fireDataReceived(new NetEvent(this,message));  
            }
            catch(java.net.SocketTimeoutException stoe)
            {
                //Thrown by SocketTimeout, so do not react
                //for other things the the client should do :)
            }
            //Force exit if there is an error
            catch(IOException ioe)
            {
                System.out.println("Failed to read data from client");
                System.out.println("Force exit of this client.");
                running = false;
                fireSocketStatusChanged(this,IConnectionStates.STATE_DISCONNECTED);
            }
            catch(NullPointerException npe)
            {
                System.out.println("No stream object to read from.");   
                running = false;
                fireSocketStatusChanged(this,IConnectionStates.STATE_DISCONNECTED);
            }
        };
    }
    
    /**
     * Sends the given message to the server.
     * A transferFailed notification will be fired
     * when the transfer failed.
     * @param message the message to send
     */
    public void sendMessage(String message)
    {
        //assert we have a socket
        if (socket == null)
        {
            fireTransferFailed(new NetEvent(this,message));
            return;
        }
        //try to send
        if(!socket.sendMessage(message))
            fireTransferFailed(new NetEvent(this,message));
    }
    
    /**
     * Returns whether or not the client has a valid network
     * connection.
     * @return true if a connection is established
     */
    public boolean isConnected()
    {
        return socket.isConnected();
    }

    /**
     * Creates and starts a thread to receive messages with this client
     */
    private void start()
    {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Method to request the thread to stop at the next possible time
     */
    public void requestStop()
    {
        //set the status to stop
        running = false;
    }

    /**
     * This method add listeners for the NetEvents
     * @param nl the target class that should reveice NetEvents
     */
    public void addNetListener(INetListener nl)
    {
        listenerList.addElement(nl);   
    }

    /**
     * This method removes the listener class for the NetEvents
     * @param nl the listener to remove
     */
    public void removeNetListener(INetListener nl)
    {
        listenerList.remove(nl);   
    }
    
    /**
     * Removes all registered listeners from the listener list
     */
    public void removeAllNetListeners()
    {
        listenerList.removeAllElements();
    }

    /**
     * This method informs all interested classes that we have new data
     * @param ne the NetEvent to send
     */
    protected void fireDataReceived(NetEvent ne)
    {
        //process the list and notify those that are interested in the event
        for (int i = 0;i<listenerList.size();i++)
            listenerList.get(i).dataReceived(ne);
    }
    
    /**
     *  Informs all listeners that the message could not be send
     *  @param ne the NetEvent holding the message that could 
     *         not be transfered
     */
    protected void fireTransferFailed(NetEvent ne)
    {
      //process the list and notify those that are interested in the event
        for (int i = 0;i<listenerList.size();i++)
            listenerList.get(i).dataTransferFailed(ne);
    }

    /**
     * This method informs all interested classes that the status has changed
     * @param client the client that has changed the status
     * @param status the new status
     */
    protected void fireSocketStatusChanged(MyClient client,int status)
    {
        //process the list and notify those that are interested in the event
        for (int i = 0;i<listenerList.size();i++)
            listenerList.get(i).socketStatusChanged(client,status); 
    }

    //  GETTERS AND SETTERS
    /**
     * Returns the socket object that is used by this client.
     * @return the socket
     */
    public MySocket getSocket()
    {
        return socket;
    }

    /**
     * @return the serverAddress
     */
    public String getServerAddress()
    {
        return serverAddress;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort()
    {
        return serverPort;
    }

    /**
     * @param serverAddress the serverAddress to set
     */
    public void setServerAddress(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }
}
