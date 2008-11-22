package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a sick person
 * 
 * @author b.thek
 */
public class SickPerson {

	private int sickPersonId;
	private String lastName;
	private String firstName;
	private String streetname;
	private String cityname;
	private boolean sex;
	private String SVNR;
	private String kindOfTransport;
	private String notes;

	// define constants
	public final static String SICKPERSON_MALE = "männlich";
	public final static String SICKPERSON_FEMALE = "weiblich";
	public final static String SICKPERSON[] = { SICKPERSON_MALE, SICKPERSON_FEMALE };

	/**
	 * Class constructor for a sick person
	 */
	public SickPerson() {
		sickPersonId = -1;
		lastName = "";
		firstName = "";
	}

	/**
	 * Class constructor for a sick person
	 * 
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 */
	public SickPerson(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	/**
	 * Returns the human readable string for this <code>SickPerson</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", sickPersonId);
		builder.append("firstName", firstName);
		builder.append("lastName", lastName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>SickPerson</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link SickPerson#getSickPersonId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(47, 57);
		builder.append(sickPersonId);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>SickPerson</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link SickPerson#getSickPersonId()}
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
		SickPerson sickPerson = (SickPerson) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(sickPersonId, sickPerson.sickPersonId);
		return builder.isEquals();
	}

	/**
	 * Returns the personal identification number.
	 * 
	 * @return the sickPersonId
	 */
	public int getSickPersonId() {
		return sickPersonId;
	}

	/**
	 * Sets the personal identification number.
	 * 
	 * @param sickPersonId
	 *            the personId to set
	 * @throws IllegalArgumentException
	 *             if the id is negative
	 */
	public void setSickPersonId(int sickPersonId) {
		this.sickPersonId = sickPersonId;
	}

	/**
	 * Returns the last name
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of this sick person
	 * 
	 * @param lastName
	 *            the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of this sick person
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of this sick person
	 * 
	 * @param firstName
	 *            the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSVNR() {
		return SVNR;
	}

	public void setSVNR(String svnr) {
		SVNR = svnr;
	}

	public String getKindOfTransport() {
		return kindOfTransport;
	}

	public void setKindOfTransport(String kindOfTransport) {
		this.kindOfTransport = kindOfTransport;
	}

	/**
	 * Returns the name of the street
	 * 
	 * @return the name of the street
	 */
	public String getStreetname() {
		return streetname;
	}

	/**
	 * Sets the name of the street where this sick person is at home
	 * 
	 * @param streetname
	 *            the name of the street
	 */
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}

	/**
	 * Returns the city name where the sick person is at home
	 * 
	 * @return the city name
	 */
	public String getCityname() {
		return cityname;
	}

	/**
	 * Sets the name of the city where the person is at home
	 * 
	 * @param cityname
	 *            the city name
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
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
	 * Sets a flag to indicate that the patient is male. Set this to false for
	 * female.
	 * 
	 * @param sex
	 *            true if the patient is male, otherwise false
	 */
	public void setMale(boolean sex) {
		this.sex = sex;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
