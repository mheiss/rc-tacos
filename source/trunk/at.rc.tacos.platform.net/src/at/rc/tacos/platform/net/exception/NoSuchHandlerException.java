package at.rc.tacos.platform.net.exception;

/**
 * Instances of this exception indicate that the requested handler cannot be
 * found.
 * 
 * @author mheiss
 */
public class NoSuchHandlerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Throws a exception that the handler cannot be found
	 * 
	 * @param handlerName
	 *            the name of the handler that was not found
	 */
	public NoSuchHandlerException(String handlerName) {
		super("The requested handler '" + handlerName + "' cannot be found");
	}

}
