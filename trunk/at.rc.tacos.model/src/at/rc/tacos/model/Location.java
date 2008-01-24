package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * This class represents a location.
 * @author Michael
 */
public class Location extends AbstractMessage
{
    //unique identification string
    public static String ID = "location";
    
    //properties
    private int id;
    private String locationName;
    private String street;
    private String streetNumber;
    private int zipcode;
    private String city;
    private String notes;
    private MobilePhoneDetail phone;
    
    //internal
    public String type;
    
    /**
     * Default class constructor for a empty location
     */
    public Location()
    {
        super(ID);
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the locationname
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @return the street
     */
    public String getStreet()
    {
        return street;
    }

    /**
     * @return the streetnumber
     */
    public String getStreetNumber()
    {
        return streetNumber;
    }

    /**
     * @return the zipcode
     */
    public int getZipcode()
    {
        return zipcode;
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @return the notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * @return the phone
     */
    public MobilePhoneDetail getPhone()
    {
        return phone;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @param locationName the locationname to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street)
    {
        this.street = street;
    }

    /**
     * @param streetnumber the streetnumber to set
     */
    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(int zipcode)
    {
        this.zipcode = zipcode;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(MobilePhoneDetail phone)
    {
        this.phone = phone;
    }
 }
