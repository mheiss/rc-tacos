package at.redcross.tacos.dbal.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code DbalResources} manages the access to the {@code
 * EntityManagerFactory}. The factory is lazily created upon the first request.
 * All subsequent calls will get the same factory instance.
 */
public abstract class DbalResources {

	private final static Logger logger = LoggerFactory.getLogger(DbalResources.class);

	// default persistence unit if nothing is provided
	public final static String DEFAULT_PERSISTENCE_UNIT = "tacosDevelopment";

	// the cached factory instance
	protected final AtomicReference<EntityManagerFactory> lazyFactory = new AtomicReference<EntityManagerFactory>();

	/**
	 * Creates and returns a new {@code EntityManagerFactory} using the given
	 * properties to initialize.
	 * 
	 * @param map
	 *            the map to initialize the factory
	 * @return the initialized factory
	 */
	public synchronized EntityManagerFactory getFactory() {
		final EntityManagerFactory existingFactory = lazyFactory.get();
		if (existingFactory != null) {
			return existingFactory;
		}
		final EntityManagerFactory newFactory = createFactory();
		if (lazyFactory.compareAndSet(null, newFactory)) {
			return newFactory;
		}
		return lazyFactory.get();
	}

	/**
	 * Callback method to initialize the factory with custom properties.
	 * Subclasses must override and define custom properties.
	 * 
	 * @param map
	 *            the map to add the custom key/value properties
	 */
	protected abstract void initFactory(Map<String, String> map);

	/**
	 * Returns the name of the persistence-unit that should be used to create
	 * the factory.
	 * 
	 * @return the name of the persistence unit
	 */
	protected abstract String getPersistenceUnit();

	// ---------------------------------
	// Private API
	// ---------------------------------

	// creates and returns a factory instance
	private EntityManagerFactory createFactory() {
		Map<String, String> map = new HashMap<String, String>();
		initFactory(map);
		String persistenceUnit = getPersistenceUnit();
		if (persistenceUnit == null || persistenceUnit.isEmpty()) {
			persistenceUnit = DEFAULT_PERSISTENCE_UNIT;
		}
		logger.info("Using persistence unit '" + persistenceUnit + "'");
		return Persistence.createEntityManagerFactory(persistenceUnit, map);
	}

}
