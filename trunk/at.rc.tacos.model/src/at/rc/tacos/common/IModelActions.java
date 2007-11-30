package at.rc.tacos.common;

/**
 * Interface definition for the available actions
 * @author Michael
 *
 */
public interface IModelActions
{
    /** Login message */
    public final static String LOGIN = "MESSAGE.LOGIN";
    /** Logout message */
    public final static String LOGOUT = "MESSAGE.LOGIN";
    /** General system messages */
    public final static String NOTIFY = "MESSAGE.NOTIFY";
    
    /** Add message */
    public final static String ADD = "MESSAGE.ADD";
    /** Remove message */
    public final static String REMOVE = "MESSAGE.REMOVE";
    /** List message */
    public final static String LIST = "MESSAGE.LIST";
    /** Update message */
    public final static String UPDATE = "MESSAGE.UPDATE";
}
