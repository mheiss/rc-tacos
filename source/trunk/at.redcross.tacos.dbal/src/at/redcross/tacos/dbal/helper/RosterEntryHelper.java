package at.redcross.tacos.dbal.helper;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.query.RosterQueryParam;

public class RosterEntryHelper {

    public static List<RosterEntry> listByDay(EntityManager manager, RosterQueryParam param) {
        param.startDate = DateUtils.truncate(param.startDate, Calendar.DAY_OF_MONTH);
        param.endDate = DateUtils.truncate(param.startDate, Calendar.DAY_OF_MONTH);
        return list(manager, param);
    }

    public static List<RosterEntry> listByWeek(EntityManager manager, RosterQueryParam param) {
        Calendar helper = Calendar.getInstance();
        helper.setTime(param.startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, helper.get(Calendar.YEAR));
        calendar.set(Calendar.WEEK_OF_YEAR, helper.get(Calendar.WEEK_OF_YEAR));

        param.startDate = DateUtils.truncate(calendar.getTime(), Calendar.DAY_OF_MONTH);
        param.endDate = DateUtils.addDays(param.startDate, 6);

        return list(manager, param);
    }

    public static List<RosterEntry> list(EntityManager manager, RosterQueryParam param) {
        param.startDate = DateUtils.truncate(param.startDate, Calendar.DAY_OF_MONTH);
        param.endDate = DateUtils.truncate(param.endDate, Calendar.DAY_OF_MONTH);
        
        StringBuilder builder = new StringBuilder();
        builder.append(" FROM RosterEntry entry ");
        builder.append(" WHERE (entry.plannedStartDate BETWEEN :start AND :end ");
        builder.append(" OR entry.plannedEndDate BETWEEN :start AND :end) ");
        builder.append(" AND entry.toDelete = :deleteFlag ");
        if (param.location != null) {
            builder.append(" AND entry.location.id = :locationId");
        }
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("start", param.startDate);
        query.setParameter("end", param.endDate);
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }
        query.setParameter("deleteFlag", param.toDelete);
        return query.getResultList();
    }

    public static boolean isSameEntity(RosterEntry lhs, RosterEntry rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        // compare attributes
        if (lhs.getSystemUser().getId() != rhs.getSystemUser().getId()) {
            return false;
        }
        if (lhs.getLocation().getId() != rhs.getLocation().getId()) {
            return false;
        }
        if (lhs.getServiceType().getId() != rhs.getServiceType().getId()) {
            return false;
        }
        if (lhs.getAssignment().getId() != rhs.getAssignment().getId()) {
            return false;
        }
        if (lhs.getPlannedStartDateTime().compareTo(rhs.getPlannedStartDateTime()) != 0) {
            return false;
        }
        if (lhs.getPlannedEndDateTime().compareTo(rhs.getPlannedEndDateTime()) != 0) {
            return false;
        }
        // seems to be equal
        return true;
    }

}
