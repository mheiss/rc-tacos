package at.rc.tacos.platform.services.listeners;

import java.util.Collection;

/**
 * The <code>DataChangeListenerFactory</code> provides methods to register and
 * query {@link DataChangeListener} implementations.
 * 
 * @author Michael
 */
public interface DataChangeListenerFactory {

	/**
	 * Returns all the listeners who are interested on changes of the given
	 * <code>dataClazz</code>. This method will return an empty list if no
	 * listener are registered for this clazz.
	 * 
	 * @param dataClazz
	 *            the class of the model to query the listeners
	 * @return The {@link DataChangeListener} matching the provided clazz
	 */
	public Collection<DataChangeListener<Object>> getListeners(Class<?> dataClazz);

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * uppon changes of the given <code>dataClazz</code>.
	 * 
	 * @param listener
	 *            the <code>DataChangeListener</code> to register
	 * @param dataClazz
	 *            the clazz to register the listener for notifications
	 */
	public void registerListener(DataChangeListener<Object> listener, Class<?> dataClazz);

	/**
	 * Removes the listener from the collection of listeners. The
	 * <code>DataChangeListener</code> will not be informed uppon changes of the
	 * <code>dataClazz</code>.
	 * 
	 * @param listener
	 *            the <code>DataChangeListener</code> instance to remove
	 * @param dataClazz
	 *            the clazz to remove the listener
	 */
	public void removeListener(DataChangeListener<Object> listener, Class<?> dataClazz);

}
