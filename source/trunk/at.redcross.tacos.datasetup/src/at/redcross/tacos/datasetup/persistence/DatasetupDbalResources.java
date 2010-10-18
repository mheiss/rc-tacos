package at.redcross.tacos.datasetup.persistence;

import java.util.Map;

import at.redcross.tacos.dbal.manager.DbalResources;

public class DatasetupDbalResources extends DbalResources {

	// the system property to define an alternative persistence unit
	public final static String PERSISTENCE_UNIT = "at.redcross.tacos.persistence";

	// the one and only
	private static DatasetupDbalResources instance;

	private DatasetupDbalResources() {
		// prevent instantiation
	}

	/**
	 * Creates or returns the shared {@code DbalResources} instance.
	 * 
	 * @return the shared instance.
	 */
	public synchronized static DbalResources getInstance() {
		if (instance == null) {
			instance = new DatasetupDbalResources();
		}
		return instance;
	}

	@Override
	protected void initFactory(Map<String, String> map) {
		map.put("hibernate.ejb.interceptor", DatasetupHistoryInterceptor.class.getName());
	}

	@Override
	protected String getPersistenceUnit() {
		return System.getProperty(PERSISTENCE_UNIT, "");
	}

}
