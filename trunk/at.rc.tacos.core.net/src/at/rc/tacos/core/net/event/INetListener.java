package at.rc.tacos.core.net.event;

import at.rc.tacos.core.net.internal.*;

/**
 * This interface defines the methods that the network layer
 * must provide to communicate with the service 
 * @author Michael
 */
public interface INetListener 
{   
    /**
     * Invoked when new data received.
     */
    public void dataReceived(NetEvent ne);
    
    /**
     * Invoked when the data could not be send
     */
    public void dataTransferFailed(NetEvent ne);
    
    /**
     * Invoked when a socket changed the status
     */
    public void socketStatusChanged(MyClient client,int status);  
}
