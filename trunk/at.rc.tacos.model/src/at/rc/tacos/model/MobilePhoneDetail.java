package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Specifies the details of the mobile phone
 * @author b.thek
 */
public class MobilePhoneDetail extends AbstractMessage
{
	private String mobilePhoneId;
	private String mobilePhoneNumber;
	
	/**
	 * Default class constructors
	 */
	public MobilePhoneDetail() 
	{ 
	    super("mobilePhone");
	}

	/**
	 * Constructor for a complete mobile pbone detail
	 * @param mobilePhoneId
	 * @param mobilphonenumer
	 */
	public MobilePhoneDetail(String mobilePhoneId, String mobilePhoneNumber) 
	{
	    this();
		this.mobilePhoneId = mobilePhoneId;
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	
	//SETTERS AND GETTERS
	
	/**
	 * @return the mobilePhoneId
	 */
	public String getMobilePhoneId() 
	{
		return mobilePhoneId;
	}

	/**
	 * Sets the identification of the mobile phone.
	 * The string is constructed using two chars to specify the station 
	 * plus a double digit distinct number.<br>
	 * Example: BM01
	 * @param mobilePhoneId the mobilePhoneId to set
	 */
	public void setMobilePhoneId(String mobilePhoneId)
	{
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
	 * Sets the mobile phone number
	 * @param mobilphonenumer the mobilePhoneNumber to set
	 */
	public void setMobilePhoneNumber(String mobilePhoneNumber) 
	{
		this.mobilePhoneNumber = mobilePhoneNumber;
	}	
}

