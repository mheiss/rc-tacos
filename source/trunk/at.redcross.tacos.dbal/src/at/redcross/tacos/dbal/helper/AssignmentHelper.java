package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Assignment;

public class AssignmentHelper {

    public static List<Assignment> list(EntityManager manager) {
        StringBuilder builder = new StringBuilder();
        builder.append(" from Assignment a ");
        builder.append(" order by a.name asc ");
        return manager.createQuery(builder.toString(), Assignment.class).getResultList();
    }

    public static Assignment getByName(EntityManager manager, String name) {
        String hqlQuery = "from Assignment a where a.name = :name";
        TypedQuery<Assignment> query = manager.createQuery(hqlQuery, Assignment.class);
        query.setParameter("name", name);
        List<Assignment> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.iterator().next();
        }
        return null;
    }

}
