package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Specifies the details of the notifier.
 * The notifier is most of the time the telephoner, sometimes an employee
 * @author b.thek
 */
public class NotifierDetail extends AbstractMessage
{
    //unique identification string
    public final static String ID = "notifierDetail";

    private String notifierName;
    private String notifierTelephoneNumber;
    private String notifierNotes;

    /**
     * Default class constructor
     */
    public NotifierDetail()
    {
        super(ID);
    }

    /**
     * Default class constructor for a complete notifier object
     * @param notifierName the name of the notifier
     * @param notifierTelephoneNumber the telephone number
     * @param notifierNotes notes taken from the caller
     */
    public NotifierDetail(String notifierName, String notifierTelephoneNumber,String notifierNotes) 
    {
        super(ID);
        setNotifierName(notifierName);
        setNotifierTelephoneNumber(notifierTelephoneNumber);
        setNotifierNotes(notifierNotes);
    }

    //METHODS
    /**
     * Returns a string based description of the object.
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return notifierName+","+notifierTelephoneNumber+","+notifierNotes;
    }
    
    /**
     * Returns the calculated hash code based on the notifier name and the telephone number.<br>
     * Two notifiers have the same hash code if the name and 
     * the telephone number is equal
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 + notifierName.hashCode() + notifierTelephoneNumber.hashCode();
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * Two notifiers are equal if, and only if, the notifier name 
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
        final NotifierDetail other = (NotifierDetail) obj;
        if (!notifierName.equals(other.notifierName))
            return false;
        if (!notifierTelephoneNumber.equals(other.notifierTelephoneNumber))
            return false;
        return true;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the name of the notifier.
     * @return the notifierName
     */
    public String getNotifierName() 
    {
        return notifierName;
    }

    /**
     * Sets the name of the person who called the emergency.
     * @param notifierName the first name and the last name of the person
     * @throws IllegalArgumentException if the notifierName is null or empty
     */
    public void setNotifierName(String notifierName) 
    {
        if(notifierName == null || notifierName.trim().isEmpty())
            throw new IllegalArgumentException("Notifier name cannot be null or empty");
        this.notifierName = notifierName.trim();
    }

    /**
     * Returns the telephone number of the person who called the emergency.
     * @return the notifierTelephoneNumber
     */
    public String getNotifierTelephoneNumber() 
    {
        return notifierTelephoneNumber;
    }


    /**
     * Sets the telephone number of the person who called the emergency
     * @param notifierTelephoneNumber the telephone number of the caller
     * @throws IllegalArgumentException if the telephone number is null or empty
     */
    public void setNotifierTelephoneNumber(String notifierTelephoneNumber) 
    {
        if(notifierTelephoneNumber == null || notifierTelephoneNumber.trim().isEmpty())
            throw new IllegalArgumentException("Telephone number cannot be null or emtpy");
        this.notifierTelephoneNumber = notifierTelephoneNumber.trim();
    }


    /**
     * Returns the notes that have been given by the caller.
     * @return the given notes
     */
    public String getNotifierNotes() 
    {
        return notifierNotes;
    }


    /**
     * Sets the notes that have been given by the caller.
     * @param notifierNotes the notes from the caller
     * @throws IllegalArgumentException if the notes are null or empty
     */
    public void setNotifierNotes(String notifierNotes) 
    {
        if(notifierNotes == null || notifierNotes.trim().isEmpty())
            throw new IllegalArgumentException("Notes cannot be null or empty");
        this.notifierNotes = notifierNotes;
    }
}
