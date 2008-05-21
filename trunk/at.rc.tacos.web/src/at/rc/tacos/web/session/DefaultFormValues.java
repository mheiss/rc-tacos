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
	private Date defaultDate;
	private Location defaultLocation;
	private StaffMember defaultStaffMember;
	public DefaultFormValues() {
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
}
