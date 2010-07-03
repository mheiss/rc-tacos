package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;

public class SystemUserHelper {

    public static List<SystemUser> list(EntityManager manager) {
        return manager.createQuery("from SystemUser", SystemUser.class).getResultList();
    }

    public static SystemUser getByLogin(EntityManager manager, String alias) {
        String hqlQuery = "from Login l where l.alias = :alias";
        TypedQuery<Login> query = manager.createQuery(hqlQuery, Login.class);
        query.setParameter("alias", alias);
        return query.getSingleResult().getSystemUser();
    }

    public static List<SystemUser> listByLocationName(EntityManager manager, String locationName) {
        StringBuilder builder = new StringBuilder();
        builder.append("from SystemUser user ");
        if (locationName != null) {
            builder.append("where user.location.name = :locationName");
        }
        TypedQuery<SystemUser> query = manager.createQuery(builder.toString(), SystemUser.class);
        if (locationName != null) {
            query.setParameter("locationName", locationName);
        }
        return query.getResultList();
    }

}
