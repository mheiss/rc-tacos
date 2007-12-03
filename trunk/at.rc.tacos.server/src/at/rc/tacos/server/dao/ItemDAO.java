package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.Item;

/**
 * Data source for items
 * @author Michael
 */
public class ItemDAO
{
    //the shared instance
    private static ItemDAO instance;
    //the data list
    private ArrayList<Item> itemList; 
    
    /**
     * Default private class constructor
     */
    private ItemDAO()
    {
        //create the list
        itemList = new ArrayList<Item>();
        //load dummy data
        Item i1 = new Item("item1");
        Item i2 = new Item("item2");
        Item i3 = new Item("item3");
        itemList.add(i1);
        itemList.add(i2);
        itemList.add(i3);
    }
    
    /**
     * Creates and returns the shared instance
     */
    public static ItemDAO getInstance()
    {
        if( instance == null)
            instance = new ItemDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<Item> getList()
    {
        return itemList;
    }
}
