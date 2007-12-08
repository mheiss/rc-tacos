package at.rc.tacos.client.modelManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.core.runtime.PlatformObject;

/**
 * This abstract base class provides all methods for handling
 * property change events.
 * @author Michael
 */
public abstract class DataManager extends PlatformObject
{
    //the listeners to inform about data changes
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    /**
     * Adds a property-change listener.
     * @param listener the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (listener == null) 
            throw new IllegalArgumentException();
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes the property change listener 
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        listeners.removePropertyChangeListener(listener);
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
}
