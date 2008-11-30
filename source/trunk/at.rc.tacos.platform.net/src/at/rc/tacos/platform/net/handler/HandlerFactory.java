package at.rc.tacos.platform.net.handler;

/**
 * Handler factory interface.
 * 
 * @author Michael
 */
public interface HandlerFactory {

	/**
	 * Get the handler instance that is capable to handle the modelClazz
	 * objects.
	 * 
	 * @param modelClazz
	 *            the model clazz to get the handler
	 * @return The {@link Handler} capable to handle the provided modelClazz or
	 *         null if no such handler exists
	 */
	public <T> Handler<T> getHandler(Class<T> modelClazz);
}
