package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import at.rc.tacos.model.*;

/**
 * All created items.
 * @author Michael
 */
public class ItemManager extends DataManager 
{
    //the item list
    private List<Item> objectList = new ArrayList<Item>();

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
