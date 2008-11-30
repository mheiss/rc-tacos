package at.rc.tacos.platform.services.exception;

/**
 * Instances of this exception indicate that the requested service cannot be
 * found or is currently not available.
 * 
 * @author mheiss
 */
public class NoSuchServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Throws a exception that the service cannot be found
	 * 
	 * @param serviceName
	 *            the name of the service that was not found
	 */
	public NoSuchServiceException(String serviceName) {
		super("The requested service '" + serviceName + "' cannot be found");
	}

	/**
	 * Throws a exception that the service cannot be found
	 * 
	 * @param serviceName
	 *            the name of the service that was not found
	 * @param throwable
	 *            the root cause of the exception
	 */
	public NoSuchServiceException(String serviceName, Throwable throwable) {
		super("The requested service '" + serviceName + "' cannot be found", throwable);
	}

}
