package at.rc.tacos.platform.model;

/**
 * This class represents a simple system message that can be used for variouse
 * things.
 * 
 * @author Michael
 */
public class SystemMessage {

	private String message;
	private int type;

	/**
	 * Default class constructor.
	 */
	public SystemMessage() {
	}

	/**
	 * Default class constructor for a system message specifying the message
	 * type
	 * 
	 * @param message
	 *            the message
	 */
	public SystemMessage(String message, int type) {
		setType(type);
		setMessage(message);
	}

	/**
	 * Returns the given system message.
	 * 
	 * @return the message
	 */
	@Override
	public String toString() {
		return "message: " + message + "; type: " + type;
	}

	/**
	 * Sets the system message.
	 * 
	 * @param message
	 *            the message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the system message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the type of the system message.
	 * 
	 * @param type
	 *            the type of the message
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Returns the type of the system message to categorize them
	 * 
	 * @return the type of the message
	 */
	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemMessage other = (SystemMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		}
		else if (!message.equals(other.message))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
