package at.rc.tacos.web.form;

import java.util.Date;

import at.rc.tacos.model.RosterEntry;

public class RosterEntryContainer {
	public static final int DEADLINE_HOURS = 72;
	
	private Date plannedStartOfWork;
	private Date plannedEndOfWork;
	private Date realStartOfWork;
	private Date realEndOfWork;
	private Date deadline;
	private Date registerStart;
	private RosterEntry rosterEntry;
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
}
