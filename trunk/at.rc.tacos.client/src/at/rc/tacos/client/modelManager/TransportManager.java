package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import at.rc.tacos.model.*;

/**
 * All transports
 * @author b.thek
 */
public class TransportManager extends PropertyManager 
{
    //the item list
    private List<Transport> objectList = new ArrayList<Transport>();

    
    /**
     * Default class constructor
     */
    public TransportManager() { }

    /**
     * Adds a new transport to the list
     * @param rosterEntry the transport to add
     */
    public void add(final Transport transport) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(transport);
                //notify the view
                firePropertyChange("TRANSPORT_ADD", null, transport);
            }
        }); 
    }    

    /**
     * Removes the transport from the list
     * @param rosterEntry the transport to remove
     */
    public void remove(final Transport transport) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(transport);
                firePropertyChange("TRANSPORT_REMOVE", transport, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the transport at the list
     * @param rosterEntry the transport to update
     */
    public void update(final Transport transport) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {  	
            	//get the position of the entry
            	int id = objectList.indexOf(transport);
            	objectList.set(id, transport);
                firePropertyChange("TRANSPORT_UPDATE", null, transport); 
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
                firePropertyChange("TRANSPORT_CLEARED",null,null);
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

}


