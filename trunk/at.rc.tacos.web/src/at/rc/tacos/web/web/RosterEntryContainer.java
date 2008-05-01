package at.rc.tacos.web.web;

import java.util.Date;

import at.rc.tacos.model.RosterEntry;

public class RosterEntryContainer {
	private Date plannedStartOfWork;
	private Date plannedEndOfWork;
	private Date realStartOfWork;
	private Date realEndOfWork;
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
}
