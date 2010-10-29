package at.redcross.tacos.dbal.helper;

import java.util.Calendar;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.query.InfoQueryParam;

public class InfoHelper {

    public static List<Info> listCurrent(EntityManager manager, InfoQueryParam param) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date >= info.displayStartDate AND :date <= info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (param.location != null) {
            builder.append(" and info.location.id = :locationId");
        }
        if (param.category != null){
        	builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }
        if (param.category != null) {
            query.setParameter("categoryId", param.category.getId());
        }
        return query.getResultList();
    }
    
    public static List<Info> listExpired(EntityManager manager, InfoQueryParam param) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date > info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (param.location != null) {
            builder.append(" and info.location.id = :locationId");
        }
        if (param.category != null) {
            builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }
        if (param.category != null) {
            query.setParameter("categoryId", param.category.getId());
        }
        return query.getResultList();
    }
    
    public static List<Info> listFuture(EntityManager manager, InfoQueryParam param) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date < info.displayStartDate");
        builder.append(" AND info.toDelete <> true ");
        if (param.location != null) {
            builder.append(" and info.location.id = :locationId");
        }
        if (param.category != null) {
            builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }
        if (param.category != null) {
            query.setParameter("categoryId", param.category.getId());
        }
        return query.getResultList();
    }
    
    public static List<Info> list(EntityManager manager) {
       
    	StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where info.toDelete <> true ");
       
        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);

        return query.getResultList();
    }

}
