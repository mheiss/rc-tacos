package at.rc.tacos.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import at.rc.tacos.common.AbstractMessage;

/**
 * Represents a staff member (there are several kinds: regular staff member, civil server, volunteer)
 * but there is no difference between them in this class.
 * @author b.thek
 */
public class StaffMember extends AbstractMessage
{
    //unique identification string
    public final static String ID = "staffMember";

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);    

    private int personId;
    private String lastName;
    private String firstName;
    private String userName;
    private String function;

    /**
     * Class constructor for a staff member
     */
    public StaffMember()
    {
        super(ID);
    }

    /**
     * Class constructor for a complete staff member
     * @param firstName the first name
     * @param lastName the last name
     * @param userName the username of this member
     */
    public StaffMember(String firstName, String lastName, String userName)
    {
        super(ID);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
    }

    // METHODS
    public void addPropertyChangeListener(String propertyName,PropertyChangeListener listener) 
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName,listener);
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
        return 31 + personId;
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
        if (personId != other.personId)
            return false;
        return true;
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
     * Sets the personal identification number.
     * @param personId the personId to set
     * @throws IllegalArgumentException if the id is negative
     */
    public void setPersonId(int personId) 
    {
        if(personId < 0)
            throw new IllegalArgumentException("The id cannot be negative");
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
     * @param firstname the first name to set
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
     * @param username the user name to set
     * @throws IllegalArgumentException if the userName is null or empty
     */
    public void setUserName(String userName) 
    {
        if(userName == null || userName.trim().isEmpty())
            throw new IllegalArgumentException("The userName cannot be null or empty");
        this.userName = userName;
    }

    /**
     * Sets the function of this staff member.<br>
     * Note: This is only used during the enocde to xml.
     * @param function the function of this member
     * @throws IllegalArgumentException if the function is null or empty
     */
    public void setFunction(String function)
    {
        if(function == null || function.trim().isEmpty())
            throw new IllegalArgumentException("The function cannot be null or empty");  
        this.function = function;
    }

    /**
     * Returns the function of this staff member<br>
     * Note: This is only used during the enocde to xml.
     * @return the funtion
     */
    public String getFunction()
    {
        return function;
    }
}
