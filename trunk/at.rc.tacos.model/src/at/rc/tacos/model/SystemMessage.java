package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * This class represents a simple system message that
 * can be used for variouse things.
 * @author Michael
 */
public class SystemMessage extends AbstractMessage
{
    //Identification of a system message
    public final static String ID = "system";
    
    //properties
    private String message;
    
    /**
     * Default class constructor.
     */
    public SystemMessage()
    {
        super(ID);
    }
    
    /**
     * Default class constructor for a system message.
     * @param message the message
     */
    public SystemMessage(String message)
    {
        super(ID);
        setMessage(message);
    }
    
    /**
     * Returns the given system message.
     * @return the message
     */
    @Override
    public String toString()
    {
        return message;
    }
    
    /**
     * Sets the system message.
     * @param message the message to set.
     * @throws IllegalArgumentException if the message is null
     */
    public void setMessage(String message)
    {
        if(message == null || message.trim().isEmpty())
            throw new IllegalArgumentException("The message cannot be null or empty");
        this.message = message;
    }
    
    /**
     * Returns the system message.
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }
}
