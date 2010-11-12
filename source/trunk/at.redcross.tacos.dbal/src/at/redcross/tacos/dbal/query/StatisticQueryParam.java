package at.redcross.tacos.dbal.query;

import java.util.Date;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.ServiceType;

public class StatisticQueryParam {

    /** the start range for the query */
    public Date startDate;

    /** the end range for the query */
    public Date endDate;

    /** show deleted entries */
    public boolean stateDelete = false;
    
    /** show normal entries */
    public boolean stateNormal = true;

    /** the owner of the entry */
    public Integer systemUserId = null;

    /** the service type of the entry */
    public ServiceType serviceType;

    /** the location of the entry */
    public Location location;
    
    /** the year of the statistic result*/
    public int year;
    
    /** the month of the statistic result*/
    public int month;

}
