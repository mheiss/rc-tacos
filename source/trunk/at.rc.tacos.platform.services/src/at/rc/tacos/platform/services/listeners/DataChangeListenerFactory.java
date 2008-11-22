package at.rc.tacos.platform.services.listeners;

/**
 * {@link DataChangeListener} factory interface
 * 
 * @author Michael
 */
public interface DataChangeListenerFactory {

	/**
	 * Returns the listener instance. Each listener instance is responsible for
	 * a single data clazz.
	 * <p>
	 * An example for such a data class would be
	 * {@link at.rc.tacos.platform.model.RosterEntry}.
	 * </p>
	 * 
	 * @param dataClazz
	 *            the clazz of the data to create
	 * @return The {@link DataChangeListener} matching the provided clazz
	 */
	public <T> DataChangeListener<T> getListener(Class<?> dataClazz);

}
