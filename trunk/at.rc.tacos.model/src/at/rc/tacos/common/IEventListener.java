package at.rc.tacos.common;

/**
 * This interface describes methods that the ui must provide
 * to get status updates from the network.
 * @author Michael
 */
public interface IEventListener
{
    //IDENTIFICATION STRINGS FOR THE ACTIONS
    /** Login message */
    public final static String LOGIN = "MESSAGE.LOGIN";
    
    /** Logout message */
    public final static String LOGOUT = "MESSAGE.LOGIN";
    
    /** General system messages */
    public final static String NOTIFY = "MESSAGE.NOTIFY";
    
    // METHODS
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
