package at.rc.tacos.core.net;

import java.io.*;
import java.net.*;
import java.util.Vector;

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
     * The listeners will be notifyed uppon the status of the socket.
     * @return true if a connection has been established otherwise false
     */
    public void connect()
    {
        try
        {
            if (socket == null)
                socket = new MySocket(serverAddress,serverPort);
            socket.createInputStream();
            socket.createOutputStream(); 
            fireSocketStatusChanged(socket, IConnectionStates.STATE_CONNECTED);
            start();
        }
        catch(UnknownHostException uhe)
        {
            System.out.println("Cannot resole the host name "+serverAddress);
            fireSocketStatusChanged(socket, IConnectionStates.STATE_DISCONNECTED_NETWORK);
        }
        catch (IOException ioe)
        {
            System.out.println("Error cannot connect to the server");
            fireSocketStatusChanged(socket, IConnectionStates.STATE_DISCONNECTED_NETWORK);
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
                return;
            }
            if (!socket.isConnected())
            {
                System.out.println("Not connected to a server");
                return;
            }

            String message = null;
            try
            {
                message = socket.receiveMessage();
                //Create a new NetEvent and fire it
                NetEvent ne = new NetEvent(socket,message);
                fireDataReceived(ne);  
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
                fireSocketStatusChanged(getSocket(),IConnectionStates.STATE_DISCONNECTED_NETWORK);
            }
            catch(NullPointerException npe)
            {
                System.out.println("No stream object to read from.");   
                running = false;
                fireSocketStatusChanged(getSocket(),IConnectionStates.STATE_DISCONNECTED_NETWORK);
            }
        };
    }

    /**
     * Method to start the main thread of the client
     */
    public void start()
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
     * This method sets the listener class for the NetEvents
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
     * This method informs all interested classes that the status has changed
     * @param socket the socket that has changed the status
     * @param status the new status
     */
    protected void fireSocketStatusChanged(MySocket socket,int status)
    {
        //process the list and notify those that are interested in the event
        for (int i = 0;i<listenerList.size();i++)
            listenerList.get(i).socketStatusChanged(socket,status); 
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
