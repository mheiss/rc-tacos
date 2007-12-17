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
    public final static String ID = "callerDetail";

    private String callerName;
    private String callerTelephoneNumber;
    private String callerNotes;

    /**
     * Default class constructor
     */
    public CallerDetail()
    {
        super(ID);
    }

    /**
     * Default class constructor for a complete caller object
     * @param callerName the name of the caller
     * @param callerTelephoneNumber the telephone number
     * @param callerNotes notes taken from the caller
     */
    public CallerDetail(String callerName, String callerTelephoneNumber,String callerNotes) 
    {
        super(ID);
        setCallerName(callerName);
        setCallerTelephoneNumber(callerTelephoneNumber);
        setCallerNotes(callerNotes);
    }

    //METHODS
    /**
     * Returns a string based description of the object.
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return callerName+","+callerTelephoneNumber+","+callerNotes;
    }
    
    /**
     * Returns the calculated hash code based on the caller name and the telephone number.<br>
     * Two callers have the same hash code if the name and 
     * the telephone number is equal
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 + callerName.hashCode() + callerTelephoneNumber.hashCode();
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * Two callers are equal if, and only if, the caller name 
     * and the telephone number is the same.
     * @return true if the name and the number is the same, otherwise false
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
        if (!callerName.equals(other.callerName))
            return false;
        if (!callerTelephoneNumber.equals(other.callerTelephoneNumber))
            return false;
        return true;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the name of the caller.
     * @return the callerName
     */
    public String getCallerName() 
    {
        return callerName;
    }

    /**
     * Sets the name of the person who called the emergency.
     * @param callerName the first name and the last name of the person
     * @throws IllegalArgumentException if the callerName is null or empty
     */
    public void setCallerName(String callerName) 
    {
        if(callerName == null || callerName.trim().isEmpty())
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
        if(callerTelephoneNumber == null || callerTelephoneNumber.trim().isEmpty())
            throw new IllegalArgumentException("Telephone number cannot be null or emtpy");
        this.callerTelephoneNumber = callerTelephoneNumber.trim();
    }


    /**
     * Returns the notes that have been given by the caller.
     * @return the given notes
     */
    public String getCallerNotes() 
    {
        return callerNotes;
    }


    /**
     * Sets the notes that have been given by the caller.
     * @param callerNotes the notes from the caller
     * @throws IllegalArgumentException if the notes are null
     */
    public void setCallerNotes(String callerNotes) 
    {
        if(callerNotes == null)
            throw new IllegalArgumentException("Notes cannot be null");
        this.callerNotes = callerNotes;
    }
}
