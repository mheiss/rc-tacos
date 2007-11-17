package at.rc.tacos.core.service;

//java
import java.util.ArrayList;
//model
import at.rc.tacos.model.*;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.INetworkListener;
/**
 * Implementation of the service layer.
 * @author Michael
 */
public class ServiceLayerImpl implements IServiceLayer,INetworkListener
{
    private IServiceListener listener;
    
    /**
     * Default class constructor
     */
    public ServiceLayerImpl() { }
    
    //LISTENER SUPPORT
    @Override
    /**
     * Register a service listener to notify
     * @param listener the listener to add
     */
    public void registerServiceListener(IServiceListener listener)
    {       
        this.listener = listener;
    }
    
    @Override
    /**
     * Remove the listener from the listener list
     * @param listener the listener to remove
     */
    public void removeServiceListener(IServiceListener listener)
    {
        this.listener = null;
    }
    
    
    // NETOWRK UPDATES
    @Override
    public void fireNetMessage(String type,String action, ArrayList<AbstractMessage> objects)
    {
        //The type of the content
        if(type.equalsIgnoreCase(Item.ID))
        {    
            //the action to do
            if("add".equalsIgnoreCase(action))
                listener.itemAdded((Item)objects.get(0));
            if("update".equalsIgnoreCase(action))
                listener.itemUpdated((Item)objects.get(0));
            if("remove".equalsIgnoreCase(action))
                listener.itemRemoved((Item)objects.get(0));
            if("list".equalsIgnoreCase(action))
            {
                //create a new item list and add all items
                ArrayList<Item> itemList = new ArrayList<Item>();
                for(Object objectItem:objects)
                    itemList.add((Item)objectItem);
                //notify
                listener.itemListing(itemList);
            }
        }  
        //roster entry
        if(type.equalsIgnoreCase(RosterEntry.ID))
        {
        	RosterEntry rosterEntry = (RosterEntry)objects.get(0);
        	if("add".equalsIgnoreCase(action))
        		listener.rosterEntryAdded(rosterEntry);
        }
    }
    
    // NETWORK METHODS
    @Override
    public void addItem(Item newItem)
    {  
        NetWrapper.getDefault().fireNetworkMessage(Item.ID,"add", newItem);
    }

    @Override
    public void listItems()
    {   
        NetWrapper.getDefault().fireNetworkMessage(Item.ID,"list", new ArrayList<AbstractMessage>());
    }

    @Override
    public void removeItem(Item item)
    {
        NetWrapper.getDefault().fireNetworkMessage(Item.ID,"remove", item);
    }

    @Override
    public void updateItem(Item newItem)
    {
        NetWrapper.getDefault().fireNetworkMessage(Item.ID,"update", newItem);      
    }

	@Override
	public void addRosterEntry(RosterEntry entry) 
	{
		NetWrapper.getDefault().fireNetworkMessage(RosterEntry.ID, "add", entry);
	}
}
