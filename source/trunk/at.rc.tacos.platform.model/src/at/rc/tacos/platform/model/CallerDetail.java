package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Specifies the details of the caller. The caller is most of the time the
 * telephoner, sometimes an employee.
 * 
 * @author b.thek
 */
public class CallerDetail extends Lockable {

	private int id;
	private String name;
	private String phoneNumber;

	/**
	 * Default class constructor
	 */
	public CallerDetail() {
		id = -1;
		name = "";
		phoneNumber = "";
	}

	/**
	 * Default class constructor for a complete caller object
	 * 
	 * @param name
	 *            the name of the caller
	 * @param phoneNumber
	 *            the telephone number
	 */
	public CallerDetail(String name, String phoneNumber) {
		setCallerName(name);
		setCallerTelephoneNumber(phoneNumber);
	}

	/**
	 * Returns the human readable string for this <code>CallerDetail</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("callerName", name);
		builder.append("callerPhone", phoneNumber);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>CallerDetail</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link CallerDetail#getId()}.
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(19, 39);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>CallerDetail</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link CallerDetail#getId()}
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
		CallerDetail detail = (CallerDetail) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, detail.id);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return id;
	}

	@Override
	public Class<?> getLockedClass() {
		return CallerDetail.class;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the id of the caller.<br>
	 * The id is a internal value to identify the caller
	 * 
	 * @return id the unique id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the caller.
	 * 
	 * @return the callerName
	 */
	public String getCallerName() {
		return name;
	}

	/**
	 * Sets the unique id for the caller object.<br>
	 * The id is the number of the primary key in the database.
	 * 
	 * @param callerId
	 *            the id of the caller
	 */
	public void setId(int callerId) {
		this.id = callerId;
	}

	/**
	 * Sets the name of the person who called the emergency.
	 * 
	 * @param callerName
	 *            the first name and the last name of the person
	 * @throws IllegalArgumentException
	 *             if the callerName is null or empty
	 */
	public void setCallerName(String callerName) {
		if (callerName == null)
			throw new IllegalArgumentException("Caller name cannot be null");
		this.name = callerName.trim();
	}

	/**
	 * Returns the telephone number of the person who called the emergency.
	 * 
	 * @return the callerTelephoneNumber
	 */
	public String getCallerTelephoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the telephone number of the person who called the emergency
	 * 
	 * @param callerTelephoneNumber
	 *            the telephone number of the caller
	 * @throws IllegalArgumentException
	 *             if the telephone number is null or empty
	 */
	public void setCallerTelephoneNumber(String callerTelephoneNumber) {
		if (callerTelephoneNumber == null)
			throw new IllegalArgumentException("Telephone number cannot be null");
		this.phoneNumber = callerTelephoneNumber.trim();
	}
}
