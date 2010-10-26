package at.redcross.tacos.dbal.query;

import java.util.Date;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.ServiceType;

public class RosterQueryParam {

	/** the location of the entry */
	public Location location;

	/** the date of the entry */
	public Date date;

	/** show deleted entries */
	public boolean toDelete = false;

	/** the owner of the entry */
	public Integer systemUserId = null;
	
	//statistic params
	/** the location of the user */
	public Location locationOfUser;

	/** month of the roster entry */
	public Date month;

	
	/** year of the roster entry */
	public Date year;

	
	/** service type of the roster entry */
	public ServiceType serviceType;

	
	
	
}
