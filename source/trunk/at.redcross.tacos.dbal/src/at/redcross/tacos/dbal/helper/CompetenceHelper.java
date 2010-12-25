package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Competence;

public class CompetenceHelper {

    public static List<Competence> list(EntityManager manager) {
        return manager.createQuery("from Competence", Competence.class).getResultList();
    }

    public static List<Competence> getByName(EntityManager manager, String name) {
        String hqlQuery = "from Competence c where c.name = :name";
        TypedQuery<Competence> query = manager.createQuery(hqlQuery, Competence.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

}
