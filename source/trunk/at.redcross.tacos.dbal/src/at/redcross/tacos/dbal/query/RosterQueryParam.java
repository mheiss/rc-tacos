package at.redcross.tacos.dbal.query;

import java.util.Date;

import at.redcross.tacos.dbal.entity.Location;

public class RosterQueryParam {

	/** the location of the entry */
	public Location location;

	/** the date of the entry */
	public Date date;

	/** show deleted entries */
	public boolean toDelete = false;

	/** the owner of the entry */
	public Integer systemUserId = null;

}
