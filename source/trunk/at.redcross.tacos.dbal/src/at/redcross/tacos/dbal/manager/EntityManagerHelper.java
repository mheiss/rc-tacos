package at.redcross.tacos.dbal.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * The {@code EntityContainerHelper} contains common helper class to work with
 * an {@linkplain EntityManager entity manager}.
 */
public class EntityManagerHelper {

	// the default persistence to use #see persistence.xml
	private static final String PERSISTENCE_UNIT_NAME = "tacosDevelopment";

	// the factory instance
	private static EntityManagerFactory factory = null;

	/**
	 * Creates and returns a new {@EntityManager entity manager}
	 * instance.
	 * 
	 * @return a entity manager instance
	 */
	public static EntityManager createEntityManager() {
		EntityManagerFactory factory = getFactory();
		return factory.createEntityManager();
	}

	/**
	 * Closes the provider entity manager instances.
	 * <p>
	 * The default use case when working with an entity manager is:
	 * 
	 * <pre>
	 * EntityManager manager = null;
	 * try {
	 * 	manager = EntityManagerHelper.createEntityManager();
	 * 	// do something
	 * }
	 * finally {
	 * 	manager = EntityManagerHelper.close(manager);
	 * }
	 * </pre>
	 * 
	 * @param manager
	 *            the manager to close
	 * @return always {@code null}
	 */
	public static EntityManager close(EntityManager manager) {
		if (manager == null) {
			return null;
		}
		try {
			manager.close();
		}
		catch (Exception ex) {
			// we cannot do anything
		}
		return null;
	}

	/**
	 * Opens a new transaction and commits all changed entity instances in the
	 * given container.
	 * 
	 * @param container
	 *            the container to commit.
	 */
	public static void commit(EntityManager manager) {
		EntityTransaction transaction = null;
		try {
			// Begin a new local transaction so that we can persist a new entity
			transaction = manager.getTransaction();
			transaction.begin();
			transaction.commit();
			transaction = null;
		}
		catch (Exception ex) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		}
		finally {
			EntityManagerHelper.close(manager);
		}
	}

	// creates a new factory or returns the initialized
	private static synchronized EntityManagerFactory getFactory() {
		if (factory != null) {
			return factory;
		}
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		return factory;
	}
}
