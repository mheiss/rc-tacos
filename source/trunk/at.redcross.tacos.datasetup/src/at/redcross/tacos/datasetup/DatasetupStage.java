package at.redcross.tacos.datasetup;

import javax.persistence.EntityManager;

/**
 * A stage is used to import data programmatically into the database. A given
 * stage should only import data of a specific entity type.
 */
public interface DatasetupStage {

    /**
     * Creates the required entity.
     * 
     * @param manager
     *            the manager instance to persist the entities.
     */
    public void performImport(EntityManager manager);

    /**
     * Called before the import of the given stage.
     */
    public void performCleanup(EntityManager manager);

}
