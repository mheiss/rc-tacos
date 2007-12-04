package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.ItemDAO;
import at.rc.tacos.model.Item;

public class ItemDAOTest implements ItemDAO
{
    //the shared instance
    private static ItemDAOTest instance;
    
    //the data list
    private ArrayList<Item> itemList;
    
    /**
     * Default class constructor
     */
    private ItemDAOTest()
    {
        itemList = new TestDataSource().itemList;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ItemDAOTest getInstance()
    {
        if (instance == null)
            instance = new ItemDAOTest();
        return instance;
    }

    @Override
    public int addItem(Item item)
    {
        itemList.add(item);
        return itemList.indexOf(item);
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
    public List<Item> listItems()
    {
        return itemList;
    }
}
