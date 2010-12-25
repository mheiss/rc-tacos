package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.query.SystemUserQueryParam;

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

    public static List<SystemUser> list(EntityManager manager, SystemUserQueryParam param) {
        StringBuilder builder = new StringBuilder();
        builder.append(" select distinct user from SystemUser user ");
        builder.append(" left join user.groups as userGroup ");
        builder.append(" left join user.competences as competence ");
        builder.append(" where ");
        builder.append(" user.login.locked = :locked AND ");
        if (param.stateDelete & !param.stateNormal) {
            builder.append(" user.toDelete = :toDelete AND ");
        }
        if (param.stateNormal & !param.stateDelete) {
            builder.append(" user.toDelete = :toDelete AND ");
        }
        if (param.locationName != null && param.locationName != "*") {
            builder.append(" user.location.name = :locationName AND ");
        }
        if (param.group != null) {
            builder.append(" userGroup.id = :groupId AND ");
        }
        if (param.competence != null) {
            builder.append(" competence.id = :competenceId AND ");
        }
        if (param.userName != null) {
            builder.append(" ( user.firstName like :name ");
            builder.append(" OR ");
            builder.append(" user.lastName like :name ) ");
        }
        // ensure valid string
        String queryString = builder.toString().trim();
        if (queryString.endsWith("AND")) {
            queryString = queryString.substring(0, queryString.length() - 3);
        }
        if (queryString.endsWith("WHERE")) {
            queryString = queryString.substring(0, queryString.length() - 5);
        }
        queryString += " order by user.lastName ";
        System.out.println(queryString);
        // append the order clause
        TypedQuery<SystemUser> query = manager.createQuery(queryString, SystemUser.class);
        query.setParameter("locked", param.stateLocked);
        if (param.stateDelete & !param.stateNormal) {
            query.setParameter("toDelete", true);
        }
        if (param.stateNormal & !param.stateDelete) {
            query.setParameter("toDelete", false);
        }
        if (param.locationName != null && param.locationName != "*") {
            query.setParameter("locationName", param.locationName);
        }
        if (param.group != null) {
            query.setParameter("groupId", param.getGroup().getId());
        }
        if (param.competence != null) {
            query.setParameter("competenceId", param.getCompetence().getId());
        }
        if (param.userName != null) {
            String value = "%" + param.userName + "%";
            query.setParameter("name", value);
        }
        return query.getResultList();
    }
}
