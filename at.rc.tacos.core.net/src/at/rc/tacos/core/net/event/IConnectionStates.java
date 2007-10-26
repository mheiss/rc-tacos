package at.rc.tacos.core.net.event;

/**
 * Public interface for network connection states.
 * @author Michael
 */
public interface IConnectionStates
{
    /**  connected to the server */
    public static final int STATE_CONNECTED = 1;
    
    /** disconnected from the netork */
    public static final int STATE_DISCONNECTED_NETWORK = 2;
    
    /** disconnected from the primary server  */
    public static final int STATE_DISCONNECTED_PRIMARY_SERVER = 3;
}
