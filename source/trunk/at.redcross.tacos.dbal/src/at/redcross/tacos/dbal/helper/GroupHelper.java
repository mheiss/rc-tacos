package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Group;

public class GroupHelper {

	public static List<Group> getByName(EntityManager manager, String group) {
		String hqlQuery = "from Group g where g.name = :group";
		TypedQuery<Group> query = manager.createQuery(hqlQuery, Group.class);
		query.setParameter("group", group);
		return query.getResultList();
	}

}
