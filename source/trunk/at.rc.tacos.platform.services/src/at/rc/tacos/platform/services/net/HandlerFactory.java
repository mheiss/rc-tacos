package at.rc.tacos.platform.services.net;

/**
 * Handler factory interface.
 * 
 * @author Michael
 */
public interface HandlerFactory {

	/**
	 * Get the handler instance.
	 */
	public INetHandler<?> getHandler(String modelClazz);
}
