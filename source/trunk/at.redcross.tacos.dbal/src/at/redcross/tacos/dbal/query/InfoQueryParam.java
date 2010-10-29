package at.redcross.tacos.dbal.query;

import java.util.Date;


import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Location;

public class InfoQueryParam {

	/** the location of the info */
	public Location location;

	/** the start and end date of the info (indication for display) */
	public Date dateStart;
	public Date dateEnd;
	
	/** the category of the info */
	public Category category;

	/** show deleted entries */
	public boolean toDelete = false;
	
}
