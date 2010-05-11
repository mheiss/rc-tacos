package at.redcross.tacos.dbal.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * The {@code EntityManagerHelper} contains common helper class to work with
 * an {@linkplain EntityManager entity manager}.
 */
public class EntityManagerHelper {

    /**
     * Closes the provider entity manager instances.
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
            // we cannot do anything here
        }
        return null;
    }

    /**
     * Opens a new transaction and commits all changed entity instances in the
     * given container. The given container is not closed after the commit, so
     * it is the responsibility of the caller to close the manager if it is not
     * used any more.
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
    }
}
