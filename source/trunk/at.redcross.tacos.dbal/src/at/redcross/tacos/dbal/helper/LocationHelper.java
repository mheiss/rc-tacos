package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Location;

public class LocationHelper {

    public static List<Location> list(EntityManager manager) {
        StringBuilder builder = new StringBuilder();
        builder.append(" from Location location ");
        builder.append(" order by location.name asc ");
        return manager.createQuery(builder.toString(), Location.class).getResultList();
    }

    public static Location getByName(EntityManager manager, String name) {
        String hqlQuery = "from Location l where l.name = :name";
        TypedQuery<Location> query = manager.createQuery(hqlQuery, Location.class);
        query.setParameter("name", name);
        List<Location> locationList = query.getResultList();
        if (!locationList.isEmpty()) {
            return locationList.iterator().next();
        }
        return null;
    }

}
