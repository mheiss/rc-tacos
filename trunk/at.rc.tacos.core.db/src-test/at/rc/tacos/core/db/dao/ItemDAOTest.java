package at.rc.tacos.core.db.dao;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.core.db.dao.memory.ItemDAOMemory;
import at.rc.tacos.model.Item;

/**
 * Test class fot the item data access object
 * @author Michael
 */
public class ItemDAOTest
{
    private ItemDAO itemDao;
    
    @Before
    public void setUp()
    {
        DaoFactory factory = DaoFactory.TEST;
        itemDao = factory.createItemDAO();
        //add test data
        itemDao.addItem(new Item("test_item1"));
        itemDao.addItem(new Item("test_item2"));
    }
    
    @After
    public void tearDown()
    {
        ItemDAOMemory dao = (ItemDAOMemory)itemDao;
        dao.reset();
    }
    
    @Test
    public void testAddItem()
    {
        int id = itemDao.addItem(new Item("test_item3"));
        Assert.assertEquals(2, id);
    }
    
    @Test 
    public void testFindById()
    {
        Item item = itemDao.getItem(new Item("test_item1"));
        Assert.assertEquals(item.getName(), "test_item1");
    }
    
    @Test
    public void testDeleteItem()
    {
        itemDao.removeItem(new Item("test_item1"));
        Assert.assertEquals(1,itemDao.listItems().size());
    }
    
    @Test
    public void testListItems()
    {
        List<Item> items = itemDao.listItems();
        Assert.assertEquals(2, items.size());
    }
}
