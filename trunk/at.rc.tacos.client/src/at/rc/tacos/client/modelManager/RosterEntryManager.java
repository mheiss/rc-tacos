package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

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
                objectList.add(rosterEntry);
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
            	//assert we have this roster entry
            	if(!objectList.contains(rosterEntry))
            		return;
            	//get the position of the entry and update it
            	int index = objectList.indexOf(rosterEntry);
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
    		//check the location
    		if(!entry.getStation().equals(location))
    			continue;
    		//check if the staff has signed in
    		if(entry.getRealStartOfWork() == 0 || entry.getRealEndOfWork() != 0)
    			continue;
    		
    		//check if the entry is for today
    		if(MyUtils.isEqualDate(Calendar.getInstance().getTimeInMillis(), entry.getRealStartOfWork()))
    				filteredList.add(entry);
    		
    		Calendar bevor = Calendar.getInstance();
    		bevor.add(Calendar.DAY_OF_MONTH, -1);		
    		//if the entry is split up we must look at the day bevor also
    		if(entry.isSplitEntry() && MyUtils.isEqualDate(bevor.getTimeInMillis(), entry.getRealStartOfWork()))
    			filteredList.add(entry);
    	}
    	return filteredList;
    }
}