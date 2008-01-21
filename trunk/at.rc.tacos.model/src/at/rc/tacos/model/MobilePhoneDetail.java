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

    private int mobilePhoneId;
    private String mobilePhoneNumber;

    /**
     * Default class constructors
     */
    public MobilePhoneDetail() 
    { 
        super(ID);
    }
    
    /**
     * Constructor for a minimal mobile phone object
     * @param mobilePhoneId the id of the details
     */
    public MobilePhoneDetail(String mobilePhoneId) 
    {
        super(ID);
        setMobilePhoneId(mobilePhoneId);
    }

    /**
     * Constructor for a complete mobile phone detail
     * @param mobilePhoneId the id of the details
     * @param mobilePhoneNumber the mobile phone number
     */
    public MobilePhoneDetail(String mobilePhoneId, String mobilePhoneNumber) 
    {
        super(ID);
        setMobilePhoneId(mobilePhoneId);
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
        return mobilePhoneId+mobilePhoneNumber;
    }

    /**
     * Returns the calculated hash code based on the mobile phone id.<br>
     * Two mobile phone entries have the same hash code if the id is equal.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31 +  mobilePhoneId.hashCode();
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
        if (!mobilePhoneId.equals(other.mobilePhoneId))
            return false;
        return true;
    }

    //SETTERS AND GETTERS
    /**
     * Returns the id of the mobile phone object.
     * @return the id of the mobile phone
     */
    public int getMobilePhoneId() 
    {
        return mobilePhoneId;
    }

    /**
     * Sets the identification of the mobile phone.<br>
     * The string is constructed using two chars to specify the station 
     * plus a double digit distinct number.<br>
     * Example: BM01
     * @param mobilePhoneId the mobilePhoneId to set
     * @throws IllegalArgumentException if the mobilePhoneId is null or empty
     */
    public void setMobilePhoneId(int mobilePhoneId)
    {
        if(mobilePhoneId == null || mobilePhoneId.trim().isEmpty())
            throw new IllegalArgumentException("The id cannot be null or empty");
        this.mobilePhoneId = mobilePhoneId;
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

