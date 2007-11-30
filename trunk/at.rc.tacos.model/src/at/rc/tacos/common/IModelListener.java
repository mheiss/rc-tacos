package at.rc.tacos.common;

import java.util.ArrayList;

/**
 * This interface describes methods that the UI listeners must provide to
 * get model updates from the network.
 * @author Michael
 */
public interface IModelListener
{
    /**
     *  Notification about a new message to add.
     *  @param addMessage the object to add
     */
    public void add(AbstractMessage addMessage);
    
    /**
     * Notification about a message to delete.
     * @param removeMessage the object to remove
     */
    public void remove(AbstractMessage removeMessage);
    
    /**
     * Notification about a list of messages to display.
     * @param listMessage the objects to list
     */
    public void list(ArrayList<AbstractMessage> listMessage);
    
    /**
     * Notification about a item to update.
     * @param updateMessage the object to update
     */
    public void update(AbstractMessage updateMessage);
}
