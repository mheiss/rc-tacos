package at.redcross.tacos.datasetup;

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

    /** look for classes in this package **/
    private String packageName = "at.redcross.tacos.dbal.entity";

    // run as java-application
    public static void main(String[] args) throws Exception {
        DatabaseMirror mirror = new DatabaseMirror();
        mirror.mirror();
    }

    /** Mirror all known tables */
    public void mirror() throws Exception {
        logger.info("Starting mirroring");
        long start = System.currentTimeMillis();
        List<Class<?>> clazzList = EntityUtils.listEntityClasses(packageName);
        for (Class<?> clazz : clazzList) {
            mirrorPersistent(clazz);
        }
        long duration = System.currentTimeMillis() - start;
        logger.info("Mirroring '" + clazzList.size() + "' tables in " + duration + "ms");
    }

    /** Mirrors the given persistence table */
    private void mirrorPersistent(Class<?> persistent) {
        // try to determine the table name
        Table table = persistent.getAnnotation(Table.class);
        if (table == null) {
            return;
        }
        String tableName = table.name();
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            // metadata about the current table
            Query countQuery = manager.createQuery("select count(*) from " + tableName + "");
            Long count = (Long) countQuery.getSingleResult();
            logger.info("Processing table '" + tableName + "' ('" + count + "')");
            // query all entries
            TypedQuery<EntityImpl> listQuery = manager.createQuery("from " + tableName,
                    EntityImpl.class);
            List<EntityImpl> backupList = listQuery.getResultList();

            // delete all entries from the table
            manager.getTransaction().begin();
            Query deleteQuery = manager.createQuery("delete from " + tableName);
            deleteQuery.executeUpdate();
            manager.getTransaction().commit();

            // insert the entries again
            manager.getTransaction().begin();
            for (EntityImpl backupEntry : backupList) {
                manager.persist(backupEntry);
            }
            manager.getTransaction().commit();
        } catch (Exception ex) {
            logger.error("Failed to mirror persistent '" + persistent + "'", ex);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
}
