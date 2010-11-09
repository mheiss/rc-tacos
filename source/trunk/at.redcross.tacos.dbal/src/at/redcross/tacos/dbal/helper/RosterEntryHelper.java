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

	/** Returns a list of entries for the given day */
	public static List<RosterEntry> listByDay(EntityManager manager, RosterQueryParam param) {
		param.startDate = DateUtils.truncate(param.startDate, Calendar.DAY_OF_MONTH);
		param.endDate = DateUtils.addDays(param.startDate, 1);
		return list(manager, param);
	}

	/** Returns a list of entries in the given week of the year */
	public static List<RosterEntry> listByWeek(EntityManager manager, RosterQueryParam param) {
		// determine the current week
		Calendar paramStartCal = Calendar.getInstance();
		paramStartCal.setTime(param.startDate);
		int startYear = paramStartCal.get(Calendar.YEAR);
		int startWeek = paramStartCal.get(Calendar.WEEK_OF_YEAR);

		// setup the starting calendar
		paramStartCal.clear();
		paramStartCal.set(Calendar.YEAR, startYear);
		paramStartCal.set(Calendar.WEEK_OF_MONTH, startWeek);

		// setup the ending calendar
		Calendar paramEndCal = Calendar.getInstance();
		paramEndCal.clear();
		paramEndCal.set(Calendar.YEAR, startYear);
		paramEndCal.set(Calendar.WEEK_OF_MONTH, startWeek + 1);

		// pass and request the listing
		param.startDate = paramStartCal.getTime();
		param.endDate = paramEndCal.getTime();
		return list(manager, param);
	}

	/** Returns a list of entries for the given parameters */
	public static List<RosterEntry> list(EntityManager manager, RosterQueryParam param) {
		param.startDate = DateUtils.truncate(param.startDate, Calendar.DAY_OF_MONTH);
		param.endDate = DateUtils.truncate(param.endDate, Calendar.DAY_OF_MONTH);
		// the HQL query
		StringBuilder builder = new StringBuilder();
		builder.append(" FROM RosterEntry entry ");
		builder.append(" WHERE ");
		builder.append(" ( ");
		builder.append(" 	( ");
		builder.append(" 		entry.plannedStartDateTime BETWEEN :start AND :end ");
		builder.append("  		OR  ");
		builder.append(" 		entry.plannedEndDateTime BETWEEN :start AND :end ");
		builder.append(" 	) ");
		builder.append(" 	OR ");
		builder.append(" 	( ");
		builder.append(" 		entry.plannedStartDateTime <= :start ");
		builder.append(" 		AND  ");
		builder.append(" 		:end <= entry.plannedEndDateTime ");
		builder.append(" 	) ");
		builder.append(" ) ");
		builder.append(" AND entry.state IN (:states)");
		if (param.location != null) {
			builder.append(" AND entry.location.id = :locationId");
		}

		// set the parameters for the date range
		TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
		query.setParameter("start", param.startDate, TemporalType.DATE);
		query.setParameter("end", param.endDate, TemporalType.DATE);

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

	/** Returns whether or not the given entries are the same */
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
