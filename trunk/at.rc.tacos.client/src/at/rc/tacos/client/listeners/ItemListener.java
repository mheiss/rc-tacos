package at.rc.tacos.client.listeners;

import at.rc.tacos.client.Activator;
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
        Activator.getDefault().getItemList().add((Item)addMessage);
    }
}
