package at.rc.tacos.core.net.event;

import at.rc.tacos.core.net.MySocket;

/**
 * The net listener interface for the message driven
 * communication between the client/server sockets
 * with the registered listeners.
 * @author Michael
 */
public interface INetListener 
{
    /**
     * Invoked when new data received.
     */
    public void dataReceived(NetEvent ne);
    
    /**
     * Invoked when a socket changed the status
     */
    public void socketStatusChanged(MySocket s,int status);  
}
