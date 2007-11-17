package at.rc.tacos.core.service;

import java.util.ArrayList;
import at.rc.tacos.model.*;

/**
 * This listener interface describes the methods that the client must
 * provide to get the network updates.
 * @author Michael
 */
public interface IServiceListener
{
    /**
     * Notification that a item has been added
     * @param newItem the new item to add
     */
    public void itemAdded(Item newItem);
    
    /**
     * Notification that a item has been removed
     * @param item the item to remove
     */
    public void itemRemoved(Item item);
    
    /**
     * Notification that the item should be updated
     * @param oldItem the old item
     * @param newItem the updated item
     */
    public void itemUpdated(Item newItem);
    
    /**
     * Notification about a item listing.
     * @param list the list to show
     */
    public void itemListing(ArrayList<Item> list);
    
    /** Noifictation about a new roster entry */
    public void rosterEntryAdded(RosterEntry entry);
}
