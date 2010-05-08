package at.rc.tacos.web.container;

import java.util.Calendar;
import java.util.Date;

import at.rc.tacos.model.Competence;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;

/**
 * Container for RosterEntry.
 * @author Payer Martin
 * @version 1.0
 * TODO: Roster Entry Container so aufbauen, dass er vom Controller nicht befüllt werden muss.
 */
public class RosterEntryContainer {
	public static final int ADD_ROSTER_ENTRY_DEADLINE_HOURS = 4;
	public static final int EDIT_ROSTER_ENTRY_DEADLINE_HOURS = 36;
	public static final int REGISTER_ROSTER_ENTRY_DEADLINE_HOURS = 24;
	
	private Date deadline;
	private Date registerStart;
	private RosterEntry rosterEntry;
	
	private Function function;
	
	public Function getFunction() {
		return function;
	}
	public void setFunction(Competence function) {
		this.function = new Function(function);
	}
	// Additional field
	public Date getPlannedStartOfWork() {
		return new Date(rosterEntry.getPlannedStartOfWork());
	}
	public Date getPlannedEndOfWork() {
		return new Date(rosterEntry.getPlannedEndOfWork());
	}
	public Date getRealStartOfWork() {
		if (rosterEntry.getRealStartOfWork() == 0) return null; else return new Date(rosterEntry.getRealStartOfWork());
	}
	public Date getRealEndOfWork() {
		if (rosterEntry.getRealEndOfWork() == 0) return null; else return new Date(rosterEntry.getRealEndOfWork());
	}
	public RosterEntry getRosterEntry() {
		return rosterEntry;
	}
	public void setRosterEntry(RosterEntry rosterEntry) {
		this.rosterEntry = rosterEntry;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public Date getRegisterStart() {
		return registerStart;
	}
	public void setRegisterStart(Date registerStart) {
		this.registerStart = registerStart;
	}
	
	public int getPlannedStartOfWorkDayOfYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedStartOfWork());
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	public int getPlannedStartOfWorkMonth() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedStartOfWork());
		return calendar.get(Calendar.MONTH);
	}
	
	public int getPlannedStartOfWorkYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedStartOfWork());
		return calendar.get(Calendar.YEAR);
	}
	
	public int getPlannedEndOfWorkDayOfYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedEndOfWork());
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	public int getPlannedEndOfWorkMonth() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedEndOfWork());
		return calendar.get(Calendar.MONTH);
	}
	
	public int getPlannedEndOfWorkYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(getPlannedEndOfWork());
		return calendar.get(Calendar.YEAR);
	}
	
	public int getRealStartOfWorkDayOfYear() {
		if (getRealStartOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealStartOfWork());
			return calendar.get(Calendar.DAY_OF_YEAR);
		} else return -1;
	}
	
	public int getRealStartOfWorkMonth() {
		if (getRealStartOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealStartOfWork());
			return calendar.get(Calendar.MONTH);
		} else return -1;
	}
	
	public int getRealStartOfWorkYear() {
		if (getRealStartOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealStartOfWork());
			return calendar.get(Calendar.YEAR);
		} else return -1;
	}
	
	public int getRealEndOfWorkDayOfYear() {
		if (getRealEndOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealEndOfWork());
			return calendar.get(Calendar.DAY_OF_YEAR);
		} else return -1;
	}
	
	public int getRealEndOfWorkMonth() {
		if (getRealEndOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealEndOfWork());
			return calendar.get(Calendar.MONTH);
		} else return -1;
	}
	
	public int getRealEndOfWorkYear() {
		if (getRealEndOfWork() != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(getRealEndOfWork());
			return calendar.get(Calendar.YEAR);
		} else return -1;
	}
	
	public long getRealDuration() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getRealDurationHours() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60); 
		} else return 0;
	}
	
	public int getRealDurationMinutes() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getRealDurationHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
	public long getRealDurationWeighted() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getRealDurationWeightedHours() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60);
		} else return 0;
	}
	
	public int getRealDurationWeightedMinutes() {
		if (getRealStartOfWork() != null && getRealEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getRealEndOfWork().getTime() - getRealStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getRealDurationWeightedHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
	public long getDurationForStatistic() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getDurationForStatisticHours() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60);
		} else return 0;
	}
	
	public int getDurationForStatisticMinutes() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getDurationForStatisticHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
	public long getDurationForStatisticWeighted() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getDurationForStatisticWeightedHours() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60);
		} else return 0;
	}
	
	public int getDurationForStatisticWeightedMinutes() {
		if (getRealStartOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = 0;
			if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getRealStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getRealStartOfWork().getTime());
				}
			} else {
				if (getRealEndOfWork() != null) {
					diffMilliSeconds = (getRealEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				} else {
					diffMilliSeconds = (getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime());
				}
			}
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getDurationForStatisticWeightedHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
	public long getPlannedDuration() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getPlannedDurationHours() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60);
		} else return 0;
	}
	
	public int getPlannedDurationMinutes() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getPlannedDurationHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
	public long getPlannedDurationWeighted() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return diffMilliSeconds;
		} else return 0;
	}
	
	public int getPlannedDurationWeightedHours() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)diffMilliSeconds/(1000*60*60);
		} else return 0;
	}
	
	public int getPlannedDurationWeightedMinutes() {
		if (getPlannedStartOfWork() != null && getPlannedEndOfWork() != null) {
			//Calculate difference in milliseconds
			long diffMilliSeconds = getPlannedEndOfWork().getTime() - getPlannedStartOfWork().getTime();
			if (rosterEntry.getStandby()) {
				diffMilliSeconds = diffMilliSeconds/3;
			}
			if (diffMilliSeconds < 0)diffMilliSeconds = 0;
			return (int)((diffMilliSeconds-getPlannedDurationWeightedHours()*(1000*60*60))/(1000*60));
		} else return 0;
	}
	
}
