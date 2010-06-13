package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Assignment;

public class AssignmentHelper {

	public static List<Assignment> list(EntityManager manager) {
		return manager.createQuery("from Assignment", Assignment.class).getResultList();
	}

}
