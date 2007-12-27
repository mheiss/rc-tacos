package at.rc.tacos.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This is the basic message type for the protocol.
 * @author Michael
 */
public abstract class AbstractMessage
{
    //the identification
    public static String ID = "message";
    
    /**
     * Default class constructor defining a identification
     * @param id the uniq identification of this object
     */
    public AbstractMessage(String id)
    {
        AbstractMessage.ID = id;
    }
    
    /**
     * Returns the string based description.
     * @param the id of the message
     */
    @Override
    public String toString()
    {
        return ID;
    }
    
    //implement property change support for all models
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Adds a property change listener to this model object.
     * @param listener the listener to attach
     */
	public void addPropertyChangeListener(PropertyChangeListener listener) 
	{
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Adds a specifiy property change listener to this model object.<br>
	 * The listener will only be informed about the given property events.
	 * @param propertyName the name of the interested property
	 * @param listener the listener to attach
	 */
	public void addPropertyChangeListener(String propertyName,PropertyChangeListener listener) 
	{
		propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
	}

	/**
	 * Removes a property change listener from the listener list
	 * @param listener the listener to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Removes a specific property change listener from the list<br>
	 * The listener wont't receive change events for the given property.
	 * @param propertyName the property to remove from the listener list
	 * @param listener the listener to remove
	 */
	public void removePropertyChangeListener(String propertyName,PropertyChangeListener listener) 
	{
		propertyChangeSupport.removePropertyChangeListener(propertyName,listener);
	}

	/**
	 * Fired when a property has changed the value and so the ui must be updated to 
	 * reflect the actual model.
	 * @param propertyName the property that has changed
	 * @param oldValue the old value of the property
	 * @param newValue the new value of the property
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,Object newValue) 
	{
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,newValue);
	}
}
