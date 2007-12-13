package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.ItemDAO;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will notified uppon item changes.
 * @author Michael
 */
public class ItemListener extends ServerListenerAdapter
{
    //the data source
    private ItemDAO itemDao = DaoService.getInstance().getFactory().createItemDAO();
    
    /**
     * Add a item to the list
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Item item = (Item)addObject;
        itemDao.addItem(item);
        return item;
    }

    /**
     * Get a listing of items
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(itemDao.listItems());
        return list;
    }

    /**
     * Remove the item from the list
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        Item item = (Item)removeObject;
        itemDao.removeItem(item);
        return removeObject;
    }

    /**
     * Updates the item in the list
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        Item item = (Item)updateObject;
        itemDao.updateItem(item);
        return item;
    }    
}
