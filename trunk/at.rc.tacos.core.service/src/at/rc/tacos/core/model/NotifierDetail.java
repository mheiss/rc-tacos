package at.rc.tacos.core.model;

/**
 * Specifies the details of the notifier
 * The notifier is most of the time the telephoner, sometimes an employee
 * @author b.thek
 */

public class NotifierDetail 
{
	private String notifierName;
	private String notifierTelephoneNumber;
	private String notifierNotes;
	
	
	/**
	 * Constructors
	 */
	public NotifierDetail(){}


	/**
	 * @param notifierName
	 * @param notifierTelephoneNumber
	 * @param notifierNotes
	 */
	public NotifierDetail(String notifierName, String notifierTelephoneNumber,
			String notifierNotes) {
		super();
		this.notifierName = notifierName;
		this.notifierTelephoneNumber = notifierTelephoneNumber;
		this.notifierNotes = notifierNotes;
	}


	
	/**
	 * Getter&Setter
	 */
	
	/**
	 * @return the notifierName
	 */
	public String getNotifierName() {
		return notifierName;
	}


	/**
	 * @param notifierName the notifierName to set
	 */
	public void setNotifierName(String notifierName) {
		this.notifierName = notifierName;
	}


	/**
	 * @return the notifierTelephoneNumber
	 */
	public String getNotifierTelephoneNumber() {
		return notifierTelephoneNumber;
	}


	/**
	 * @param notifierTelephoneNumber the notifierTelephoneNumber to set
	 */
	public void setNotifierTelephoneNumber(String notifierTelephoneNumber) {
		this.notifierTelephoneNumber = notifierTelephoneNumber;
	}


	/**
	 * @return the notifierNotes
	 */
	public String getNotifierNotes() {
		return notifierNotes;
	}


	/**
	 * @param notifierNotes the notifierNotes to set
	 */
	public void setNotifierNotes(String notifierNotes) {
		this.notifierNotes = notifierNotes;
	}
}
