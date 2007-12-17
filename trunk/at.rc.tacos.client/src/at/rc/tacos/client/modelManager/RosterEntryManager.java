package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.common.Constants;
import at.rc.tacos.model.*;

/**
 * All roster entries
 * @author b.thek
 */
public class RosterEntryManager extends DataManager 
{
    //the item list
    private List<RosterEntry> objectList = new ArrayList<RosterEntry>();
    
    //the active day that is showed
    private String station;
    
    /**
     * Default class constructor
     */
    public RosterEntryManager()
    {
        station = Constants.STATION_BEZIRK;
    }

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
            	int id = objectList.indexOf(rosterEntry);
            	objectList.set(id, rosterEntry);
                firePropertyChange("ROSTERENTRY_UPDATE", rosterEntry, null); 
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
     * Sets the currently active station to this value
     * @param activeStation the station to filter
     */
    public void setActiveStation(String activeStation)
    {
        this.station = activeStation;
        //force redraw
        firePropertyChange("ROSTERENTRY_ADD", null, null);
    }
    
    /**
     * Returns the currently active station
     * @return the currently active station
     */
    public String getActiveStation()
    {
        return station;
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }

    public Object[] toFilteredArray()
    {
        List<RosterEntry> filteredList = new ArrayList<RosterEntry>();
        for(RosterEntry entry:objectList)
        {
            if(Constants.STATION_BEZIRK.equalsIgnoreCase(station))//TODO
                filteredList.add(entry);
            else if (entry.getStation().equalsIgnoreCase(station))
                filteredList.add(entry);
        }
        return filteredList.toArray();
    }
}

