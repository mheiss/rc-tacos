package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.ServiceType;

public class ServiceTypeHelper {

    public static List<ServiceType> list(EntityManager manager) {
        return manager.createQuery("from ServiceType", ServiceType.class).getResultList();
    }

    public static ServiceType getByName(EntityManager manager, String name) {
        String hqlQuery = "from ServiceType st where st.name = :name";
        TypedQuery<ServiceType> query = manager.createQuery(hqlQuery, ServiceType.class);
        query.setParameter("name", name);
        List<ServiceType> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.iterator().next();
        }
        return null;
    }

}
