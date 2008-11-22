package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Stores information about a specifiy address
 * 
 * @author Michael
 */
public class Address {

	private int id;
	private int zip;
	private String city;
	private String street;
	private String streetNumber;

	/**
	 * Default class constructor
	 */
	public Address() {
		city = "";
		street = "";
		streetNumber = "-";
	}

	/**
	 * Default class constructor for a complete address object.
	 * 
	 * @param zip
	 *            the zip code
	 * @param city
	 *            the name of the city
	 * @param street
	 *            the name of the street
	 */
	public Address(int zip, String city, String street) {
		this();
		this.zip = zip;
		this.city = city;
		this.street = street;
		this.streetNumber = "-";
	}

	/**
	 * Returns the human readable string for this <code>Address</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("zip", zip);
		builder.append("city", city);
		builder.append("street", street);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Address</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Address#getCity()},
	 * {@link Address#getStreet()} and {@link Address#getZip()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(17, 37);
		builder.append(city);
		builder.append(street);
		builder.append(zip);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Address</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link Address#getCity()},
	 * {@link Address#getStreet()} and {@link Address#getZip()}
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
		Address adr = (Address) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(city, adr.city);
		builder.append(street, adr.street);
		builder.append(zip, adr.zip);
		return builder.isEquals();
	}

	/**
	 * Returns the zip code of this address record
	 * 
	 * @return the zip code
	 */
	public int getZip() {
		return zip;
	}

	/**
	 * Returns the name of the city.
	 * 
	 * @return the name of the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Returns the name of the street
	 * 
	 * @return the streetname
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the zip code.
	 * 
	 * @param zip
	 *            the zip code as number
	 */
	public void setZip(int zip) {
		this.zip = zip;
	}

	/**
	 * Sets the name of the city
	 * 
	 * @param city
	 *            the cityname
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Sets the name of the street
	 * 
	 * @param street
	 *            the streetname
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Returns the id of the address record
	 * 
	 * @return the id of the address record
	 */
	public int getAddressId() {
		return id;
	}

	/**
	 * Sets the id of the address record
	 * 
	 * @param addressId
	 */
	public void setAddressId(int addressId) {
		this.id = addressId;
	}

	/**
	 * Returns the street number
	 * 
	 * @return streetnumber
	 */
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * Sets the street number
	 * 
	 * @param streetNumber
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
}
