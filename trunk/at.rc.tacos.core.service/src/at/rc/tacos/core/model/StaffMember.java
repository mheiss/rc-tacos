package at.rc.tacos.core.model;

/**
 * Represents a staff member (there are several kinds: regular staff member, civil server, volunteer)
 * but there is no difference between them in this class
 * @author b.thek
 */

public class StaffMember 
{
	private int personId;
	private String lastName;
	private String firstName;
	private String userName;
	
	/**
	 * Constructors
	 */
	public StaffMember(){}
	
	public StaffMember(int personId, String firstName, String lastName, String userName)
	{
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
	}

	
	/**
	 * Setter & Getter
	 * @param personId
	 * @param firstName
	 * @param lastName
	 * @param userName
	 */
	
	/**
	 * @return the personId
	 */
	public int getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * @return the last name
	 */
	public String getLastname() {
		return lastName;
	}

	/**
	 * @param lastname the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstname the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param username the user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
