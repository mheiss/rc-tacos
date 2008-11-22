package at.rc.tacos.platform.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a staff member (there are several kinds: regular staff member,
 * civil server, volunteer) but there is no difference between them in this
 * class.
 * 
 * @author b.thek
 */
public class StaffMember {

	private int staffMemberId;
	private Location primaryLocation;
	private String lastName;
	private String firstName;
	private String streetname;
	private String cityname;
	private boolean sex;
	private String birthday;
	private List<MobilePhoneDetail> phonelist;
	private List<Competence> competenceList;
	private String eMail;
	private String userName;
	private String phone1;
	private String phone2;

	// define constants
	public final static String STAFF_MALE = "männlich";
	public final static String STAFF_FEMALE = "weiblich";
	public final static String STAFF[] = { STAFF_MALE, STAFF_FEMALE };

	/**
	 * Class constructor for a staff member
	 */
	public StaffMember() {
		staffMemberId = -1;
		lastName = "";
		firstName = "";
		phone1 = "";
		phone2 = "";
		phonelist = new ArrayList<MobilePhoneDetail>();
		competenceList = new ArrayList<Competence>();
	}

	/**
	 * Class constructor for a complete staff member
	 * 
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param userName
	 *            the username of this member
	 */
	public StaffMember(String firstName, String lastName, String userName) {
		setFirstName(firstName);
		setLastName(lastName);
		setUserName(userName);
	}

	/**
	 * Class constructor for a complete staff member
	 */
	public StaffMember(int staffId, String firstName, String lastName, String userName, String streetname, String cityname, boolean sex, String birthday, MobilePhoneDetail phone, Competence competence, String eMail, Location primaryLocation) {
		setStaffMemberId(staffId);
		setFirstName(firstName);
		setLastName(lastName);
		setUserName(userName);
		setStreetname(streetname);
		setCityname(cityname);
		setMale(sex);
		setBirthday(birthday);
		addCompetence(competence);
		addMobilePhone(phone);
		setEMail(eMail);
		setPrimaryLocation(primaryLocation);
	}

	/**
	 * Returns the human readable string for this <code>StaffMember</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", staffMemberId);
		builder.append("OS", primaryLocation);
		builder.append("firstName", firstName);
		builder.append("lastName", lastName);
		builder.append("username", userName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>StaffMember</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link StaffMember#getStaffMemberId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(49, 59);
		builder.append(staffMemberId);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>StaffMember</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link StaffMember#getStaffMemberId()}
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
		StaffMember staffMember = (StaffMember) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(staffMemberId, staffMember.staffMemberId);
		return builder.isEquals();
	}

	/**
	 * Returns the personal identification number.
	 * 
	 * @return the staffMemberId
	 */
	public int getStaffMemberId() {
		return staffMemberId;
	}

	/**
	 * Sets the personal identification number.
	 * 
	 * @param staffMemberId
	 *            the personId to set
	 * @throws IllegalArgumentException
	 *             if the id is negative
	 */
	public void setStaffMemberId(int staffMemberId) {
		this.staffMemberId = staffMemberId;
	}

	/**
	 * Returns the primary location of this staff member
	 * 
	 * @return the primary location
	 */
	public Location getPrimaryLocation() {
		return primaryLocation;
	}

	/**
	 * Sets the primary location for this staff member
	 * 
	 * @param primaryLocation
	 *            the primary location
	 */
	public void setPrimaryLocation(Location primaryLocation) {
		this.primaryLocation = primaryLocation;
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
	 * Sets the last name of this staff member
	 * 
	 * @param lastName
	 *            the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of this staff member
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of this staff member
	 * 
	 * @param firstName
	 *            the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the username for this staff member
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the username for this staff member
	 * 
	 * @param userName
	 *            the user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * Sets the name of the street where this staff member is at home
	 * 
	 * @param streetname
	 *            the name of the street
	 */
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}

	/**
	 * Returns the cityname where the staff member is at home
	 * 
	 * @return the cityname
	 */
	public String getCityname() {
		return cityname;
	}

	/**
	 * Sets the name of the city where the person is at home
	 * 
	 * @param cityname
	 *            the cityname
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	/**
	 * Returns the birthday of the patient
	 * 
	 * @return the date of birth
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * Sets the date of the birthday of this patient
	 * 
	 * @param birhtday
	 *            the date of birth
	 */
	public void setBirthday(String birhtday) {
		this.birthday = birhtday;
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

	/**
	 * Returns the mail address of this staff member
	 * 
	 * @return the mail address
	 */
	public String getEMail() {
		return eMail;
	}

	/**
	 * Sets the mail address of this staff member
	 * 
	 * @param mail
	 *            the mail address
	 */
	public void setEMail(String mail) {
		eMail = mail;
	}

	/**
	 * Returns the list of phones accociated to the staff member
	 * 
	 * @return the phone list
	 */
	public List<MobilePhoneDetail> getPhonelist() {
		return phonelist;
	}

	/**
	 * Helper method to add a mobile phone to a staff member
	 * 
	 * @param phone
	 *            the mobile phone to add
	 */
	public void addMobilePhone(MobilePhoneDetail phone) {
		phonelist.add(phone);
	}

	/**
	 * Sets the list of phones for this staff member
	 * 
	 * @param phonelist
	 *            the phone list to set
	 */
	public void setPhonelist(List<MobilePhoneDetail> phonelist) {
		this.phonelist = phonelist;
	}

	/**
	 * Returns a list of all competences for this staff member
	 * 
	 * @return the competenceList the competence list
	 */
	public List<Competence> getCompetenceList() {
		return competenceList;
	}

	/**
	 * Sets the list of competences for this staff member
	 * 
	 * @param competenceList
	 *            the competenceList to set
	 */
	public void setCompetenceList(List<Competence> competenceList) {
		this.competenceList = competenceList;
	}

	/**
	 * Helper method to add a competence to a staff member
	 * 
	 * @param competence
	 *            the competence to add
	 */
	public void addCompetence(Competence competence) {
		competenceList.add(competence);
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
}
