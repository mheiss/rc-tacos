package at.redcross.tacos.dbal.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.DataState;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.query.RosterQueryParam;

public class RosterEntryHelper {

    public static List<RosterEntry> listByDay(EntityManager manager, RosterQueryParam param) {
        param.startDate = param.endDate = DateUtils
                .truncate(param.startDate, Calendar.DAY_OF_MONTH);
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
        // the HQL query
        StringBuilder builder = new StringBuilder();
        builder.append(" FROM RosterEntry entry ");
        builder.append(" WHERE ");
        builder.append(" ( ");
        builder.append(" :start BETWEEN entry.plannedStartDateTime AND entry.plannedEndDateTime ");
        builder.append("  OR  ");
        builder.append(" :end BETWEEN entry.plannedStartDateTime AND entry.plannedEndDateTime ");
        builder.append(" ) ");
        builder.append("  OR  ");
        builder.append(" ( ");
        builder.append(" day(entry.plannedStartDateTime) = :startDay ");
        builder.append(" AND ");
        builder.append(" month(entry.plannedStartDateTime) = :startMonth ");
        builder.append(" AND ");
        builder.append(" year(entry.plannedStartDateTime) = :startYear ");
        builder.append(" ) ");

        builder.append(" AND entry.state IN (:states)");
        if (param.location != null) {
            builder.append(" AND entry.location.id = :locationId");
        }

        // set the parameters for the date range
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("start", param.startDate, TemporalType.TIMESTAMP);
        query.setParameter("end", param.endDate, TemporalType.TIMESTAMP);

        // explicit matching for a given day
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(param.startDate);
        query.setParameter("startDay", startCalendar.get(Calendar.DAY_OF_MONTH));
        query.setParameter("startMonth", startCalendar.get(Calendar.MONTH) + 1);
        query.setParameter("startYear", startCalendar.get(Calendar.YEAR));

        // filter by location
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }

        // filter by state
        Collection<DataState> states = new ArrayList<DataState>();
        if (param.stateDelete) {
            states.add(DataState.DELETE);
        }
        if (param.stateNormal) {
            states.add(DataState.NORMAL);
        }
        if (!param.stateDelete && !param.stateNormal) {
            states.add(DataState.NONE);
        }

        query.setParameter("states", states);
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
