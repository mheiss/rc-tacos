package at.redcross.tacos.dbal.query;

import java.io.Serializable;
import java.util.Date;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Location;

/** Holds query parameters for the info entry */
public class InfoQueryParam implements Serializable {

    private static final long serialVersionUID = 7043737334456427879L;

    /** the location of the info */
    public Location location;

    /** the start date */
    public Date dateStart;

    /** the end date */
    public Date dateEnd;

    /** the category of the info */
    public Category category;

    /** show deleted entries */
    public boolean toDelete = false;

}
