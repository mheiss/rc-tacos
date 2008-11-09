package at.rc.tacos.platform.services.net;

/**
 * Handler factory interface.
 * 
 * @author Michael
 */
public interface HandlerFactory {

	/**
	 * Returns a type save handler instance
	 * 
	 * @param type
	 *            the type of the handler to get
	 * @return the handler instance or null if nothing found
	 */
	public <T> INetHandler<T> getTypeSaveHandler(T type);
}
