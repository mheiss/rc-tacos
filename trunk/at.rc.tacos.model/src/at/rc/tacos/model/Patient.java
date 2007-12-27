package at.rc.tacos.model;

import java.util.List;

import at.rc.tacos.common.AbstractMessage;


/**
 * Contains the data of the patient.
 * Will be extended if a patient database is implemented
 * @author b.thek
 */
public class Patient extends AbstractMessage
{
    //unique identification string
    public final static String ID = "patient";

    //Rückgabewerte:
    //p.firstname, p.lastname, sex, birthday, t.transport_ID, s.streetname, sn.streetnumber, cy.cityname, cy.zipcode
    private long patientId;
    private String firstname;
    private String lastname;
    private boolean sex;
    private List<String> transportID;
    private String streetname;
    private String streetnumber;
    private String cityname;
    private int zipcode;
    

    /**
     * Default class construtor
     */
    public Patient()
    {
        super(ID);
    }

    /**
     * Default class construtor for a complete patient detail object.
     * @param firstname the firstname 
     * @param lastname the lastname
     */
    public Patient(String firstname, String lastname) 
    {
        super(ID);
        setFirstname(firstname);
        setLastname(lastname);
    }

    //METHODS
    /**
     * Returns a string based description of the object.<br>
     * The returned values are patientId,firstname,lastname.
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return patientId+","+firstname+","+lastname;
    }
    
    /**
     * Returns the calculated hash code based on the patient id.<br>
     * Two patients have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31  + (int) (patientId ^ (patientId >>> 32));
    }   
    
    /**
     * Returns whether the objects are equal or not.<br>
     * Two patients are equal if, and only if, the patient id is the same.
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
        final Patient other = (Patient) obj;
        if (patientId != other.patientId)
            return false;
        return true;
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
     * Sets the identification number of the patient.
     * @param patientId the patientId to set
     * @throws IllegalArgumentException when the patient id is lesser than 0
     */
    public void setPatientId(long patientId) 
    {
        if(patientId < 0)
            throw new IllegalArgumentException("The id cannot be negative");
        this.patientId = patientId;
    }

    /**
     * Returns the firstname of the patient.
     * @return the firstname
     */
    public String getFirstname() 
    {
        return firstname;
    }

    /**
     * Sets the last name for the patient.
     * @param firstname the firstname to set
     * @throws IllegalArgumentException if the first name is null or empty
     */
    public void setFirstname(String firstname) 
    {
        if(firstname == null)
            throw new IllegalArgumentException("Invalid firstname");
        this.firstname = firstname.trim();
    }

    /**
     * Returns the last name of the patient.
     * @return the lastname
     */
    public String getLastname() 
    {
        return lastname;
    }

    /**
     * Sets the last name for the patient.
     * @param lastname the lastname to set
     * @throws IllegalArgumentException if the lastname is null or empty
     */
    public void setLastname(String lastname)
    {
        if(lastname == null)
            throw new IllegalArgumentException("Invalid lastname");
        this.lastname = lastname;
    }

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public List<String> getTransportID() {
		return transportID;
	}

	public void setTransportID(List<String> transportID) {
		this.transportID = transportID;
	}

	public String getStreetname() {
		return streetname;
	}

	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}

	public String getStreetnumber() {
		return streetnumber;
	}

	public void setStreetnumber(String streetnumber) {
		this.streetnumber = streetnumber;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
}
