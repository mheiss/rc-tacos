package at.redcross.tacos.web.faces;

import java.net.ConnectException;

/**
 * The {@code ExceptionResolver} is used to determine the cause of an exception
 * and return a meaningful error message and code.
 */
public class ExceptionResolver {

	private final Throwable throwable;

	/**
	 * Creates a new resolver using the given throwable
	 * 
	 * @param throwable
	 *            the throwable to resolve
	 */
	public ExceptionResolver(Throwable throwable) {
		this.throwable = throwable;
	}

	/**
	 * Resolves the given exception and return the appropriate error code as
	 * {@code String}
	 * 
	 * @return the resolved code
	 */
	public String resolve() {
		Throwable cause = resolveRoot(throwable);
		if (cause instanceof ConnectException) {
			return "DATABASE_CONNECTION_FAILED";
		}
		else {
			return "INTERNAL_ERROR";
		}
	}

	// helper method to determine the root of the exception
	private Throwable resolveRoot(Throwable t) {
		Throwable cause = t.getCause();
		while (cause != null) {
			t = cause;
			cause = cause.getCause();
		}
		return t;
	}

}
