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
    
    //Types for the message
    public static final int TYPE_INFO = 0x01;
    public static final int TYPE_ERROR = 0x02;
    
    //properties
    private String message;
    private int type;
    
    /**
     * Default class constructor.
     */
    public SystemMessage()
    {
        super(ID);
    }
    
    /**
     * Default class constructor for a system message specifying the message type
     * @param message the message
     */
    public SystemMessage(String message,int type)
    {
        super(ID);
        setType(type);
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
    
    /**
     * Sets the type of the system message.
     * @param type the type of the message
     */
    public void setType(int type)
    {
    	this.type = type;
    }
    
    /**
     * Returns the type of the system message to categorize them
     * @return the type of the message
     */
    public int getType()
    {
    	return type;
    }
}
