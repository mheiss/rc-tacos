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
	//item
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
     * @param newItem the updated item
     */
    public void itemUpdated(Item newItem);
    
    /**
     * Notification about a item listing.
     * @param list the list to show
     */
    public void itemListing(ArrayList<Item> list);
    
    
    //roster entry
    /** Notification about a new roster entry 
     * @param newRosterEntry the new roster entry to add
     */
    public void rosterEntryAdded(RosterEntry newRosterEntry);
    
    /** Notification that a roster entry has been removed
     * @param rosterEntry the rosterEntry to remove
     */
    public void rosterEntryRemoved(RosterEntry rosterEntry);
    
    /** Notification that a roster entry should be updated
     * @param newRosterEntry the updated roster entry
     */
    public void rosterEntryUpdated(RosterEntry newRosterEntry);
    
    /** Notification about a roster entry listing
     * @param list the list to show
     */
    public void rosterEntryListing(ArrayList<RosterEntry> list);
    
}
