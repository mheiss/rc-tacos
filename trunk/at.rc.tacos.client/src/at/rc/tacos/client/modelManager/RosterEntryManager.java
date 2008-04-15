package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Calendar displayedDate;
    
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
	 * Informs the views that the selected date in the roster view filter has changed.
	 * @param newDate the newDate to display
	 */
	public void fireRosterViewFilterChanged(final Calendar newDate)
	{
		this.displayedDate = newDate;
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				//fire a property change event to notify the viewers that the date changed
				firePropertyChange("ROSTER_DATE_CHANGED",null,newDate);
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
    
    /** Returns a list of all checked in roster entries by location within two days
     * so  two days after the planned end of the entry the member can't longer be assigned to a vehicle
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
    		
    		//do not add entries if they are older than 2 days even if the member is signed in		
    		Calendar cal = Calendar.getInstance();
    		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) -2);
    		long before4Days = cal.getTimeInMillis();
    		if(entry.getPlannedEndOfWork()>before4Days)
    			filteredList.add(entry);
    	}
    	return filteredList;
    }
    
    /**
     * Returns the roster entry for the given (checked in) StaffMember or null if there is no entry for the staffMemberId
     * @param staffMember the staffMember to filter
     */
    public RosterEntry getCheckedInRosterEntryByStaffId(int staffId)
    {
    	for(RosterEntry entry : objectList)
    	{
    		if(entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0)
    			if(entry.getStaffMember().getStaffMemberId() == staffId)
    			return entry;
    	}
    	return null;
    }
    
    /**
	 * Returns the date of the currently displayed entry choosen by the filter view.
	 * @return displayDate the displayed date
	 */
	public Calendar getDisplayedDate()
	{
		return displayedDate;
	}
}