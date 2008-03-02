package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.*;

/**
 * All roster entries
 * @author b.thek
 */
public class RosterEntryManager extends PropertyManager 
{
    //the item list
    private List<RosterEntry> objectList = new ArrayList<RosterEntry>();
    
    /**
     * Default class constructor
     */
    public RosterEntryManager() { }

    /**
     * Adds a new roster entry to the list
     * @param rosterEntry the roster entry to add
     */
    public void add(final RosterEntry rosterEntry) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(rosterEntry);
                //notify the view
                firePropertyChange("ROSTERENTRY_ADD", null, rosterEntry);
            }
        }); 
    }    

    /**
     * Removes the roster entry from the list
     * @param rosterEntry the roster entry to remove
     */
    public void remove(final RosterEntry rosterEntry) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(rosterEntry);
                firePropertyChange("ROSTERENTRY_REMOVE", rosterEntry, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the roster entry at the list
     * @param rosterEntry the roster entry to update
     */
    public void update(final RosterEntry rosterEntry) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {  	
            	//get the position of the entry
            	int index = objectList.indexOf(rosterEntry);
            	//replace by the new
            	objectList.set(index, rosterEntry);
                firePropertyChange("ROSTERENTRY_UPDATE", null, rosterEntry); 
            }
        }); 
    }
    
    /**
     * Removes all elements form the list
     */
    public void removeAllEntries()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("ROSTERENTRY_CLEARED",null,null);
            }
        }); 
        
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }
    
    /**
     * Returns all roster entries.
     * @return all entries
     */
    public List<RosterEntry> getRosterList()
    {
    	return objectList;
    }
    
    /** Returns a list of all checked in roster entries by location
     * @param location the location to filter
     */
    public List<RosterEntry> getCheckedInRosterEntriesByLocation(Location location)
    {   	
    	List<RosterEntry> filteredList = new ArrayList<RosterEntry>();
    	for(RosterEntry entry : objectList)
    	{
    		if(entry.getRealStartOfWork()!=0 && entry.getRealEndOfWork() == 0 && entry.getStation().getId() == location.getId())
    		{
    			filteredList.add(entry);
    		}
    	}
    	return filteredList;
    }
}


