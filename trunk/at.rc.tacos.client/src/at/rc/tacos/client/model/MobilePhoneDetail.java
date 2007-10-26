package at.rc.tacos.client.model;

/**
 * Spezifies the details of the mobilephone
 * @author b.thek
 */

public class MobilePhoneDetail 
{

	private String mobilePhoneId;//Muss von Art: z.B. 'Bm01' sein (Zweistelliges Ortsstellen-Kennzeichen, zweistellige Nummer)- zwecks Vergleich mit zugehörigem Fahrzeug
	private String mobilePhoneNumber;
	
	/**
	 * Constructors
	 */
	public MobilePhoneDetail()
	{
		
	}

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

