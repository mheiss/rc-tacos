package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Specifies the details of the mobile phone
 * @author b.thek
 */
public class MobilePhoneDetail extends AbstractMessage
{
    //unique identification string
    public final static String ID = "mobilePhoneDetail";

    //properties
    private int id;
    private String mobilePhoneName;
    private String mobilePhoneNumber;

    /**
     * Default class constructors
     */
    public MobilePhoneDetail() 
    { 
        super(ID);
    }
    
    /**
     * Constructor for a complete mobile phone object.
     * @param mobilePhoneName the name of the mobile phone.
     * @param mobilePhoneNumber the number of the phone
     */
    public MobilePhoneDetail(String mobilePhoneName,String mobilePhoneNumber) 
    {
        super(ID);
        setMobilePhoneName(mobilePhoneName);
        setMobilePhoneNumber(mobilePhoneNumber);
    }

    //METHODS
    /**
     * Returns a string based description of the object
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return id+": "+mobilePhoneName+" - "+mobilePhoneNumber;
    }

    /**
     * Returns the calculated hash code based on the mobile phone id.<br>
     * Two mobile phone entries have the same hash code if the id is equal.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 + id;
    }

    /**
     * Returns whether the objects are equal or not.<br>
     * The mobile phone details are equal if, and only if, the id is the same.
     * @return true if the id is the same, otherwise false
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
        final MobilePhoneDetail other = (MobilePhoneDetail) obj;
        if (id != other.id)
            return false;
        return true;
    }

    //SETTERS AND GETTERS
    /**
     * Returns the internal identification number of the mobile phone.
     * The id is a internal value in the database to identify the phone.
     * @param id the uniqe id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Returns the name of the mobile phone.<br>
     * When the mobile phone is accociated to a vehicle the name of the phone
     * is equal to the name of the vehicle.
     * @return the name of the phone
     */
    public String getMobilePhoneName()
    {
        return mobilePhoneName;
    }
    
    /**
     * Returns the mobile phone number
     * @return the mobilePhoneNumber
     */
    public String getMobilePhoneNumber() 
    {
        return mobilePhoneNumber;
    }
    
    /**
     * Sets the id of this mobile phone.
     * @param id the unique id out of the database
     */
    public void setId(int id)
    {
        this.id = id;
    }
        
    /**
     * Sets the identification of the mobile phone.<br>
     * The string is constructed using two chars to specify the station 
     * plus a double digit distinct number.<br>
     * Example: BM01
     * @param mobilePhoneId the mobilePhoneId to set
     * @throws IllegalArgumentException if the mobilePhoneId is null or empty
     */
    public void setMobilePhoneName(String mobilePhoneName)
    {
        if(mobilePhoneName == null || mobilePhoneName.trim().isEmpty())
            throw new IllegalArgumentException("The name cannot be null or empty");
        this.mobilePhoneName = mobilePhoneName;
    }

    /**
     * Sets the mobile phone number.
     * @param mobilePhoneNumber the mobilePhoneNumber to set
     * @throws IllegalArgumentException if the mobilePhoneId is null or empty
     */
    public void setMobilePhoneNumber(String mobilePhoneNumber) 
    {
        if(mobilePhoneNumber == null || mobilePhoneNumber.trim().isEmpty())
            throw new IllegalArgumentException("The phone number cannot be null or empty");
        this.mobilePhoneNumber = mobilePhoneNumber;
    }	
}

