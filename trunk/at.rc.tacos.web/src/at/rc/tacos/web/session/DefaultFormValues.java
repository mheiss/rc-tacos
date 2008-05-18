package at.rc.tacos.web.session;

import java.util.Date;

import at.rc.tacos.model.Location;
import at.rc.tacos.model.StaffMember;

/**
 * Default form values are stored. This user specific object is stored in the class UserSession.
 * @author Payer Martin
 * @version 1.0
 */
public class DefaultFormValues {
	private Date rosterDefaultDate;
	private Location rosterDefaultLocation;
	private StaffMember staffMemberDefaultStaffMember;
	public DefaultFormValues() {
		rosterDefaultDate = null;
	}
	public Date getRosterDefaultDate() {
		return rosterDefaultDate;
	}
	public void setRosterDefaultDate(Date rosterDefaultDate) {
		this.rosterDefaultDate = rosterDefaultDate;
	}
	public Location getRosterDefaultLocation() {
		return rosterDefaultLocation;
	}
	public void setRosterDefaultLocation(Location defaultLocation) {
		this.rosterDefaultLocation = defaultLocation;
	}
	public StaffMember getStaffMemberDefaultStaffMember() {
		return staffMemberDefaultStaffMember;
	}
	public void setStaffMemberDefaultStaffMember(
			StaffMember staffMemberDefaultStaffMember) {
		this.staffMemberDefaultStaffMember = staffMemberDefaultStaffMember;
	}
}
