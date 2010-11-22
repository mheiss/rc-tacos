package at.redcross.tacos.datasetup.persistence;

import at.redcross.tacos.dbal.entity.RevisionInfo;
import at.redcross.tacos.dbal.entity.listener.PersistentAuditListener;

public class DatasetupAuditListener extends PersistentAuditListener {

    @Override
    public void newRevision(Object revisionObject) {
        RevisionInfo revisionEntity = (RevisionInfo) revisionObject;
        revisionEntity.setUsername("(DataImport)");
    }

}
