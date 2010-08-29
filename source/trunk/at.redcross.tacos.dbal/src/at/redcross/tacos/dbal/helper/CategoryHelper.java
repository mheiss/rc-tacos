package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Category;

public class CategoryHelper {

    public static List<Category> list(EntityManager manager) {
        return manager.createQuery("from Category", Category.class).getResultList();
    }

    public static Category getByName(EntityManager manager, String name) {
        String hqlQuery = "from Category a where a.name = :name";
        TypedQuery<Category> query = manager.createQuery(hqlQuery, Category.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

}
