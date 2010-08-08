package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.SecuredResource;

public class SecuredResourceHelper {

	public static List<SecuredResource> list(EntityManager manager) {
		String hqlQuery = "from SecuredResource sr";
		TypedQuery<SecuredResource> query = manager.createQuery(hqlQuery, SecuredResource.class);
		return query.getResultList();
	}

	public static List<SecuredResource> getByName(EntityManager manager, String resource) {
		String hqlQuery = "from SecuredResource sr where sr.resource = :resource";
		TypedQuery<SecuredResource> query = manager.createQuery(hqlQuery, SecuredResource.class);
		query.setParameter("resource", resource);
		return query.getResultList();
	}

}
