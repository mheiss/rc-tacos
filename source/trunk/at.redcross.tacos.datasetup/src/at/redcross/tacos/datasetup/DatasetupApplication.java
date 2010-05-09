package at.redcross.tacos.datasetup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.datasetup.stage1.ServiceTypeStage;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

/**
 * The {@code DatasetupApplication} is used to programmatically insert default
 * values into the database.
 */
public class DatasetupApplication {

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
    public void importAll() {
        EntityManager manager = null;
        try {
            manager = EntityManagerHelper.createEntityManager();
            for (DatasetupStage stage : stages) {
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
        app.registerStage(new ServiceTypeStage());
        app.importAll();
    }
}
