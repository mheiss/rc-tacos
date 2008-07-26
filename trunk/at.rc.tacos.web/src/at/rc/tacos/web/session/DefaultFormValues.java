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
	
	// Default values for Roster Month
	private Location rosterMonthLocation;
	private Integer rosterMonthMonth;
	private Integer rosterMonthYear;
	private Competence rosterMonthFunction;
	private Location rosterMonthLocationStaffMember;
	private StaffMember rosterMonthStaffMember;
	
	// Default values for Admin Statistic
	private Location adminStatisticLocation;
	private Integer adminStatisticMonth;
	private Integer adminStatisticYear;
	private Location adminStatisticLocationStaffMember;
	private StaffMember adminStatisticStaffMember;
	private ServiceType adminStatisticServiceType;
	
	// Default values for Personnel Statistic
	private Location personnelStatisticLocation;
	private Integer personnelStatisticMonth;
	private Integer personnelStatisticYear;
	
	public Location getAdminStatisticLocation() {
		return adminStatisticLocation;
	}
	public void setAdminStatisticLocation(Location adminStatisticLocation) {
		this.adminStatisticLocation = adminStatisticLocation;
	}
	public Integer getAdminStatisticMonth() {
		return adminStatisticMonth;
	}
	public void setAdminStatisticMonth(Integer adminStatisticMonth) {
		this.adminStatisticMonth = adminStatisticMonth;
	}
	public Integer getAdminStatisticYear() {
		return adminStatisticYear;
	}
	public void setAdminStatisticYear(Integer adminStatisticYear) {
		this.adminStatisticYear = adminStatisticYear;
	}
	public Location getAdminStatisticLocationStaffMember() {
		return adminStatisticLocationStaffMember;
	}
	public void setAdminStatisticLocationStaffMember(
			Location adminStatisticLocationStaffMember) {
		this.adminStatisticLocationStaffMember = adminStatisticLocationStaffMember;
	}
	public StaffMember getAdminStatisticStaffMember() {
		return adminStatisticStaffMember;
	}
	public void setAdminStatisticStaffMember(StaffMember adminStatisticStaffMember) {
		this.adminStatisticStaffMember = adminStatisticStaffMember;
	}
	public Location getRosterMonthLocationStaffMember() {
		return rosterMonthLocationStaffMember;
	}
	public void setRosterMonthLocationStaffMember(
			Location rosterMonthLocationStaffMember) {
		this.rosterMonthLocationStaffMember = rosterMonthLocationStaffMember;
	}
	public DefaultFormValues() {
		defaultJob = null;
		defaultStaffMember = null;
		defaultLocation = null;
		defaultServiceType = null;
		defaultDate = null;
		defaultStandBy = false;
		
		rosterMonthLocation = null;
		rosterMonthMonth = null;
		rosterMonthYear = null;
		rosterMonthFunction = null;
		rosterMonthLocationStaffMember = null;
		rosterMonthStaffMember = null;
		
		adminStatisticLocation = null;
		adminStatisticMonth = null;
		adminStatisticYear = null;
		adminStatisticLocationStaffMember = null;
		adminStatisticStaffMember = null;
		adminStatisticServiceType = null;
		
		personnelStatisticLocation = null;
		personnelStatisticMonth = null;
		personnelStatisticYear = null;
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
	public Competence getRosterMonthFunction() {
		return rosterMonthFunction;
	}
	public void setRosterMonthFunction(Competence defaultFunction) {
		this.rosterMonthFunction = defaultFunction;
	}
	public Location getRosterMonthLocation() {
		return rosterMonthLocation;
	}
	public void setRosterMonthLocation(Location rosterMonthLocation) {
		this.rosterMonthLocation = rosterMonthLocation;
	}
	public Integer getRosterMonthMonth() {
		return rosterMonthMonth;
	}
	public void setRosterMonthMonth(Integer rosterMonthMonth) {
		this.rosterMonthMonth = rosterMonthMonth;
	}
	public Integer getRosterMonthYear() {
		return rosterMonthYear;
	}
	public void setRosterMonthYear(Integer rosterMonthYear) {
		this.rosterMonthYear = rosterMonthYear;
	}
	public StaffMember getRosterMonthStaffMember() {
		return rosterMonthStaffMember;
	}
	public void setRosterMonthStaffMember(StaffMember rosterMonthStaffMember) {
		this.rosterMonthStaffMember = rosterMonthStaffMember;
	}
	public Location getPersonnelStatisticLocation() {
		return personnelStatisticLocation;
	}
	public void setPersonnelStatisticLocation(Location personnelStatisticLocation) {
		this.personnelStatisticLocation = personnelStatisticLocation;
	}
	public Integer getPersonnelStatisticMonth() {
		return personnelStatisticMonth;
	}
	public void setPersonnelStatisticMonth(Integer personnelStatisticMonth) {
		this.personnelStatisticMonth = personnelStatisticMonth;
	}
	public Integer getPersonnelStatisticYear() {
		return personnelStatisticYear;
	}
	public void setPersonnelStatisticYear(Integer personnelStatisticYear) {
		this.personnelStatisticYear = personnelStatisticYear;
	}
	public ServiceType getAdminStatisticServiceType() {
		return adminStatisticServiceType;
	}
	public void setAdminStatisticServiceType(ServiceType adminStatisticServiceType) {
		this.adminStatisticServiceType = adminStatisticServiceType;
	}
}
