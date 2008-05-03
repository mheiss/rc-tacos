package at.rc.tacos.web.session;

import java.util.Date;
import at.rc.tacos.model.Location;

/**
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class FormDefaultValues {
	private Date rosterDefaultDate;
	private Location rosterDefaultLocation;
	public FormDefaultValues() {
		rosterDefaultDate = null;
		rosterDefaultLocation = null;
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
	public void setRosterDefaultLocation(Location rosterDefaultLocation) {
		this.rosterDefaultLocation = rosterDefaultLocation;
	}
}
