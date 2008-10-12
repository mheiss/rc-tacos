package at.rc.tacos.platform.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the header for the message object
 * 
 * @author mheiss
 */
public class MessageCommand {

	private String command;
	private Map<String, String> params;

	/**
	 * Default clas constructor
	 */
	public MessageCommand() {
		this.params = new HashMap<String, String>();
	}

	/**
	 * Default class constructor for a new message header.
	 * 
	 * @param command
	 *            the message command
	 */
	public MessageCommand(String command) {
		this.params = new HashMap<String, String>();
		this.command = command;
	}

	// COMMON METHODS
	public void addParam(String key, String value) {
		params.put(key, value);
	}

	// DEFAULT GETTERS AND SETTERS
	public String getCommand() {
		return command;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
