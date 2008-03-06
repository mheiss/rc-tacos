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
		//set default values
		id = -1;
		locationName = "";
	}

	/**
	 * Default class constructor for a complet location object
	 * @param locationName the name of the station
	 * @param phone the mobilePhone of the location
	 * @param street the street name
	 * @param streetNumber the number of the street
	 * @param zipcode the zip code
	 * @param city the name of the city
	 * @param notes some notes to add to the station
	 */
	public Location(String locationName,MobilePhoneDetail phone,String street,String streetNumber,
			int zipcode,String city,String notes) {
		this();
		this.locationName = locationName;
		this.phone = phone;
		this.street = street;
		this.streetNumber = streetNumber;
		this.zipcode = zipcode;
		this.city = city;
		this.notes = notes;
	}

	//METHODS
	/**
	 * Returns a string based description of the object.<br>
	 * The returned values are the location id and the name.
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
		return id +","+locationName;
	}

	/**
	 * Returns the calculated hash code based on the location id.<br>
	 * Two locations have the same hash code if the id is the same.
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two locations are equal if, and only if, the location id is the same.
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
		final Location other = (Location) obj;
		if (id != other.id)
			return false;
		return true;
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
