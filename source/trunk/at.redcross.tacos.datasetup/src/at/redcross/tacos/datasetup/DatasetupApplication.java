package at.redcross.tacos.datasetup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.datasetup.persistence.EntityManagerFactory;
import at.redcross.tacos.datasetup.stage0.GroupStage;
import at.redcross.tacos.datasetup.stage0.LocationStage;
import at.redcross.tacos.datasetup.stage0.SecuredResourceStage;
import at.redcross.tacos.datasetup.stage0.SystemUserStage;
import at.redcross.tacos.datasetup.stage1.AssignmentStage;
import at.redcross.tacos.datasetup.stage1.CarStage;
import at.redcross.tacos.datasetup.stage1.CategoryStage;
import at.redcross.tacos.datasetup.stage1.CompetenceStage;
import at.redcross.tacos.datasetup.stage1.InfoStage;
import at.redcross.tacos.datasetup.stage1.LinkStage;
import at.redcross.tacos.datasetup.stage1.NotificationStage;
import at.redcross.tacos.datasetup.stage1.RosterEntryStage;
import at.redcross.tacos.datasetup.stage1.ServiceTypeStage;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

/**
 * The {@code DatasetupApplication} is used to programmatically insert default
 * values into the database.
 */
public class DatasetupApplication {

	private final static Logger logger = LoggerFactory.getLogger(DatasetupApplication.class);

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
		logger.info("Performing cleanup");
		runCleanup();
		logger.info("Performing import");
		runImport();
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
			List<DatasetupStage> cleanStages = new ArrayList<DatasetupStage>(stages);
			Collections.reverse(cleanStages);
			manager.getTransaction().begin();
			for (DatasetupStage stage : cleanStages) {
				logger.debug("Cleaning stage '" + stage.getClass().getSimpleName() + "'");
				stage.performCleanup(manager);
			}
			manager.getTransaction().commit();
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/**
	 * Loops through all registered stages and imports the data.
	 */
	public void runImport() {
		for (DatasetupStage stage : stages) {
			EntityManager manager = null;
			try {
				manager = EntityManagerFactory.createEntityManager();
				logger.debug("Importing stage '" + stage.getClass().getSimpleName() + "'");
				stage.performImport(manager);
				EntityManagerHelper.commit(manager);
			} finally {
				manager = EntityManagerHelper.close(manager);
			}
		}
	}

	// run as java-application
	public static void main(String[] args) {
		DatasetupApplication app = new DatasetupApplication();
		app.registerStage(new LocationStage());
		app.registerStage(new GroupStage());
		app.registerStage(new SystemUserStage());
		app.registerStage(new SecuredResourceStage());
		app.registerStage(new AssignmentStage());
		app.registerStage(new CarStage());
		app.registerStage(new CategoryStage());
		app.registerStage(new CompetenceStage());
		app.registerStage(new InfoStage());
		app.registerStage(new LinkStage());
		app.registerStage(new NotificationStage());
		app.registerStage(new ServiceTypeStage());
		app.registerStage(new RosterEntryStage());
		app.execute();
	}
}
