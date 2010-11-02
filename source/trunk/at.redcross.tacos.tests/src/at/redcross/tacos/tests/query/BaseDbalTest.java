package at.redcross.tacos.tests.query;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import at.redcross.tacos.datasetup.persistence.EntityManagerFactory;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

public abstract class BaseDbalTest {

    protected static EntityManager manager = null;

    @BeforeClass
    public static void setupClass() {
        manager = EntityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void tearDownClass() {
        manager = EntityManagerHelper.close(manager);
    }

}
