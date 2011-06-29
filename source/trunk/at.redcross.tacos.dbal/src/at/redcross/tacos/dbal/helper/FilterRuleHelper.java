package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.entity.FilterRuleParam;

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

    public static void replaceParams(FilterRule... filterRules) {
        for (FilterRule rule : filterRules) {
            String description = rule.getDescriptionTemplate();
            for (FilterRuleParam param : rule.getParams()) {
                final String name = param.getName();
                final String value = param.getValue();

                // replace all place-holders
                String replaceValue = value;
                if (value == null || value.trim().isEmpty()) {
                    replaceValue = "(" + name + ")";
                }
                description = description.replace("$" + name + "$", replaceValue);
            }
            rule.setDescription(description);
        }
    }

}
