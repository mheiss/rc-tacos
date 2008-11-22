package at.rc.tacos.platform.services.listeners;

/**
 * Handles all data change change events fired from the network plugin.
 * <p>
 * This events will be typically used to update the UI when the underlying data
 * has changed.
 * </p>
 * 
 * @author Michael
 */
public interface DataChangeListener<T> {

	public void dataChanged(T oldData, T newData);

}
