package at.rc.tacos.platform.services.listeners;

/**
 * Handles all data change change events fired from the network plugin.
 * <p>
 * This events will be typically used to update the UI when the underlying data
 * has changed. You can register a listener with a
 * {@link DataChangeListenerFactory} to get notified uppon changes
 * </p>
 * 
 * @author Michael
 */
public interface DataChangeListener<T> {

	public void dataChanged(T oldData, T newData);

}
