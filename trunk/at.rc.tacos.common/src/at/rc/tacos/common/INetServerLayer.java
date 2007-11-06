package at.rc.tacos.common;

/**
 * Defines methods for the implementation of the network protocol.<br>
 * This methods are designed to send messages to the server.
 * @author Michael
 */
public interface INetServerLayer
{
    /**
     * This method is called to send a item to the server
     */
    public void requestAddItem(String item);
}
