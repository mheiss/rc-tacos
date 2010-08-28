package at.redcross.tacos.dbal.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;

public class RosterEntryHelper {

    public static List<RosterEntry> listByDay(EntityManager manager, Location location, Date date) {
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from RosterEntry entry ");
        builder.append(" where :date >= entry.plannedStartDate AND :date <= entry.plannedEndDate");
        builder.append(" AND entry.toDelete <> true ");
        if (location != null) {
            builder.append(" and entry.location.id = :locationId");
        }

        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("date", start);
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }

    public static List<RosterEntry> listByWeek(EntityManager manager, Location location, Date date) {
        Calendar helper = Calendar.getInstance();
        helper.setTime(date);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, helper.get(Calendar.YEAR));
        calendar.set(Calendar.WEEK_OF_YEAR, helper.get(Calendar.WEEK_OF_YEAR));

        Date start = DateUtils.truncate(calendar.getTime(), Calendar.DAY_OF_MONTH);
        Date end = DateUtils.addDays(start, 6);
        

        StringBuilder builder = new StringBuilder();
        builder.append(" from RosterEntry entry ");
        builder.append(" where (entry.plannedStartDate between :start and :end ");
        builder.append(" OR entry.plannedEndDate between :start and :end) ");
        builder.append(" AND entry.toDelete <> true ");
        if (location != null) {
            builder.append(" and entry.location.id = :locationId");
        }
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
    
    public static List<RosterEntry> listByDayOrderByCar(EntityManager manager, Location location, Date date) {
        Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(" from RosterEntry entry ");
        builder.append(" where :date >= entry.plannedStartDate AND :date <= entry.plannedEndDate");
        builder.append(" AND entry.toDelete <> true ");
        if (location != null) {
            builder.append(" and entry.location.id = :locationId");
        }
        builder.append(" order by entry.car, entry.plannedStartTime, entry.systemUser.lastName");

        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("date", start);
        if (location != null) {
            query.setParameter("locationId", location.getId());
        }
        return query.getResultList();
    }
    
    public static List<RosterEntry> list(EntityManager manager) {
       
    	StringBuilder builder = new StringBuilder();
        builder.append(" from RosterEntry entry ");
        builder.append(" where entry.toDelete <> true ");
       
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);

        return query.getResultList();
    }

}
