package at.rc.tacos.core.model;

/**
 * Specifies the details of the mobile phone
 * @author b.thek
 */

public class MobilePhoneDetail 
{
	private String mobilePhoneId;
	private String mobilePhoneNumber;
	
	/**
	 * Constructors
	 */
	public MobilePhoneDetail(){}

	/**
	 * @param mobilePhoneId
	 * @param mobilphonenumer
	 */
	public MobilePhoneDetail(String mobilePhoneId, String mobilePhoneNumber) {
		super();
		this.mobilePhoneId = mobilePhoneId;
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	
	/**
	 * Setter&Getter
	 */
	
	/**
	 * @return the mobilePhoneId
	 */
	public String getMobilePhoneId() {
		return mobilePhoneId;
	}

	/**
	 * kind of the id: e.g. 'Bm01'  (two chars to specify the station plus a double digit distinct number )- 
	 * - because of the comparison with the primary vehicle
	 * @param mobilePhoneId the mobilePhoneId to set
	 */
	public void setMobilePhoneId(String mobilePhoneId) {
		this.mobilePhoneId = mobilePhoneId;
	}

	/**
	 * @return the mobilePhoneNumber
	 */
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * @param mobilphonenumer the mobilePhoneNumber to set
	 */
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}	
}

