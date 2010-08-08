package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Group;

public class GroupStage implements DatasetupStage {

	@Override
	public void performCleanup(EntityManager manager) {
		TypedQuery<Group> query = manager.createQuery("from Group", Group.class);
		for (Group group : query.getResultList()) {
			manager.remove(group);
		}
	}

	@Override
	public void performImport(EntityManager manager) {
		{
			Group group = new Group();
			group.setName("ROLE_ANONYMOUS");
			manager.persist(group);
		}
		{
			Group group = new Group();
			group.setName("ROLE_ADMIN");
			manager.persist(group);
		}
	}
}
