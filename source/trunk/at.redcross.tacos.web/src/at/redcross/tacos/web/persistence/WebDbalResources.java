package at.redcross.tacos.web.persistence;

import java.util.Map;

import at.redcross.tacos.dbal.manager.DbalResources;

public class WebDbalResources extends DbalResources {

	// the one and only
	private static WebDbalResources instance;

	// the name of the persistence unit to be used
	private String persistenceUnit;

	private WebDbalResources() {
		// prevent instantiation
	}

	/**
	 * Sets the name of the persistence unit that will be used to create the
	 * manager factory.
	 * <p>
	 * Please note that this call must be done <b>before</tt> the factory is
	 * created otherwise some kind of unchecked exception will be thrown.
	 * </p>
	 * 
	 * @param persistenceUnit
	 */
	protected void setPersistenceUnit(String persistenceUnit) {
		if (lazyFactory.get() != null) {
			throw new IllegalStateException("Container factory already initialized");
		}
		this.persistenceUnit = persistenceUnit;
	}

	/**
	 * Creates or returns the shared {@code WebDbalResources} instance.
	 * 
	 * @return the shared instance.
	 */
	public synchronized static WebDbalResources getInstance() {
		if (instance == null) {
			instance = new WebDbalResources();
		}
		return instance;
	}

	@Override
	protected void initFactory(Map<String, String> map) {
		map.put("hibernate.ejb.interceptor", WebHistoryInterceptor.class.getName());
	}

	@Override
	protected String getPersistenceUnit() {
		return persistenceUnit;
	}
}