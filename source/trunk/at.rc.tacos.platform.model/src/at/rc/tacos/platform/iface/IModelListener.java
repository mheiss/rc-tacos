package at.rc.tacos.platform.iface;

import java.util.List;

import at.rc.tacos.platform.model.AbstractMessage;

/**
 * This interface describes methods that the UI listeners must provide to get model updates from the network.
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
     *  Notification about a new message list to add.
     *  @param addMessage the object to add
     */
    public void addAll(List<AbstractMessage> addMessage);
    
    /**
     * Notification about a message to delete.
     * @param removeMessage the object to remove
     */
    public void remove(AbstractMessage removeMessage);
    
    /**
     * Notification about a list of messages to display.
     * @param listMessage the objects to list
     */
    public void list(List<AbstractMessage> listMessage);
    
    /**
     * Notification about a item to update.
     * @param updateMessage the object to update
     */
    public void update(AbstractMessage updateMessage);
    
    /**
     * Notification about a login message.
     * @param message the login message
     */
    public void loginMessage(AbstractMessage message);
    
    /**
     * Notification about a logout message.
     * @param message the logout message
     */
    public void logoutMessage(AbstractMessage message);
    
    /**
     * Notification about a system message.
     * @param message the system message
     */
    public void systemMessage(AbstractMessage message);
}
