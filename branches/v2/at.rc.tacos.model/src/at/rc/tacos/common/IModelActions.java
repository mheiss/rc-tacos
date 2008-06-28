package at.rc.tacos.common;

/**
 * Interface definition for the available actions
 * @author Michael
 *
 */
public interface IModelActions
{
    /** Login message */
    public final static String LOGIN = "message.login";
    /** Logout message */
    public final static String LOGOUT = "message.logout";
    /** General system messages */
    public final static String SYSTEM = "message.system";
    
    /** Add message */
    public final static String ADD = "message.add";
    /** Add message */
    public final static String ADD_ALL = "message.addAll";
    
    /** Remove message */
    public final static String REMOVE = "message.remove";
    /** List message */
    public final static String LIST = "message.list";
    /** Update message */
    public final static String UPDATE = "message.update";
}