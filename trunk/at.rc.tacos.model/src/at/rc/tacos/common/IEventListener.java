package at.rc.tacos.common;

/**
 * This interface describes methods that the ui must provide
 * to get status updates from the network.
 * @author Michael
 */
public interface IEventListener
{   
    /**
     * Notification about a login message.<br> 
     */
    public void loginMessage(AbstractMessage message);
    
    /**
     * Notification about a logout message.<br> 
     */
    public void logoutMessage(AbstractMessage message);
    
    /**
     * Notification about a system message<br>
     * Possible messages would be: 
     * <ul>
     * <li>Information about a login of another user</li>
     * </ul>
     */
    public void statusMessage(AbstractMessage message);
}
