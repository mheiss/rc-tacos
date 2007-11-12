package at.rc.tacos.core.service;

import at.rc.tacos.model.*;

/**
 * This interface is designed to offer the clients the 
 * chance to access the network layer throught the service layer.
 * @author Michael
 */
public interface IServiceLayer
{
    // Listener to inform 
    /**
     * Register a listener to notify about changes.<br>
     * Note that only one listener is supported
     * @param listener the listener to add
     */
    public void registerServiceListener(IServiceListener listener);
    
    /**
     * Removes the listener from the listener list.
     * @param listener the listener to remove
     */
    public void removeServiceListener(IServiceListener listener);
    
    // SERVICE METHODS
    /** Add a item to the itemTable **/
    public void addItem(Item newItem);
    /** Update an existing item */
    public void updateItem(Item newItem);
    /** Remove the item */
    public void removeItem(Item item);
    /** List the available items */
    public void listItems();
}
