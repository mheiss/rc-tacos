package at.rc.tacos.common;

/**
 * Interface for the definition of the available filter types
 * @author Michael
 */
public interface IFilterTypes
{
    /** Filter by id */
    public final static String ID_FILTER = "ID";
    
    /** Filter by staff memeber */
    public final static String EMPLOYEE_FILTER = "employee";
    
    /** Filter by staff station */
    public final static String STATION_FILTER = "station";
    
    /** Filter by single date */
    public final static String DATE_FILTER = "singleDate";
    
    /** Filter by dateRange */
    public final static String DATERANGE_FILTER = "dateRange";
}
