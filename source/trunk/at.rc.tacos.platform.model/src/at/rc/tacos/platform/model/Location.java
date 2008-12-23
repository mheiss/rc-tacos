package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a location.
 * 
 * @author Michael
 */
public class Location extends Lockable {

	private int id;
	private String locationName;
	private String street;
	private String streetNumber;
	private int zipcode;
	private String city;
	private String notes;
	private MobilePhoneDetail phone;

	/**
	 * Default class constructor for a empty location
	 */
	public Location() {
		id = -1;
		locationName = "";
	}

	/**
	 * Default class constructor for a complete location object
	 * 
	 * @param locationName
	 *            the name of the station
	 * @param phone
	 *            the mobilePhone of the location
	 * @param street
	 *            the street name
	 * @param streetNumber
	 *            the number of the street
	 * @param zipcode
	 *            the zip code
	 * @param city
	 *            the name of the city
	 * @param notes
	 *            some notes to add to the station
	 */
	public Location(String locationName, MobilePhoneDetail phone, String street, String streetNumber, int zipcode, String city, String notes) {
		this.locationName = locationName;
		this.phone = phone;
		this.street = street;
		this.streetNumber = streetNumber;
		this.zipcode = zipcode;
		this.city = city;
		this.notes = notes;
	}

	/**
	 * Returns the human readable string for this <code>Location</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("OS", locationName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Location</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Location#getId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(59, 69);
		builder.append(id);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Location</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Location#getId()}.
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
		Location location = (Location) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, location.id);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return id;
	}

	@Override
	public Class<?> getLockedClass() {
		return Location.class;
	}

	// GETTERS AND SETTERS
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the locationname
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the streetnumber
	 */
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @return the zipcode
	 */
	public int getZipcode() {
		return zipcode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return the phone
	 */
	public MobilePhoneDetail getPhone() {
		return phone;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param locationName
	 *            the locationname to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @param streetNumber
	 *            the streetnumber to set
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	/**
	 * @param zipcode
	 *            the zipcode to set
	 */
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(MobilePhoneDetail phone) {
		this.phone = phone;
	}
}
