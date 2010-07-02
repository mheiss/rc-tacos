package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Location;

public class LocationHelper {

    public static List<Location> list(EntityManager manager) {
        return manager.createQuery("from Location", Location.class).getResultList();
    }

    public static Location getByName(EntityManager manager, String name) {
        String hqlQuery = "from Location l where l.name = :name";
        TypedQuery<Location> query = manager.createQuery(hqlQuery, Location.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

}
