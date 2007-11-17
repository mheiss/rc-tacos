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
     * Default class construtor
     */
    public NotifierDetail()
    {
        super(ID);
    }

    /**
     * Default class construcotor for a complete notifier object
     * @param notifierName the name of the notifier
     * @param notifierTelephoneNumber the telephone number
     * @param notifierNotes notes taken from the caller
     */
    public NotifierDetail(String notifierName, String notifierTelephoneNumber,
            String notifierNotes) 
    {
        this();
        this.notifierName = notifierName;
        this.notifierTelephoneNumber = notifierTelephoneNumber;
        this.notifierNotes = notifierNotes;
    }

    //METHODS
    /**
     * Returns a string based description of the object
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return ID;
    }

    //GETTERS AND SETTERS
    /**
     * @return the notifierName
     */
    public String getNotifierName() 
    {
        return notifierName;
    }


    /**
     * @param notifierName the notifierName to set
     */
    public void setNotifierName(String notifierName) 
    {
        this.notifierName = notifierName;
    }

    /**
     * @return the notifierTelephoneNumber
     */
    public String getNotifierTelephoneNumber() 
    {
        return notifierTelephoneNumber;
    }


    /**
     * @param notifierTelephoneNumber the notifierTelephoneNumber to set
     */
    public void setNotifierTelephoneNumber(String notifierTelephoneNumber) 
    {
        this.notifierTelephoneNumber = notifierTelephoneNumber;
    }


    /**
     * @return the notifierNotes
     */
    public String getNotifierNotes() 
    {
        return notifierNotes;
    }


    /**
     * @param notifierNotes the notifierNotes to set
     */
    public void setNotifierNotes(String notifierNotes) 
    {
        this.notifierNotes = notifierNotes;
    }
}
