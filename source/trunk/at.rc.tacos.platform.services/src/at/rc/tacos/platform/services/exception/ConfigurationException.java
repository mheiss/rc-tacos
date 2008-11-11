package at.rc.tacos.platform.services.exception;

/**
 * This exception indicates that the configuration is not valid.
 * 
 * @author Michael
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * {@link RuntimeException#RuntimeException(String, Throwable)}
	 */
	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
