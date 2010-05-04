package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Instances of this message represent a error during the communication with the
 * server
 * 
 * @author Michael
 */
public class SystemMessage {

	private String message;

	/**
	 * Default class constructor
	 */
	public SystemMessage() {
	}

	/**
	 * Default class constructor to create a new message
	 * 
	 * @param message
	 *            the message
	 */
	public SystemMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the human readable string for this <code>SystemMessage</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("message", message);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>SystemMessage</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link SystemMessage#getMessage()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(51, 61);
		builder.append(message);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>SystemMessage</code> instance is equal
	 * to the compared object.
	 * <p>
	 * The compared fields are {@link SystemMessage#getMessage()}
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
		SystemMessage systemMessage = (SystemMessage) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(message, systemMessage.message);
		return builder.isEquals();
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the system message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
