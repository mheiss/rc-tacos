package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.SecuredAction;

public class SecuredActionHelper {

	public static List<SecuredAction> list(EntityManager manager) {
		String hqlQuery = "from SecuredAction sa";
		TypedQuery<SecuredAction> query = manager.createQuery(hqlQuery, SecuredAction.class);
		return query.getResultList();
	}

	public static List<SecuredAction> getByName(EntityManager manager, String actionExpression) {
		String hqlQuery = "from SecuredAction sa where sa.actionExpression = :actionExpression";
		TypedQuery<SecuredAction> query = manager.createQuery(hqlQuery, SecuredAction.class);
		query.setParameter("actionExpression", actionExpression);
		return query.getResultList();
	}

}
