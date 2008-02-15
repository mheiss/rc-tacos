package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Specifies the details of the caller.
 * The caller is most of the time the telephoner, sometimes an employee
 * @author b.thek
 */
public class CallerDetail extends AbstractMessage
{
    //unique identification string
    public static String ID = "callerDetail";

    private int callerId;
    private String callerName;
    private String callerTelephoneNumber;

    /**
     * Default class constructor
     */
    public CallerDetail()
    {
        super(ID);
        //set default values
        callerName = "";
        callerTelephoneNumber = "";
    }

    /**
     * Default class constructor for a complete caller object
     * @param callerName the name of the caller
     * @param callerTelephoneNumber the telephone number
     * @param callerNotes notes taken from the caller
     */
    public CallerDetail(String callerName, String callerTelephoneNumber) 
    {
        super(ID);
        setCallerName(callerName);
        setCallerTelephoneNumber(callerTelephoneNumber);
    }

    //METHODS
    /**
     * Returns a string based description of the object.
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return callerName+","+callerTelephoneNumber;
    }
    
    /**
     * Returns the calculated hash code based on the caller id<br>
     * Two callers have the same hash code if the id is equal.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 + callerId;
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * Two callers are equal if, and only if, the caller id is the same
     * @return true if the callerId is the same, otherwise false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CallerDetail other = (CallerDetail) obj;
        if(callerId != other.getCallerId())
            return false;
        return true;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the id of the caller.<br>
     * The id is a internal value to identify the caller
     * @return id the unique id
     */
    public int getCallerId() 
    {
        return callerId;
    }
    
    /**
     * Returns the name of the caller.
     * @return the callerName
     */
    public String getCallerName() 
    {
        return callerName;
    }
    
    /**
     * Sets the unique id for the caller object.<br>
     * The id is the number of the primary key in the database.
     * @param callerId the id of the caller
     */
    public void setCallerId(int callerId) 
    {
        this.callerId = callerId;
    }

    /**
     * Sets the name of the person who called the emergency.
     * @param callerName the first name and the last name of the person
     * @throws IllegalArgumentException if the callerName is null or empty
     */
    public void setCallerName(String callerName) 
    {
        if(callerName == null)
            throw new IllegalArgumentException("Caller name cannot be null or empty");
        this.callerName = callerName.trim();
    }

    /**
     * Returns the telephone number of the person who called the emergency.
     * @return the callerTelephoneNumber
     */
    public String getCallerTelephoneNumber() 
    {
        return callerTelephoneNumber;
    }

    /**
     * Sets the telephone number of the person who called the emergency
     * @param callerTelephoneNumber the telephone number of the caller
     * @throws IllegalArgumentException if the telephone number is null or empty
     */
    public void setCallerTelephoneNumber(String callerTelephoneNumber) 
    {
        if(callerTelephoneNumber == null)
            throw new IllegalArgumentException("Telephone number cannot be null or emtpy");
        this.callerTelephoneNumber = callerTelephoneNumber.trim();
    }
}
