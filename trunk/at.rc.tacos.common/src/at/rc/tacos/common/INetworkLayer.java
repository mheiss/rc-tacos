package at.rc.tacos.common;

import java.util.ArrayList;

/**
 * This interface describes the methods that the network
 * layer must provide to communicate with the service.
 * @author Michael
 */
public interface INetworkLayer
{
    //LISTENER SUPPORT
    /**
     * Registers the service layer to receive network updates
     * @param listener the listener to add
     */
    public void registerNetworkListener(INetworkListener listener);
    
    /**
     * Remove the listener.
     * @param listener the listener to remove
     */
    public void removeNetworkListener(INetworkListener listener);
    
    // NETWORK METHODS
    /**
     * Sets the session parameters for the network communication.
     * @param userID the user identification
     */
    public void setSessionParameter(String userId);
    
    /**
     * Fired when the client wants to send one ore more objects to the server.
     * The type of the object is important when the message is deserialized again
     * to determine which object it is.
     * @param objectType the identification of the object
     * @param action the action that that is done
     * @param object a list of object that should be transmitted
     */
    public void fireNetworkMessage(String objectType,String action,ArrayList<AbstractMessage> object); 
    
    /**
     * Fired when the client wants to send one object to the server.
     * The type of the object is important when the message is deserialized again
     * to determine which object it is.
     * @param objectType the identification of the object
     * @param action the action that is done
     * @param object the object to transmitt
     */
    public void fireNetworkMessage(String objectType,String action,AbstractMessage object); 
}
