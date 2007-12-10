package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.ItemManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Item;

/**
 * This class will notified uppon item changes.
 * @author Michael
 */
public class ItemListener extends ClientListenerAdapter
{
    @Override
    public void add(AbstractMessage addMessage)
    {
        ModelFactory.getInstance().getItemManager().add((Item)addMessage);
    }

    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        ItemManager manager = ModelFactory.getInstance().getItemManager();
        for(AbstractMessage msg:listMessage)
        {
            Item item = (Item)msg;
            manager.add(item);
        }
    }    
}
