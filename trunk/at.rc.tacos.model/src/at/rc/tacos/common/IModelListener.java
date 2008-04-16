package at.rc.tacos.common;

import java.util.ArrayList;
import java.util.List;

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
    public void list(ArrayList<AbstractMessage> listMessage);
    
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
    
    /**
     * Nofification about a network status change
     * @param status the new status
     */
    public void connectionChange(int status);
    
    /**
     * Nofification that the message could not be transmitted
 	 * @param info the message that was not transmitted
     */
    public void transferFailed(AbstractMessageInfo info);
    
    /**
     * Logs the message on the client
     */
    public void log(String message,int status);
}
