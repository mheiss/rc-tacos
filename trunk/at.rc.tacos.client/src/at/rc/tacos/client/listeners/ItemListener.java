package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.Activator;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelListener;
import at.rc.tacos.model.Item;

/**
 * This class will notified uppon item changes.
 * @author Michael
 */
public class ItemListener implements IModelListener
{
    @Override
    public void add(AbstractMessage addMessage)
    {
        Activator.getDefault().getItemList().add((Item)addMessage);
    }

    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        // TODO add the listing to the table
        
    }

    @Override
    public void remove(AbstractMessage removeMessage)
    {
        // TODO remove the item from the table
        
    }

    @Override
    public void update(AbstractMessage updateMessage)
    {
        // TODO update the item
        
    }
}
