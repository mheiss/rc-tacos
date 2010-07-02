package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Assignment;

public class AssignmentHelper {

    public static List<Assignment> list(EntityManager manager) {
        return manager.createQuery("from Assignment", Assignment.class).getResultList();
    }

    public static Assignment getByName(EntityManager manager, String name) {
        String hqlQuery = "from Assignment a where a.name = :name";
        TypedQuery<Assignment> query = manager.createQuery(hqlQuery, Assignment.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

}
