package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;


/**
 * Contains the data of the patient.
 * Will be extended if a patient database is implemented
 * @author b.thek
 */
public class Patient extends AbstractMessage
{
	private long patientId;
	private String firstname;
	private String lastname;
	
	/**
	 * Default class construtor
	 */
	public Patient()
	{
	    super("patient");
	}

	/**
	 * Default class construtor for a complete patient detail object.
	 * @param patientId the identification number of the patient
	 * @param firstname the firstname 
	 * @param lastname the lastname
	 */
	public Patient(long patientId, String firstname, String lastname) 
	{
		this();
		this.patientId = patientId;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	//GETTERS AND SETTERS
	
	/**
	 * Returns the identification number of the patient
	 * @return the patientId
	 */
	public long getPatientId() 
	{
		return patientId;
	}

	/**
	 * Sets the identification number
	 * @param patientId the patientId to set
	 */
	public void setPatientId(long patientId) 
	{
		this.patientId = patientId;
	}

	/**
	 * Returns the firstname of the patient
	 * @return the firstname
	 */
	public String getFirstname() 
	{
		return firstname;
	}

	/**
	 * Sets the last name for the patient
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) 
	{
		this.firstname = firstname;
	}

	/**
	 * Returns the last name of the patient
	 * @return the lastname
	 */
	public String getLastname() 
	{
		return lastname;
	}

	/**
	 * Sets the last name for the patient
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}	
}
