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
 * All created items.
 * @author Michael
 */
public class ItemManager extends PlatformObject 
{
    //the item list
    private List<Item> objectList = new ArrayList<Item>();

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
     * Adds a new item to the list
     * @param item the item to add
     */
    public void add(final Item item) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(item);
                //notify the view
                firePropertyChange("ITEM_ADD", null, item);
            }
        }); 
    }    

    /**
     * Removes the item from the list
     * @param item the item to remove
     */
    public void remove(Item item) 
    {
        objectList.remove(item);
        firePropertyChange("ITEM_REMOVE", item, null); 
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
