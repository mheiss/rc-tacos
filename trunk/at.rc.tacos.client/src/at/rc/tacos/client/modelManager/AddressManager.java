package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import at.rc.tacos.model.Address;

/**
 * Manages the addresses of the client programm and stores them in a files.
 * Updates are synchronized between the clients
 * @author Michael
 */
public class AddressManager extends PropertyManager 
{
    //the list
    private List<Address> objectList = new ArrayList<Address>();
    
    /**
     * Default class constructor
     */
    public AddressManager()
    {
    	doLoad();
    }
    
    /**
     * Returns the list the managed addresses.
     * @return the managed addresses
     */
    public List<Address> getAddressList()
    {
    	return objectList;
    }
    
    /**
     * Saves the current list of addresses in the file
     */
    public void doSave()
    {
    	//TODO: save the list
    }
    
    /**
     * Loads the current list of all addresses from the file
     */
    private void doLoad()
    {	
    	//cleare the list
    	removeAllElements();
    	//load the file
    }
    
    /**
     * Adds a new address to the list
     * @param address the address to add
     */
    public void add(final Address address) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(address);
                //notify the view
                firePropertyChange("ADDRESS_ADD", null, address);
            }
        }); 
    }   
    
    /**
     * Adds all the messages to the list
     * @param addressList the addresses to add
     */
    public void addAll(final List<Address> addressList)
    {
    	Display.getDefault().asyncExec(new Runnable()
        {
            public void run ()       
            {
                //add the item
                objectList.addAll(addressList);
                //notify the view
                firePropertyChange("ADDRESS_ADD_ALL", null, addressList);
            }
        }); 
    }

    /**
     * Removes the address from the list
     * @param address the address to remove
     */
    public void remove(final Address address) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(address);
                firePropertyChange("ADDRESS_REMOVE", address, null); 
            }
        }); 
    }
    
    /**
     * Updates the address in the list
     * @param address the address to update
     */
    public void update(final Address address) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                //get the position of the entry
                int id = objectList.indexOf(address);
                objectList.set(id, address);
                firePropertyChange("ADDRESS_UPDATE", null, address); 
            }
        }); 
    }
    
    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Address[] toArray()
    {
    	Address[] list = new Address[objectList.size()];
    	for(int i = 0;i < objectList.size();i++)
    		list[i] = objectList.get(i);
        return list;
    }
    
    /**
     * Removes all elements and clears the list
     */
    public void removeAllElements()
    {
    	Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("ADDRESS_CLEARED",null,null);
            }
        }); 
    }
}
