package at.rc.tacos.core.service;

//java
import java.util.ArrayList;
//model
import at.rc.tacos.model.*;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.common.INetworkListener;
import at.rc.tacos.common.IXMLObject;

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
    public void fireNetMessage(String type,String action, ArrayList<IXMLObject> objects)
    {
        //The type of the content
        if(type.equalsIgnoreCase(Item.ITEM_ID))
        {    
            //the action to do
            if(action.equalsIgnoreCase("add"))
                listener.itemAdded((Item)objects.get(0));
            if(action.equalsIgnoreCase("update"))
                listener.itemUpdated((Item)objects.get(0));
            if(action.equalsIgnoreCase("remove"))
                listener.itemRemoved((Item)objects.get(0));
            if(action.equalsIgnoreCase("list"))
            {
                //create a new item list and add all items
                ArrayList<Item> itemList = new ArrayList<Item>();
                for(IXMLObject objectItem:objects)
                    itemList.add((Item)objectItem);
                //notify
                listener.itemListing(itemList);
            }
        }    
    }
    
    // NETWORK METHODS
    @Override
    public void addItem(Item newItem)
    {  
        NetWrapper.getDefault().fireNetworkMessage(Item.ITEM_ID,"add", newItem);
    }

    @Override
    public void listItems()
    {   
        NetWrapper.getDefault().fireNetworkMessage(Item.ITEM_ID,"list", new ArrayList<IXMLObject>());
    }

    @Override
    public void removeItem(Item item)
    {
        NetWrapper.getDefault().fireNetworkMessage(Item.ITEM_ID,"remove", item);
        
    }

    @Override
    public void updateItem(Item newItem)
    {
        NetWrapper.getDefault().fireNetworkMessage(Item.ITEM_ID,"update", newItem);      
    }
}
