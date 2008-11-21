package at.rc.tacos.platform.model;

/**
 * Instances of this message represent a error during the communication with the
 * server
 * 
 * @author Michael
 */
public class SystemMessage {

	private String message;

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
	 * Returns the system message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
