package at.rc.tacos.core.net.event;

/**
 * The listener interface that all client ui must implement
 * to update the ui when new data is recevied.
 * @author Michael
 */
public interface IClientNetEventListener
{
    // connection changes
    /** Connection to the primary server changed */
    public void updatePrimaryServerConnection(int newStatus);
    /** Connection to the failback server changed */
    public void updateFailbackServerConnection(int newStatus);
    
    // data updates
    public void newItem();
    public void newPerson();
}
