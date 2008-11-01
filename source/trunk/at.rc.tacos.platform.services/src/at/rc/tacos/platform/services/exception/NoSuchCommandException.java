package at.rc.tacos.platform.services.exception;

/**
 * This exception is thrown is the requested command is unknown or not
 * implemented.
 * 
 * @author Michael
 */
public class NoSuchCommandException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public NoSuchCommandException(String serviceName) {
		super("The command '" + serviceName + "' could not be executed, command is unknown");
	}

}
