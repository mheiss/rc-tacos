package simplexml.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the header for the message object
 * 
 * @author mheiss
 */
public class Message {

	private String command;
	private String contentClazz;
	private Map<String, String> params;

	/**
	 * Default clas constructor
	 */
	public Message() {
		this.params = new HashMap<String, String>();
	}

	/**
	 * Default class constructor for a new message header.
	 * 
	 * @param command
	 *            the message command
	 * @param contentClazz
	 *            the fully qualified class name of the content object
	 */
	public Message(String command, String contentClazz) {
		this.params = new HashMap<String, String>();
		this.command = command;
		this.contentClazz = contentClazz;
	}

	// COMMON METHODS
	public void addParam(String key, String value) {
		params.put(key, value);
	}

	// DEFAULT GETTERS AND SETTERS
	public String getCommand() {
		return command;
	}

	public String getContentClazz() {
		return contentClazz;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setContentClazz(String contentClazz) {
		this.contentClazz = contentClazz;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
