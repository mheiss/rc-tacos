package at.rc.tacos.model;


/**
 * Contains the data of the patient
 * Will be extended if a patient database is implemented
 * @author b.thek
 */

public class Patient 
{
	private long patientId;
	private String firstname;
	private String lastname;
	
	/**
	 * Constructors
	 */
	public Patient(){}

	/**
	 * @param patientId
	 * @param firstname
	 * @param lastname
	 */
	public Patient(long patientId, String firstname, String lastname) {
		super();
		this.patientId = patientId;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	
	/**
	 * Getter&Setter
	 */
	
	/**
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}	
}
