package at.rc.tacos.common;

import java.util.Vector;


/**
 * This is a util class that informs the listeners about changes.
 * @author Michael
 */
public class NetManager implements INetEvents
{
    //the listeners
    Vector<INetLayer> listeners;
    
    /**
     * Default class constructor
     */
    public NetManager()
    {
        listeners = new Vector<INetLayer>();
    }
    
    //METHODS
    /**
     * Add a NetChangeListener to the listener list. 
     * The listener is registered for all properties.
     * @param listener The NetChangeListener to be added
     */
    public void addNetChangeListener(INetLayer listener)
    {
        listeners.addElement(listener);
    }
    
    /**
     * Remove a NetChangeListener from the listener list. 
     * This removes a listener that was registered for all properties.
     * @param listener The NetChangeListener to be removed
     */
    public void removeNetChangeListener(INetLayer listener)
    {
        listeners.removeElement(listener);
    }
    
    //EVENTS
    /**
     * Informs the listeners that the item has changed
     * @param newItem the new item
     */
    @Override
    public void fireItemChanged(String newItem)
    {
        //loop and iform the listeners
        for(INetLayer listener:listeners)
            listener.itemChanged(newItem);
    }
}
