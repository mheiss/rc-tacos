package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Info;

public class InfoStage implements DatasetupStage {

	@Override
	public void performCleanup(EntityManager manager) {
		TypedQuery<Info> query = manager.createQuery("from Info", Info.class);
		for (Info info : query.getResultList()) {
			manager.remove(info);
		}
	}

	@Override
	public void performImport(EntityManager manager) {
		// nothing to import
	}

}
