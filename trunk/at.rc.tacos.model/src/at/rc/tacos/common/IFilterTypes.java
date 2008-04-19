package at.rc.tacos.common;

/**
 * Interface for the definition of the available filter types
 * @author Michael
 */
public interface IFilterTypes
{
    /** Filter by id */
    public final static String ID_FILTER = "ID";
    
    /** Filter by single date */
    public final static String DATE_FILTER = "singleDate";
    
    /** Filter by dateRange */
    public final static String DATERANGE_FILTER = "dateRange";
    
    /** Filter by type */
    public final static String TYPE_FILTER = "typeFilter";
    
    /** Filter by name */
    public final static String SERVICETYPE_FILTER = "serviceTypeFilter";
    
}
