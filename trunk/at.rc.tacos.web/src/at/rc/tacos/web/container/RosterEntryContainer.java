package at.rc.tacos.web.container;

import java.util.Calendar;
import java.util.Date;

import at.rc.tacos.model.Competence;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.web.container.Function;

/**
 * Container for RosterEntry.
 * @author Payer Martin
 * @version 1.0
 */
public class RosterEntryContainer {
	public static final int ADD_ROSTER_ENTRY_DEADLINE_HOURS = 4;
	public static final int EDIT_ROSTER_ENTRY_DEADLINE_HOURS = 36;
	public static final int REGISTER_ROSTER_ENTRY_DEADLINE_HOURS = 24;
	
	private Date plannedStartOfWork;
	private Date plannedEndOfWork;
	private Date realStartOfWork;
	private Date realEndOfWork;
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
		return plannedStartOfWork;
	}
	public void setPlannedStartOfWork(Date plannedStartOfWork) {
		this.plannedStartOfWork = plannedStartOfWork;
	}
	public Date getPlannedEndOfWork() {
		return plannedEndOfWork;
	}
	public void setPlannedEndOfWork(Date plannedEndOfWork) {
		this.plannedEndOfWork = plannedEndOfWork;
	}
	public Date getRealStartOfWork() {
		return realStartOfWork;
	}
	public void setRealStartOfWork(Date realStartOfWork) {
		this.realStartOfWork = realStartOfWork;
	}
	public Date getRealEndOfWork() {
		return realEndOfWork;
	}
	public void setRealEndOfWork(Date realEndOfWork) {
		this.realEndOfWork = realEndOfWork;
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
		calendar.setTime(plannedStartOfWork);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	public int getPlannedStartOfWorkMonth() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(plannedStartOfWork);
		return calendar.get(Calendar.MONTH);
	}
	
	public int getPlannedStartOfWorkYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(plannedStartOfWork);
		return calendar.get(Calendar.YEAR);
	}
	
	public int getPlannedEndOfWorkDayOfYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(plannedEndOfWork);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	public int getPlannedEndOfWorkMonth() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(plannedEndOfWork);
		return calendar.get(Calendar.MONTH);
	}
	
	public int getPlannedEndOfWorkYear() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(plannedEndOfWork);
		return calendar.get(Calendar.YEAR);
	}
	
	public int getRealStartOfWorkDayOfYear() {
		if (realStartOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realStartOfWork);
			return calendar.get(Calendar.DAY_OF_YEAR);
		} else return -1;
	}
	
	public int getRealStartOfWorkMonth() {
		if (realStartOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realStartOfWork);
			return calendar.get(Calendar.MONTH);
		} else return -1;
	}
	
	public int getRealStartOfWorkYear() {
		if (realStartOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realStartOfWork);
			return calendar.get(Calendar.YEAR);
		} else return -1;
	}
	
	public int getRealEndOfWorkDayOfYear() {
		if (realEndOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realEndOfWork);
			return calendar.get(Calendar.DAY_OF_YEAR);
		} else return -1;
	}
	
	public int getRealEndOfWorkMonth() {
		if (realEndOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realEndOfWork);
			return calendar.get(Calendar.MONTH);
		} else return -1;
	}
	
	public int getRealEndOfWorkYear() {
		if (realEndOfWork != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(realEndOfWork);
			return calendar.get(Calendar.YEAR);
		} else return -1;
	}
	
}
