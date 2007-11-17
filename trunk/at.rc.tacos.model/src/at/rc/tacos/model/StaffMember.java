package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;


/**
 * Represents a staff member (there are several kinds: regular staff member, civil server, volunteer)
 * but there is no difference between them in this class.
 * @author b.thek
 */
public class StaffMember extends AbstractMessage
{
	private int personId;
	private String lastName;
	private String firstName;
	private String userName;
	
	/**
	 * Class constructor for a staff member
	 */
	public StaffMember()
	{
	    super("staffMember");
	}
	
	/**
	 * Class constructor for a complete staff member
	 * @param personId the personal identification number
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the username of this member
	 */
	public StaffMember(int personId, String firstName, String lastName, String userName)
	{
	    this();
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
	}

	//SETTERS AND GETTERS
	
	/**
	 * Returns the personal identification number
	 * @return the personId
	 */
	public int getPersonId() 
	{
		return personId;
	}

	/**
	 * Sets the personal identification number
	 * @param personId the personId to set
	 */
	public void setPersonId(int personId) 
	{
		this.personId = personId;
	}

	/**
	 * Returns the last name of this staff member
	 * @return the last name
	 */
	public String getLastname() 
	{
		return lastName;
	}

	/**
	 * Sets the last name of this staff member
	 * @param lastname the last name to set
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of this staff member
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of this staff member
	 * @param firstname the first name to set
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	/**
	 * Returns the username for this staff member
	 * @return the user name
	 */
	public String getUserName() 
	{
		return userName;
	}

	/**
	 * Sets the username for this staff member
	 * @param username the user name to set
	 */
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
}
