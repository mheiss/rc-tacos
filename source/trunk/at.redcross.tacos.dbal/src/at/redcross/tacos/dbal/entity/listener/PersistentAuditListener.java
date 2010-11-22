package at.redcross.tacos.dbal.entity.listener;

import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.dbal.manager.DbalResources;

public class PersistentAuditListener implements RevisionListener {

    private final static Logger logger = LoggerFactory.getLogger(PersistentAuditListener.class);

    @Override
    public void newRevision(Object revisionObject) {
        // we cannot do anything here to get the class name
        if (System.getProperty(DbalResources.AUDIT_CLASSNAME, "").isEmpty()) {
            return;
        }
        String className = System.getProperty(DbalResources.AUDIT_CLASSNAME);
        // here we use a little hack as the audit class is not configurable
        // through properties
        try {
            Class<?> auditListenerClass = Class.forName(className);
            Object newInstance = auditListenerClass.newInstance();
            if (!(newInstance instanceof RevisionListener)) {
                logger.error("Wrong type hierarchy");
                return;
            }
            RevisionListener auditListener = (RevisionListener) newInstance;
            auditListener.newRevision(revisionObject);
        } catch (Exception initError) {
            logger.error("Initialisation failed", initError);
        } catch (LinkageError linkError) {
            logger.error("Linkage failed", linkError);
        }
    }
}
