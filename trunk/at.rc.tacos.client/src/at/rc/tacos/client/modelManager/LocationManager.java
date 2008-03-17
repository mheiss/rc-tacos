package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Location;

public class LocationManager extends PropertyManager 
{
    //the list
    private List<Location> objectList = new ArrayList<Location>();

    /**
     * Default class constructor
     */
    public LocationManager() { }

    /**
     * Adds a new location to the list
     * @param location the location to add
     */
    public void add(final Location location) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(location);
                //notify the view
                firePropertyChange("LOCATION_ADD", null, location);
            }
        }); 
    }    

    /**
     * Removes the location from the list
     * @param location the location to remove
     */
    public void remove(final Location location) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(location);
                firePropertyChange("LOCATION_REMOVE", location, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the location in the list
     * @param location the location to update
     */
    public void update(final Location location) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
            	//assert we have this location
            	if(!objectList.contains(location))
            		return;
                //get the position of the entry
                int id = objectList.indexOf(location);
                objectList.set(id, location);
                firePropertyChange("LOCATION_UPDATE", null, location); 
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
                firePropertyChange("LOCATION_CLEARED",null,null);
            }
        }); 
    }
    
    /**
     * Returns a given location by the name of the station
     * @param station name the name of the station to get the location
     */
    public Location getLocationByName(String station)
    {
        //loop and search
        for(Location loc :objectList)
        {
            if(loc.getLocationName().equalsIgnoreCase(station))
                return loc;
        }
        //nothing found
        return null;
    }
    
    /**
     * Returns all locations in the list
     * @return the location list
     */
    public List<Location> getLocations()
    {
        return objectList;
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }
}