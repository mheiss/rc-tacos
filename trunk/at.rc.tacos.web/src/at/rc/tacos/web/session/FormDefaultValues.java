package at.rc.tacos.web.session;

import java.util.Date;

import at.rc.tacos.model.Location;

/**
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class FormDefaultValues {
	private Date defaultDate;
	private Location defaultLocation;
	public FormDefaultValues() {
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
}
