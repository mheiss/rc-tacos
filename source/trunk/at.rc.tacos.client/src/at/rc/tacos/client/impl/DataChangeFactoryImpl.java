package at.rc.tacos.client.impl;

import java.util.Map;

import at.rc.tacos.platform.services.listeners.DataChangeListener;
import at.rc.tacos.platform.services.listeners.DataChangeListenerFactory;

/**
 * Factory for {@link DataChangeListener} instances
 * 
 * @author Michael
 */
public class DataChangeFactoryImpl implements DataChangeListenerFactory {

	
	@Override
	public <T> DataChangeListener<T> getListener(Class<?> dataClazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
