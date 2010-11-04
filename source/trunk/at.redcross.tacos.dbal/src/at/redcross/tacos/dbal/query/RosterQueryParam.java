package at.redcross.tacos.dbal.query;

import java.util.Date;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.ServiceType;

public class RosterQueryParam {

    /** the start range for the query */
    public Date startDate;

    /** the end range for the query */
    public Date endDate;

    /** show deleted entries */
    public boolean toDelete = false;

    /** the owner of the entry */
    public Integer systemUserId = null;

    /** the service type of the entry */
    public ServiceType serviceType;

    /** the location of the entry */
    public Location location;

}
