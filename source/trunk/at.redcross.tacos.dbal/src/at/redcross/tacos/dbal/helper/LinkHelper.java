package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Link;

public class LinkHelper {

	public static List<Link> list(EntityManager manager) {
		return manager.createQuery("from Link", Link.class)
				.getResultList();
	}

	public static Link getByName(EntityManager manager, String name) {
		String hqlQuery = "from Link c where c.name = :name";
		TypedQuery<Link> query = manager.createQuery(hqlQuery,
				Link.class);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

}
