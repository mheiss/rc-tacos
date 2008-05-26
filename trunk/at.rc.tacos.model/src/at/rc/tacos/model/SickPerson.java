package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * Represents a sick person
 * @author b.thek
 */
public class SickPerson extends AbstractMessage
{
	//unique identification string
	public final static String ID = "sickPerson";

	private int sickPersonId;
	private String lastName;
	private String firstName;
	private String streetname;
	private String cityname;
	private boolean sex;
	private String SVNR;
	private String kindOfTransport;
	private String notes;

	//define constants
	public final static String SICKPERSON_MALE = "männlich";
	public final static String SICKPERSON_FEMALE = "weiblich";
	public final static String SICKPERSON[] = { SICKPERSON_MALE, SICKPERSON_FEMALE };

	/**
	 * Class constructor for a sick person
	 */
	public SickPerson()
	{
		super(ID);
		//set default values
		sickPersonId = -1;
		lastName = "";
		firstName = "";
		
	}

	/**
	 * Class constructor for a sick person
	 * @param firstName the first name
	 * @param lastName the last name
	 */
	public SickPerson(String firstName, String lastName)
	{
		this();
		setFirstName(firstName);
		setLastName(lastName);
	}
	

	/**
	 * Returns a string based description of the object
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
		return "id: "+sickPersonId+";"
		+"; Nachn: "+lastName+"; Vorn: "+firstName;
	}

	/**
	 * Returns the calculated hash code based on the sick person id.<br>
	 * Two sick persons have the same hash code if the id is the same.
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode()
	{
		return 31 + sickPersonId;
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two sick persons are equal if, and only if, the id is the same.
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
		final SickPerson other = (SickPerson) obj;
		if (sickPersonId != other.sickPersonId)
			return false;
		return true;
	}

	//SETTERS AND GETTERS
	/**
	 * Returns the personal identification number.
	 * @return the sickPersonId
	 */
	public int getSickPersonId() 
	{
		return sickPersonId;
	}

	/**
	 * Sets the personal identification number.
	 * @param sickPersonId the personId to set
	 * @throws IllegalArgumentException if the id is negative
	 */
	public void setSickPersonId(int sickPersonId) 
	{
		this.sickPersonId = sickPersonId;
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
	 * Sets the last name of this sick person
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
	 * Returns the first name of this sick person
	 * @return the first name
	 */
	public String getFirstName() 
	{
		return firstName;
	}

	/**
	 * Sets the first name of this sick person
	 * @param firstName the first name to set
	 * @throws IllegalArgumentException if the first name is null or empty
	 */
	public void setFirstName(String firstName) 
	{
		if(firstName == null || firstName.trim().isEmpty())
			throw new IllegalArgumentException("The first name cannot be null or empty");
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
	 * @return the name of the street
	 */
	public String getStreetname() {
		return streetname;
	}

	/**
	 * Sets the name of the street where this sick person is at home
	 * @param streetname the name of the street
	 */
	public void setStreetname(String streetname) 
	{
		this.streetname = streetname;
	}

	/**
	 * Returns the city name where the sick person is at home
	 * @return the city name
	 */
	public String getCityname() 
	{
		return cityname;
	}

	/**
	 * Sets the name of the city where the person is at home
	 * @param cityname the city name
	 */
	public void setCityname(String cityname) 
	{
		this.cityname = cityname;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
