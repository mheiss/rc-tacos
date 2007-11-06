package at.rc.tacos.common;

/**
 * Defines methods for the network manager.
 * @author Michael
 */
public interface INetEvents
{
    /** Fired when the item has changed */
    public void fireItemChanged(String item);
}
