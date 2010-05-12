package at.redcross.tacos.datasetup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.datasetup.persistence.EntityManagerFactory;
import at.redcross.tacos.datasetup.stage1.AssignmentStage;
import at.redcross.tacos.datasetup.stage1.CompetenceStage;
import at.redcross.tacos.datasetup.stage1.DistrictStage;
import at.redcross.tacos.datasetup.stage1.LocationStage;
import at.redcross.tacos.datasetup.stage1.ServiceTypeStage;
import at.redcross.tacos.datasetup.stage1.SystemUserStage;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

/**
 * The {@code DatasetupApplication} is used to programmatically insert default
 * values into the database.
 */
public class DatasetupApplication {

    private final static Logger logger = LoggerFactory.getLogger(DatasetupApplication.class);

    // the system property whether or not a cleanup should be executed
    private final static String PROP_CLEANUP = "at.redcross.tacos.datasetup.cleanup";

    // the system property whether or not a import should be executed
    private final static String PROP_IMPORT = "at.redcross.tacos.datasetup.import";

    // the registered stages that will be executed
    private List<DatasetupStage> stages;

    public DatasetupApplication() {
        stages = new ArrayList<DatasetupStage>();
    }

    /**
     * Registers the given stage so that it will be included when performing the
     * import.
     * 
     * @param stage
     *            the stage to add
     */
    public void registerStage(DatasetupStage stage) {
        stages.add(stage);
    }

    /**
     * Loops through all registered stages and imports the data.
     */
    public void execute() {
        long start = System.currentTimeMillis();
        logger.info("Executing datasetup");
        if (System.getProperty(PROP_CLEANUP, "true").equals("true")) {
            logger.info("Performing cleanup");
            runCleanup();
        }
        if (System.getProperty(PROP_IMPORT, "true").equals("true")) {
            logger.info("Performing import");
            runImport();
        }
        long duration = System.currentTimeMillis() - start;
        logger.info("Datasetup successfully in '" + duration + "' ms");
    }

    /**
     * Loops through all registered stages and performs a cleanup
     */
    public void runCleanup() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (DatasetupStage stage : stages) {
                logger.debug("Cleaning stage '" + stage.getClass().getSimpleName() + "'");
                stage.performCleanup(manager);
            }
            EntityManagerHelper.commit(manager);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /**
     * Loops through all registered stages and imports the data.
     */
    public void runImport() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (DatasetupStage stage : stages) {
                logger.debug("Importing stage '" + stage.getClass().getSimpleName() + "'");
                stage.performImport(manager);
            }
            EntityManagerHelper.commit(manager);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // run as java-application
    public static void main(String[] args) {
        DatasetupApplication app = new DatasetupApplication();
        app.registerStage(new DistrictStage());
        app.registerStage(new LocationStage());
        app.registerStage(new ServiceTypeStage());
        app.registerStage(new AssignmentStage());
        app.registerStage(new CompetenceStage());
        app.registerStage(new SystemUserStage());
        app.execute();
    }
}
