package at.rc.tacos.web.session;

import java.util.Date;

import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

/**
 * Default form values are stored. This user specific object is stored in the class UserSession.
 * @author Payer Martin
 * @version 1.0
 */
public class DefaultFormValues {
	
	// roster entry properties
	private Job defaultJob;
	private StaffMember defaultStaffMember;
	private Location defaultLocation;
	private ServiceType defaultServiceType;
	private Date defaultDate;
	private boolean defaultStandBy;
	private Competence defaultFunction;
	
	public DefaultFormValues() {
		defaultJob = null;
		defaultStaffMember = null;
		defaultLocation = null;
		defaultServiceType = null;
		defaultDate = null;
	}
	public Date getDefaultDate() {
		return defaultDate;
	}
	public void setDefaultDate(Date rosterDefaultDate) {
		this.defaultDate = rosterDefaultDate;
	}
	public Location getDefaultLocation() {
		return defaultLocation;
	}
	public void setDefaultLocation(Location defaultLocation) {
		this.defaultLocation = defaultLocation;
	}
	public StaffMember getDefaultStaffMember() {
		return defaultStaffMember;
	}
	public void setDefaultStaffMember(
			StaffMember staffMemberDefaultStaffMember) {
		this.defaultStaffMember = staffMemberDefaultStaffMember;
	}
	public ServiceType getDefaultServiceType() {
		return defaultServiceType;
	}
	public void setDefaultServiceType(ServiceType defaultServiceType) {
		this.defaultServiceType = defaultServiceType;
	}
	public Job getDefaultJob() {
		return defaultJob;
	}
	public void setDefaultJob(Job defaultJob) {
		this.defaultJob = defaultJob;
	}
	public boolean isDefaultStandBy() {
		return defaultStandBy;
	}
	public void setDefaultStandBy(boolean defaultStandBy) {
		this.defaultStandBy = defaultStandBy;
	}
	public Competence getDefaultFunction() {
		return defaultFunction;
	}
	public void setDefaultFunction(Competence defaultFunction) {
		this.defaultFunction = defaultFunction;
	}
}
