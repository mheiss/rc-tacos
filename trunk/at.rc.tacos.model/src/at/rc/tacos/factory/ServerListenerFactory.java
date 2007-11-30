package at.rc.tacos.factory;

import java.util.HashMap;
import at.rc.tacos.common.IServerListener;

/**
 * This class handles the listener classes for client request
 * @author Michael
 */
public class ServerListenerFactory
{
    //the shared instance
    private static ServerListenerFactory instance;
    //the listener classes
    private HashMap<String, IServerListener> serverListeners;
    
    /**
     * Default private class constructor
     */
    private ServerListenerFactory()
    {
        serverListeners = new HashMap<String, IServerListener>();
    }
    
    /**
     * Returns the shared instance.
     * @return the instance
     */
    public static ServerListenerFactory getInstance()
    {
        if (instance == null)
            instance = new ServerListenerFactory();
        return instance;
    }
    
    /**
     * Registers a listener class for the given message identification string.<br>
     * The listener will only be informed about updates on this message id.<br>
     * There can only be one listener for one event.
     * @param id the purpose to register the listener
     * @param listener the listener to add
     */
    public void registerModelListener(String id, IServerListener listener)
    {
        serverListeners.put(id, listener);
    }
    
    /**
     * Removes a listener form the listener list. <br>
     * The listener wont receive any updates about this message id.
     * @param id the purpose of the register to remove
     */
    public void removeModelListener(String id)
    {
        serverListeners.remove(id);
    }
    
    /**
     * Returns a EventListener for this type of message.<br>
     * When no listener is registered the method 
     * will return null.
     * @param id the message to get the listener
     * @return the listener for this type of message
     */
    public IServerListener getListener(String id)
    {
        return serverListeners.get(id);
    }
    
    /**
     * Convenience method to check if this type of
     * message has a listener or not.
     * @param id the message to check for listeners
     * @return true if a listener was found, otherwise false
     */
    public boolean hasEventListeners(String id)
    {
        if (serverListeners.containsKey(id))
            return true;
        return false;
    }
}
