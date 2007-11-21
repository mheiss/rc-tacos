package at.rc.tacos.factory;

import java.util.HashMap;
import at.rc.tacos.common.IModelListener;

/**
 * This class handles the listener classes for the gui updates.
 * @author Michael
 */
public class ListenerFactory
{
    private static ListenerFactory instance;
    private HashMap<String, IModelListener> modelListener;
    
    /**
     * Default class constuructor
     */
    private ListenerFactory()
    {
        modelListener = new HashMap<String, IModelListener>();
    }
    
    /**
     * Returns the shared instance.
     * @return the shared instance
     */
    public static ListenerFactory getDefault()
    {
        if(instance == null)
            instance = new ListenerFactory();
        return instance;
    }
    
    /**
     * Registers a listener class for the given message identification string.<br>
     * The listener will only be informed about updates on this message id.<br>
     * There can only be one listener for one event.
     * @param id the purpose to register the listener
     * @param listener the listener to add
     */
    public void registerModelListener(String id, IModelListener listener)
    {
        modelListener.put(id, listener);
    }
    
    /**
     * Removes a listener form the listener list. <br>
     * The listener wont receive any updates about this message id.
     * @param id the purpose of the register to remove
     */
    public void removeModelListener(String id)
    {
        modelListener.remove(id);
    }
    
    /**
     * Returns a ModelListener for this type of message.<br>
     * When no listener is registered the method will return 
     * null.
     * @param id the message to get the listener
     * @return the listener for this type of message
     */
    public IModelListener getListener(String id)
    {
        return modelListener.get(id);
    }
    
    /**
     * Convenience method to check if this type of
     * message has a listener or not.
     * @param id the message to check for listeners
     * @return true if a listener was found, otherwise false
     */
    public boolean hasListeners(String id)
    {
        if (modelListener.get(id) != null)
            return true;
        return false;
    }
}
