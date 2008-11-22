package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Specifies the details of the mobile phone
 * 
 * @author b.thek
 */
public class MobilePhoneDetail {

	private int id;
	private String mobilePhoneName;
	private String mobilePhoneNumber;

	/**
	 * Default class constructors
	 */
	public MobilePhoneDetail() {
		id = -1;
		mobilePhoneName = "";
		mobilePhoneNumber = "";
	}

	/**
	 * Constructor for a complete mobile phone object.
	 * 
	 * @param mobilePhoneName
	 *            the name of the mobile phone.
	 * @param mobilePhoneNumber
	 *            the number of the phone
	 */
	public MobilePhoneDetail(String mobilePhoneName, String mobilePhoneNumber) {
		id = -1;
		setMobilePhoneName(mobilePhoneName);
		setMobilePhoneNumber(mobilePhoneNumber);
	}

	/**
	 * Returns the human readable string for this <code>MobilePhoneDetail</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("name", mobilePhoneName);
		builder.append("phone", mobilePhoneNumber);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>MobilePhoneDetail</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link MobilePhoneDetail#getId()} and
	 * {@link MobilePhoneDetail#mobilePhoneNumber}.
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(37, 47);
		builder.append(id);
		builder.append(mobilePhoneNumber);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>MobilePhoneDetail</code> instance is
	 * equal to the compared object.
	 * <p>
	 * The compared fields are {@link MobilePhoneDetail#getId()} and
	 * {@link MobilePhoneDetail#mobilePhoneNumber}.
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
		MobilePhoneDetail mobilePhoneDetail = (MobilePhoneDetail) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, mobilePhoneDetail.id);
		builder.append(mobilePhoneNumber, mobilePhoneDetail.mobilePhoneNumber);
		return builder.isEquals();
	}

	/**
	 * Returns the internal identification number of the mobile phone. The id is
	 * a internal value in the database to identify the phone.
	 * 
	 * @return id the uniqe id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the mobile phone.<br>
	 * When the mobile phone is accociated to a vehicle the name of the phone is
	 * equal to the name of the vehicle.
	 * 
	 * @return the name of the phone
	 */
	public String getMobilePhoneName() {
		return mobilePhoneName;
	}

	/**
	 * Returns the mobile phone number
	 * 
	 * @return the mobilePhoneNumber
	 */
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * Sets the id of this mobile phone.
	 * 
	 * @param id
	 *            the unique id out of the database
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the identification of the mobile phone.<br>
	 * The string is constructed using two chars to specify the station plus a
	 * double digit distinct number.<br>
	 * Example: BM01
	 * 
	 * @param mobilePhoneName
	 *            the mobilePhoneId to set
	 */
	public void setMobilePhoneName(String mobilePhoneName) {
		this.mobilePhoneName = mobilePhoneName;
	}

	/**
	 * Sets the mobile phone number.
	 * 
	 * @param mobilePhoneNumber
	 *            the mobilePhoneNumber to set
	 */
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
}
