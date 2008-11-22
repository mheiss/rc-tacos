package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Contains the data of the patient. Will be extended if a patient database is
 * implemented
 * 
 * @author b.thek
 */
public class Patient {

	// properties
	private long patientId;
	private String firstname;
	private String lastname;
	private boolean sex;
	private long birthday;

	/**
	 * Default class construtor
	 */
	public Patient() {
		patientId = 0;
		firstname = "";
		lastname = "";
	}

	/**
	 * Default class construtor for a complete patient detail object.
	 * 
	 * @param firstname
	 *            the firstname
	 * @param lastname
	 *            the lastname
	 */
	public Patient(String firstname, String lastname) {
		setFirstname(firstname);
		setLastname(lastname);
	}

	/**
	 * Returns the human readable string for this <code>Patient</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", patientId);
		builder.append("firstname", firstname);
		builder.append("lastname", lastname);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Patient</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Patient#getFirstname()} and
	 * {@link Patient#getLastname()}.
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(39, 49);
		builder.append(firstname);
		builder.append(lastname);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Patient</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Patient#getFirstname()} and
	 * {@link Patient#getLastname()}.
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Patient patient = (Patient) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(firstname, patient.firstname);
		builder.append(lastname, patient.lastname);
		return builder.isEquals();
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the identification number of the patient
	 * 
	 * @return the patientId
	 */
	public long getPatientId() {
		return patientId;
	}

	/**
	 * Returns the last name of the patient.
	 * 
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Returns the firstname of the patient.
	 * 
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Returns the birthday of the patient
	 * 
	 * @return the date of birht
	 */
	public long getBirthday() {
		return birthday;
	}

	/**
	 * Returns whether or not this patient is male.
	 * 
	 * @return true if the patient is male, otherwise female ;)
	 */
	public boolean isMale() {
		return sex;
	}

	/**
	 * Sets the identification number of the patient.
	 * 
	 * @param patientId
	 *            the patientId to set
	 */
	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	/**
	 * Sets the last name for the patient.
	 * 
	 * @param firstname
	 *            the firstname to set
	 * @throws IllegalArgumentException
	 *             if the first name is null or empty
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname.trim();
	}

	/**
	 * Sets the last name for the patient.
	 * 
	 * @param lastname
	 *            the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Sets the date of the birthday of this patient
	 * 
	 * @param birhtday
	 *            the date of birth
	 */
	public void setBirthday(long birhtday) {
		this.birthday = birhtday;
	}

	/**
	 * Sets a flag to indicate that the patient is male. Set this to false for
	 * female.
	 * 
	 * @param sex
	 *            true if the patient is male, otherwise false
	 */
	public void setMale(boolean sex) {
		this.sex = sex;
	}
}
