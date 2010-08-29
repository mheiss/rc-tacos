package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.SecuredResource;

public class SecuredResourceStage implements DatasetupStage {

	@Override
	public void performCleanup(EntityManager manager) {
		TypedQuery<SecuredResource> query = manager.createQuery("from SecuredResource",
				SecuredResource.class);
		for (SecuredResource resource : query.getResultList()) {
			manager.remove(resource);
		}
	}

	@Override
	public void performImport(EntityManager manager) {
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/login");
			resource.setAccess("permitAll");
			manager.persist(resource);
		}
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/logout");
			resource.setAccess("isAuthenticated()");
			manager.persist(resource);
		}
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/error");
			resource.setAccess("isAuthenticated()");
			manager.persist(resource);
		}
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/accessDenied");
			resource.setAccess("isAuthenticated()");
			manager.persist(resource);
		}
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/**");
			resource.setAccess("isAuthenticated()");
			manager.persist(resource);
		}
		{
			SecuredResource resource = new SecuredResource();
			resource.setResource("/admin/**");
			resource.setAccess("hasRole('ROLE_ADMIN')");
			manager.persist(resource);
		}
	}
}
