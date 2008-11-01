package at.rc.tacos.platform.services.exception;

/**
 * Service exception is used to encapsulate all error that occured during the
 * interaction with services.
 * 
 * @author Michael
 */
public class ServiceException extends Exception {

	/**
	 * The identification string
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Throws a new service exception.
	 * 
	 * @param errorMessage
	 *            the error that occured
	 */
	public ServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Throws a new service exception with a source and a error message.
	 * 
	 * @param source
	 *            the source where the exception occured
	 * @param errorMessage
	 *            the error that occured
	 * @param throwable
	 *            the root cause of the exception
	 */
	public ServiceException(String errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}

}
