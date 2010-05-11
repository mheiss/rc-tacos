package at.redcross.tacos.datasetup.persistence;

import javax.persistence.EntityManager;

/**
 * The {@code EntityManagerFactory} provides convenient methods to create and
 * initialize {@linkplain EntityManager entity manager} instances.
 */
public class EntityManagerFactory {

    /**
     * Creates and returns a new {@linkplain EntityManager entity manager}
     * instance.
     * <p>
     * The default use case when working with an entity manager is:
     * </p>
     * 
     * <pre>
     * EntityManager manager = null;
     * try {
     *     manager = EntityManagerFactory.createEntityManager();
     *     // do something
     * }
     * finally {
     *     manager = EntityManagerHelper.close(manager);
     * }
     * </pre>
     * 
     * @return a entity manager instance
     */
    public static EntityManager createEntityManager() {
        return DatasetupDbalResources.getInstance().getFactory().createEntityManager();
    }

}
