package at.rc.tacos.platform.net.request;

import at.rc.tacos.platform.model.Login;

/**
 * Specialized request message for the logout.
 * 
 * @author mheiss
 */
public class LogoutMessage extends ExecMessage<Login> {

	/**
	 * Default class constructor for a new logout message
	 */
	public LogoutMessage() {
		// logout messages are also handled by the login handler
		super(new Login());
	}
}
