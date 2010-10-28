package at.redcross.tacos.dbal.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;

public class InfoHelper {

    public static List<Info> listCurrent(EntityManager manager, Location location) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date >= info.displayStartDate AND :date <= info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (location != null) {
            builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
    
    public static List<Info> listExpired(EntityManager manager, Location location) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date > info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (location != null) {
            builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
    
    public static List<Info> listFuture(EntityManager manager, Location location) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date < info.displayStartDate");
        builder.append(" AND info.toDelete <> true ");
        if (location != null) {
            builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (location != null) {
            query.setParameter("locationId", location.getId());
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

	public static List<Info> listExpired(EntityManager manager,Category category) {
		Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date > info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        return query.getResultList();
	}
	
	public static List<Info> listFuture(EntityManager manager, Category category) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date < info.displayStartDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        return query.getResultList();
    }
	
	public static List<Info> listCurrent(EntityManager manager, Category category) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date >= info.displayStartDate AND :date <= info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        return query.getResultList();
    }
	
	public static List<Info> listCurrent(EntityManager manager, Location location, Category category) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date >= info.displayStartDate AND :date <= info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }
        
        if (location != null) {
        	builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
	
	public static List<Info> listExpired(EntityManager manager, Location location, Category category) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date > info.displayEndDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }
        
        if (location != null) {
        	builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
	
public static List<Info> listFuture(EntityManager manager, Location location, Category category) {
    	
    	Date date = Calendar.getInstance().getTime();
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from Info info ");
        builder.append(" where :date < info.displayStartDate");
        builder.append(" AND info.toDelete <> true ");
        if (category != null) {
            builder.append(" and info.category.id = :categoryId");
        }
        
        if (location != null) {
        	builder.append(" and info.location.id = :locationId");
        }

        TypedQuery<Info> query = manager.createQuery(builder.toString(), Info.class);
        query.setParameter("date", start);
        if (category != null) {
            query.setParameter("categoryId", category.getId());
        }
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }

}
