package at.rc.tacos.core.net.event;

import at.rc.tacos.core.net.internal.MySocket;

/**
 * Simple net event to get a socket and the received message.
 * @author Michael
 */
public class NetEvent 
{    
    //properties
    private String message;   
    private MySocket socket;

    /**
     * default Constructor for an empty NetEvent
     */
    public NetEvent() { }
    
    /**
     * Create a NetEvent
     * @param socket the socket that is the source for the event
     * @param message the data that is reveived
     */
    public NetEvent(MySocket socket,String message)
    {
        this.socket = socket;
        this.message = message;
    }
    /**
     * This method returns the message that was reveived with the socket
     * @return the message itself
     */
    public String getMessage()
    {
        return message;
    }
    
    /**
     * Method to set the message for this netEvent
     * @param message the message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * Method to get the source (= socket) that is responsible for this netEvent
     * @return the socket that has reveived the Message
     */
    public MySocket getSocket()
    {
        return socket;
    }
    
    /**
     * Method to set the socket that received the message
     * @param socket the socket 
     */
    public void setSocket(MySocket socket)
    {
        this.socket = socket;
    }
}
