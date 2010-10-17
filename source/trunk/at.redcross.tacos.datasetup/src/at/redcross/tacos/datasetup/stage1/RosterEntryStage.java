package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.RosterEntry;

public class RosterEntryStage implements DatasetupStage {

	@Override
	public void performCleanup(EntityManager manager) {
		TypedQuery<RosterEntry> query = manager.createQuery("from RosterEntry", RosterEntry.class);
		for (RosterEntry rosterEntry : query.getResultList()) {
			manager.remove(rosterEntry);
		}
	}

	@Override
	public void performImport(EntityManager manager) {
		// nothing to import
	}

}
