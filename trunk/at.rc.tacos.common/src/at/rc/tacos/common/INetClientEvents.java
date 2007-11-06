package at.rc.tacos.common;

/**
 * Defines methods for the network manager.
 * This methods are defined to notify the listeners to update.
 * @author Michael
 */
public interface INetClientEvents
{
    /** Fired when the item has changed */
    public void fireItemAdded(String item);
}
