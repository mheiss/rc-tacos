package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;

public class SystemUserHelper {

    public static List<SystemUser> list(EntityManager manager) {
        TypedQuery<SystemUser> query = manager.createQuery(
                "from SystemUser user order by user.lastName desc", SystemUser.class);
        return query.getResultList();
    }

    public static SystemUser getByLogin(EntityManager manager, String loginName) {
        String hqlQuery = "from Login l where l.loginName = :loginName";
        TypedQuery<Login> query = manager.createQuery(hqlQuery, Login.class);
        query.setParameter("loginName", loginName);
        return query.getSingleResult().getSystemUser();
    }

    public static SystemUser getByPersonalNumber(EntityManager manager, String pNr) {
        String hqlQuery = "from SystemUser user where user.pnr = :pnr";
        TypedQuery<SystemUser> query = manager.createQuery(hqlQuery, SystemUser.class);
        query.setParameter("pnr", Integer.valueOf(pNr));
        List<SystemUser> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.iterator().next();
        }
        return null;
    }

    public static List<SystemUser> listByLocationName(EntityManager manager, String locationName, boolean locked) {
        StringBuilder builder = new StringBuilder();
        builder.append("from SystemUser user ");
        builder.append(" where user.login.locked = :locked ");
        if (locationName != null) {
            builder.append(" AND user.location.name = :locationName ");
        }
        builder.append(" order by user.lastName ");
        TypedQuery<SystemUser> query = manager.createQuery(builder.toString(), SystemUser.class);
        query.setParameter("locked", locked);
        if (locationName != null) {
            query.setParameter("locationName", locationName);
        }
        return query.getResultList();
    }

}
