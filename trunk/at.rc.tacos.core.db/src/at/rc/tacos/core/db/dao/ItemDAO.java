package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Item;

public interface ItemDAO
{
    public int addItem(Item item);
    public void updateItem(Item item);
    public void removeItem(Item item);
    
    public Item getItem(Item item);
    public List<Item> listItems();
}
