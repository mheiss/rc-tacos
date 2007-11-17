package at.rc.tacos.common;

/**
 * This is the basic message type for the protocol.
 * @author Michael
 */
public abstract class AbstractMessage
{
    //the identification
    public static String ID = "message";
    
    /**
     * Default class constructor defining a identification
     * @param id the uniq identification of this object
     */
    public AbstractMessage(String id)
    {
        AbstractMessage.ID = id;
    }
    
    /**
     * Returns the string based description.
     * @param the id of the message
     */
    @Override
    public String toString()
    {
        return ID;
    }
}
