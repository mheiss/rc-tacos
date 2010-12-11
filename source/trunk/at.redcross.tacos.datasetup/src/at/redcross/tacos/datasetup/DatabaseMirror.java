package at.redcross.tacos.datasetup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.datasetup.persistence.EntityManagerFactory;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.EntityUtils;

public class DatabaseMirror {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseMirror.class);

    // run as java-application
    public static void main(String[] args) throws Exception {
        DatabaseMirror mirror = new DatabaseMirror();
        mirror.mirror();
    }

    /** Mirror all known tables */
    public void mirror() throws Exception {
        List<EntityImpl> entityList = new ArrayList<EntityImpl>();
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            logger.info("Starting mirroring");
            long start = System.currentTimeMillis();

            // get a list of all entities from the database
            collectPersistent(manager, entityList);
            deleteEntityList(manager, entityList);

            manager = EntityManagerHelper.close(manager);
            manager = EntityManagerFactory.createEntityManager();

            // now insert the whole list again
            mirrorEntityList(manager, entityList);

            long duration = System.currentTimeMillis() - start;
            logger.info("Mirrored tables in " + duration + "ms");
        } catch (Exception ex) {
            logger.error("Failed to mirror persistents", ex);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }

    }

    /** Collects all entities from the given persistent */
    private void collectPersistent(EntityManager manager, List<EntityImpl> persistentList) throws Exception {
        for (Class<?> tableClazz : EntityUtils.listEntityClasses("at.redcross.tacos.dbal.entity")) {
            Table table = tableClazz.getAnnotation(Table.class);
            String tableName = table.name();
            try {
                Query countQuery = manager.createQuery("select count(*) from " + tableName + "");
                Long count = (Long) countQuery.getSingleResult();
                logger.info("Processing table '" + tableName + "' ('" + count + "')");
                // query all entries
                TypedQuery<EntityImpl> listQuery = manager.createQuery("from " + tableName,
                        EntityImpl.class);
                persistentList.addAll(listQuery.getResultList());
            } catch (Exception ex) {
                logger.error("Failed to collect persistent '" + tableName + "'", ex);
            }
        }
    }

    /** Deletes the given entities from the database */
    private void deleteEntityList(EntityManager manager, List<EntityImpl> persistentList) {
        try {
            manager.getTransaction().begin();
            for (EntityImpl backupEntry : persistentList) {
                manager.remove(backupEntry);
            }
            manager.getTransaction().commit();
        } catch (Exception ex) {
            logger.error("Failed to delete entity", ex);
        }
    }

    /** Inserts the given entities into the database */
    private void mirrorEntityList(EntityManager manager, List<EntityImpl> persistentList) {
        try {
            manager.getTransaction().begin();
            for (EntityImpl backupEntry : persistentList) {
                manager.merge(backupEntry);
            }
            manager.getTransaction().commit();
        } catch (Exception ex) {
            logger.error("Failed to mirror persistents", ex);
        }
    }
}
