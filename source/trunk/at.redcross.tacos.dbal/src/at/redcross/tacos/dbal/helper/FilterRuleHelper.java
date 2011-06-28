package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.FilterRule;

public class FilterRuleHelper {

    public static List<FilterRule> list(EntityManager manager) {
        return manager.createQuery("from FilterRule", FilterRule.class).getResultList();
    }

    public static FilterRule getByName(EntityManager manager, String name) {
        String hqlQuery = "from FilterRule rule where rule.name = :name";
        TypedQuery<FilterRule> query = manager.createQuery(hqlQuery, FilterRule.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

}
