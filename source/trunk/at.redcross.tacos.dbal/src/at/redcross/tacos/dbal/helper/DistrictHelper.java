package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.District;

public class DistrictHelper {

    public static List<District> list(EntityManager manager) {
        return manager.createQuery("from District", District.class).getResultList();
    }

    public static District getByName(EntityManager manager, String name) {
        String hqlQuery = "from District d where d.name = :name";
        TypedQuery<District> query = manager.createQuery(hqlQuery, District.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

}
