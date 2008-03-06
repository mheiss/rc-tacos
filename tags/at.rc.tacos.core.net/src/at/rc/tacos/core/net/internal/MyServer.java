package at.rc.tacos.core.net.internal;

import java.io.*;
import java.util.Vector;

import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.core.net.event.*;

/**
 * The server socket class provides a thread to accept
 * new client connections and notifies the listeners
 * when the status of a client changed.
 * @author Michael
 */
public class MyServer implements Runnable,IConnectionStates
{                
    //the port for the Server
    private int port;

    /** 
     * The <code>netListener</code> for this server to notify
     */
    protected Vector<INetListener> listenerList = new Vector<INetListener>();
    
    //indicates if the server thread should be stopped
    protected volatile boolean running;
    
    /**
     * Construct a new myServer object with given port number and ControllerClass.<br>
     * @param port the port that the server will listen to
     * @param serverController the controller, the events and messages will be send there
     */
    public MyServer(int port)
    {
        this.port = port;
    }

    //endless loop so that clients can connect
    public void run()
    { 
        running = true;
        //the socket, accepts the incoming connections
        MyServerSocket server = null;
        //set up the server
        try
        {
            server = new MyServerSocket(port);
        }
        catch(IOException ioe)
        {
            System.out.println("Port "+port+" in use or unusable.\n");
            running = false;
        }
        while (running)
        { 
            MySocket socket = null;
            try
            {
                //wait for and accept incoming connections, notify the listeners
                socket = server.accept();
                fireSocketStatusChanged(new MyClient(socket), IConnectionStates.STATE_CONNECTED);
            }
            catch(java.net.SocketTimeoutException stoe)
            {
                //Thrown by SocketTimeout, so do not react
                
                //for other things the server should do :)
            }
            catch(IOException ioe)
            {
                System.out.println("Could not get incomming connection.");
            }
            //force exit, if we do not have a socket object
            catch(NullPointerException npe)
            {
                System.out.println("Cannot create a new socket, exiting now.");
                running = false;
            }
        }
        //Main thread finished so exit the thread
        System.out.println("Server shuting down, Bye :)");
    }
    
    /**
     * This method sets the listener class for the NetEvents
     * @param nl the target class that should receive NetEvents
     */
    public void addNetListener(INetListener nl)
    {
        listenerList.add(nl);   
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
}
