package at.rc.tacos.model;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.util.MyUtils;

/**
 * Represents a staff member (there are several kinds: regular staff member, civil server, volunteer)
 * but there is no difference between them in this class.
 * @author b.thek
 */
public class StaffMember extends AbstractMessage
{
	//unique identification string
	public final static String ID = "staffMember";

	private int staffMemberId;
	private Location primaryLocation;
	private String lastName;
	private String firstName;
	private String streetname;
	private String cityname;
	private boolean sex;
	private long birthday;
	private List<MobilePhoneDetail> phonelist;
	private List<Competence> competenceList;
	private String eMail;
	private String userName;
	
	//internal information, only needed to serialize and deserialize
	public String function;
	
	//define constants
	public final static String STAFF_MALE = "männlich";
	public final static String STAFF_FEMALE = "weiblich";
	public final static String STAFF[] = { STAFF_MALE, STAFF_FEMALE };

	/**
	 * Class constructor for a staff member
	 */
	public StaffMember()
	{
		super(ID);
		//set default values
		lastName = "";
		firstName = "";
		phonelist = new ArrayList<MobilePhoneDetail>();
		competenceList = new ArrayList<Competence>();
	}

	/**
	 * Class constructor for a complete staff member
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the username of this member
	 */
	public StaffMember(String firstName, String lastName, String userName)
	{
		this();
		setFirstName(firstName);
		setLastName(lastName);
		setUserName(userName);
	}
	
	/**
	 * Class constructor for a complete staff member
	 */
	public StaffMember(int staffId,String firstName, String lastName, String userName,
	        String streetname,String cityname,boolean sex,long birthday,
	        MobilePhoneDetail phone,Competence competence,String eMail,Location primaryLocation)
	{
	    this();
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
	 * Returns a string based description of the object
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
		return userName +","+lastName + " " + firstName;
	}

	/**
	 * Returns the calculated hash code based on the staff member id.<br>
	 * Two staff members have the same hash code if the id is the same.
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode()
	{
		return 31 + staffMemberId;
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two staff members are equal if, and only if, the id is the same.
	 * @return true if the id is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StaffMember other = (StaffMember) obj;
		if (staffMemberId != other.staffMemberId)
			return false;
		return true;
	}

	//SETTERS AND GETTERS
	/**
	 * Returns the personal identification number.
	 * @return the staffMemberId
	 */
	public int getStaffMemberId() 
	{
		return staffMemberId;
	}

	/**
	 * Sets the personal identification number.
	 * @param staffMemberId the personId to set
	 * @throws IllegalArgumentException if the id is negative
	 */
	public void setStaffMemberId(int staffMemberId) 
	{
		if(staffMemberId < 0)
			throw new IllegalArgumentException("The id cannot be negative");
		this.staffMemberId = staffMemberId;
	}
	
	/**
	 * Returns the primary location of this staff member
	 * @return the primary location
	 */
	public Location getPrimaryLocation()
	{
	    return primaryLocation;
	}
	
	/**
	 * Sets the primary location for this staff member
	 * @param primaryLocation the primary location
	 */
	public void setPrimaryLocation(Location primaryLocation)
	{
	    this.primaryLocation = primaryLocation;
	}

	/**
	 * Returns the last name
	 * @return the lastName
	 */
	public String getLastName() 
	{
		return lastName;
	}

	/**
	 * Sets the last name of this staff member
	 * @param lastName the last name to set
	 * @throws IllegalArgumentException if the lastName is null or empty
	 */
	public void setLastName(String lastName) 
	{
		if(lastName == null || lastName.trim().isEmpty())
			throw new IllegalArgumentException("The last name cannot be null or empty");
		this.lastName = lastName;
	}

	/**
	 * Returns the first name of this staff member
	 * @return the first name
	 */
	public String getFirstName() 
	{
		return firstName;
	}

	/**
	 * Sets the first name of this staff member
	 * @param firstName the first name to set
	 * @throws IllegalArgumentException if the first name is null or empty
	 */
	public void setFirstName(String firstName) 
	{
		if(firstName == null || firstName.trim().isEmpty())
			throw new IllegalArgumentException("The first name cannot be null or empty");
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
	 * @param userName the user name to set
	 * @throws IllegalArgumentException if the userName is null or empty
	 */
	public void setUserName(String userName) 
	{
		if(userName == null || userName.trim().isEmpty())
			throw new IllegalArgumentException("The userName cannot be null or empty");
		String oldName = this.userName;
		this.userName = userName;
		firePropertyChange("userName",oldName, userName);
	}

	/**
	 * Returns the name of the street
	 * @return the name of the street
	 */
	public String getStreetname() {
		return streetname;
	}

	/**
	 * Sets the name of the street where this staff member is at home
	 * @param streetname the name of the street
	 */
	public void setStreetname(String streetname) 
	{
		this.streetname = streetname;
	}

	/**
	 * Returns the cityname where the staff member is at home
	 * @return the cityname
	 */
	public String getCityname() 
	{
		return cityname;
	}

	/**
	 * Sets the name of the city where the person is at home
	 * @param cityname the cityname
	 */
	public void setCityname(String cityname) 
	{
		this.cityname = cityname;
	}
	
    /**
     * Returns the birthday of the patient
     * @return the date of birht
     */
    public long getBirthday()
    {
        return birthday;
    }
    
    /**
     * Sets the date of the birthday of this patient
     * @param sex the date of birth
     */
    public void setBirthday(long birhtday)
    {
        if(!MyUtils.isValidDate(birthday))
            throw new IllegalArgumentException("This is not a valid birthday");
        this.birthday = birhtday;
    }
    
    /**
     * Returns whether or not this patient is male.
     * @return true if the patient is male, otherwise female ;)
     */
    public boolean isMale() 
    {
        return sex;
    }

    /**
     * Sets a flag to indicate that the patient is male.
     * Set this to false for female.
     * @param male true if the patient is male, otherwise false
     */
    public void setMale(boolean sex) 
    {
        this.sex = sex;
    }

    /**
     * Returns the mail address of this staff member
     * @return the mail address
     */
	public String getEMail() 
	{
		return eMail;
	}

	/**
	 * Sets the mail address of this staff member
	 * @param mail the mail address
	 */
	public void setEMail(String mail) 
	{
		eMail = mail;
	}
	
	/**
	 * Returns the list of phones accociated to the staff member
	 * @return the phone list
	 */
	public List<MobilePhoneDetail> getPhonelist() 
	{
		return phonelist;
	}
	
	/**
	 * Helper method to add a mobile phone to a staff member
	 * @param phone the mobile phone to add
	 */
	public void addMobilePhone(MobilePhoneDetail phone)
	{
	    phonelist.add(phone);
	}
	
	/**
	 * Sets the list of phones for this staff member
	 * @param phonelist the phone list to set
	 */
	public void setPhonelist(List<MobilePhoneDetail> phonelist) 
	{
		this.phonelist = phonelist;
	}
	
	/**
	 * Returns a list of all competences for this staff member
     * @return the competenceList the competence list
     */
    public List<Competence> getCompetenceList()
    {
        return competenceList;
    }

    /**
     * Sets the list of competences for this staff member
     * @param competenceList the competenceList to set
     */
    public void setCompetenceList(List<Competence> competenceList)
    {
        this.competenceList = competenceList;
    }
    
    /**
     * Helper method to add a competence to a staff member
     * @param competence the competence to add
     */
    public void addCompetence(Competence competence)
    {
        competenceList.add(competence);
    }
}
