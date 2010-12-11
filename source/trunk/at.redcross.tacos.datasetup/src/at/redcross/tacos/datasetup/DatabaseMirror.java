package at.redcross.tacos.datasetup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.datasetup.persistence.EntityManagerFactory;
import at.redcross.tacos.dbal.entity.Assignment;
import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Link;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.Notification;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.SecuredResource;
import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

public class DatabaseMirror {

	private final static Logger logger = LoggerFactory.getLogger(DatabaseMirror.class);

	// run as java-application
	public static void main(String[] args) throws Exception {
		DatabaseMirror mirror = new DatabaseMirror();
		mirror.mirror();
	}

	/** Mirror all known tables */
	public void mirror() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			logger.info("Starting mirroring");
			long start = System.currentTimeMillis();

			// get a list of all entities from the database
			List<EntityImpl> entityList = queryEntities(manager);
			logger.info("Collected '" + entityList.size() + "' entities from the database");

			// remove all of them and insert again
			deleteEntityList(manager);
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
	private List<EntityImpl> queryEntities(EntityManager manager) throws Exception {
		List<EntityImpl> entityList = new ArrayList<EntityImpl>();
		for (Class<?> tableClazz : getPersistentClasses()) {
			String tableName = tableClazz.getSimpleName();
			try {
				Query countQuery = manager.createQuery("select count(*) from " + "Group" + "");
				Long count = (Long) countQuery.getSingleResult();
				logger.info("Processing table '" + tableName + "' ('" + count + "')");
				// query all entries
				TypedQuery<EntityImpl> listQuery = manager.createQuery("from " + tableName,
						EntityImpl.class);
				entityList.addAll(listQuery.getResultList());
			} catch (Exception ex) {
				logger.error("Failed to collect persistent '" + tableName + "'", ex);
			}
		}
		return entityList;
	}

	/** Deletes the given entities from the database */
	private void deleteEntityList(EntityManager manager) {
		try {
			manager.getTransaction().begin();
			List<Class<?>> clazzes = getPersistentClasses();
			Collections.reverse(clazzes);
			for (Class<?> clazz : clazzes) {
				String hqlQuery = "delete from " + clazz.getSimpleName();
				manager.createQuery(hqlQuery).executeUpdate();
			}
			manager.getTransaction().commit();
		} catch (Exception ex) {
			logger.error("Failed to delete entity", ex);
		}
	}

	/** Inserts the given entities into the database */
	private void mirrorEntityList(EntityManager manager, List<EntityImpl> persistentList) {
		/** Process every entry and add to the database */
		try {
			manager.getTransaction().begin();
			Iterator<EntityImpl> mergeIter = persistentList.iterator();
			while (mergeIter.hasNext()) {
				EntityImpl mergeEntry = mergeIter.next();

				// create and commit the new record
				Object oid = mergeEntry.getOid();
				EntityImpl source = manager.merge(mergeEntry);

				logger.info("Inserted record '" + source.getDisplayString() + "' to '"
						+ source.getClass().getSimpleName() + "'");

				// remove from the list
				mergeIter.remove();

				// replace all occurrences of the old key with the new one
				for (EntityImpl replaceEntry : persistentList) {
					processEntity(replaceEntry, source, oid);
				}
			}
			manager.getTransaction().commit();
		} catch (Exception ex) {
			logger.error("Failed to mirror persistents", ex);
		}
	}

	/** Processes the given object and changes the entity reference */
	@SuppressWarnings("unchecked")
	private void processEntity(EntityImpl target, EntityImpl source, Object oid) throws Exception {
		Map<String, Object> properties = BeanUtils.describe(target);
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String property = entry.getKey();
			Object value = PropertyUtils.getProperty(target, property);
			if (value instanceof Collection<?>) {
				Collection<?> propertyCollection = (Collection<?>) value;
				for (Object propertyEntry : propertyCollection) {
					processObject(propertyEntry, source, oid);
				}
			}
			if (value instanceof EntityImpl) {
				processObject(value, source, oid);
			}
		}
	}

	/** Checks and processes the given object */
	private void processObject(Object target, Object source, Object oid) throws Exception {
		// class must match
		if (target.getClass() != source.getClass()) {
			return;
		}
		// target must be an entity
		if (!(target instanceof EntityImpl)) {
			return;
		}
		// target must have the same primary key
		EntityImpl targetImpl = (EntityImpl) target;
		EntityImpl sourceImpl = (EntityImpl) source;
		if (targetImpl.getOid() != oid) {
			return;
		}
		Field field = targetImpl.getClass().getDeclaredField("id");
		field.setAccessible(true);
		field.setLong(target, (Long) sourceImpl.getOid());
	}

	/** Returns a list of all persistent classes to process */
	private List<Class<?>> getPersistentClasses() {
		// note that the order is important
		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		
		// no dependency
		clazzes.add(Link.class);
		clazzes.add(Notification.class);
		clazzes.add(SecuredResource.class);
		
		// depends on the roster entry
		clazzes.add(RosterEntry.class);
		clazzes.add(Assignment.class);
		clazzes.add(Competence.class);
		clazzes.add(ServiceType.class);
		
		// depends on the system user
		clazzes.add(SystemUser.class);
		clazzes.add(Login.class);
		clazzes.add(Group.class);

		// depends on location
		clazzes.add(Car.class);
		clazzes.add(Info.class);
		clazzes.add(Category.class);
		
		// the last one
		clazzes.add(Location.class);

		return clazzes;
	}

}
