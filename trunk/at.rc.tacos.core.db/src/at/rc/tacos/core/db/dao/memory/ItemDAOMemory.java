package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.ItemDAO;
import at.rc.tacos.model.Item;

public class ItemDAOMemory implements ItemDAO
{
    //the shared instance
    private static ItemDAOMemory instance;
    
    //the data list
    private ArrayList<Item> itemList;
    
    /**
     * Default class constructor
     */
    private ItemDAOMemory()
    {
        itemList = new ArrayList<Item>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ItemDAOMemory getInstance()
    {
        if (instance == null)
            instance = new ItemDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        itemList = new ArrayList<Item>();
    }

    @Override
    public int addItem(Item item)
    {
        itemList.add(item);
        return itemList.size();
    }
    
    @Override
    public void updateItem(Item item)
    {
        int index = itemList.indexOf(item);
        itemList.remove(index);
        itemList.add(index,item);
    }
    
    @Override
    public void removeItem(Item item)
    {
        itemList.remove(item);
    }
    
    @Override
    public Item getItem(Item item)
    {
        int index = itemList.indexOf(item);
        return itemList.get(index); 
    }

    @Override
    public List<Item> listItems()
    {
        return itemList;
    }
}
