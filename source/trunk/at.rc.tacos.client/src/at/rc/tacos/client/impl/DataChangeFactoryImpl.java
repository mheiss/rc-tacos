package at.rc.tacos.client.impl;

import java.util.Collection;

import org.apache.commons.collections.map.MultiValueMap;

import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;

/**
 * Factory for {@link DataChangeListener} instances.
 * <p>
 * The listeners are stored in a {@link MultiValueMap}. The access to the map is
 * syncronized so it can be used by multiple threads concurrently.
 * </p>
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class DataChangeFactoryImpl implements DataChangeListenerFactory {

	private MultiValueMap listenerMap = new MultiValueMap();

	@Override
	public Collection<DataChangeListener<Object>> getListeners(Class<?> dataClazz) {
		synchronized (listenerMap) {
			return listenerMap.getCollection(dataClazz);
		}
	}

	@Override
	public void registerListener(DataChangeListener<?> listener, Class<?> dataClazz) {
		synchronized (listenerMap) {
			listenerMap.put(dataClazz, listener);
		}
	}

	@Override
	public void removeListener(DataChangeListener<?> listener, Class<?> dataClazz) {
		synchronized (listenerMap) {
			listenerMap.remove(dataClazz, listener);
		}
	}
}
