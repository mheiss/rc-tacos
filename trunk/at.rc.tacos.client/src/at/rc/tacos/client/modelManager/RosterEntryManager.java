package at.rc.tacos.client.modelManager;

//java
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
//rcp
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.swt.widgets.Display;
//model
import at.rc.tacos.model.*;


/**
 * All roster entries
 * @author b.thek
 */
public class RosterEntryManager extends PlatformObject 
{
    //the item list
    private List<RosterEntry> objectList = new ArrayList<RosterEntry>();

    //the listeners to inform about data changes
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * Adds a property-change listener.
     * @param l the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        if (l == null) 
            throw new IllegalArgumentException();
        listeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        listeners.removePropertyChangeListener(l);
    }

    /**
     * Notificates all listeners to a model-change
     * @param prop the property-id
     * @param old the old-value
     * @param newValue the new value
     */
    protected void firePropertyChange(String prop, Object old, Object newValue)
    {
        if (listeners.hasListeners(prop)) 
            listeners.firePropertyChange(prop, old, newValue);
    }

    /**
     * Adds a new roster entry to the list
     * @param roster entry the roster entry to add
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
     * @param roster entry the roster entry to remove
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
     * @param roster entry the roster entry to update
     */
    public void update(final RosterEntry rosterEntry) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
            	//TODO ????????????????????????????????
                objectList.remove(rosterEntry);
                objectList.add(rosterEntry);
                firePropertyChange("ROSTERENTRY_UPDATE", rosterEntry, null); 
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

    public Object[] toArray(String station)
    {
        List<RosterEntry> filteredList = new ArrayList<RosterEntry>();
        for(RosterEntry entry:objectList)
        {
            if (entry.getStation().equalsIgnoreCase(station))
                filteredList.add(entry);
        }
        return filteredList.toArray();
    }
}

