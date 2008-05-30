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
    public final static String SERVICETYPE_SERVICENAME_FILTER = "serviceTypeFilter";
    
    /** Filter by username */
    public final static String USERNAME_FILTER = "userNameFilter";
    
    /** Filter roster by location */
    public final static String ROSTER_LOCATION_FILTER = "rosterLocationFilter";
    
    /** Filter by search string */
    public final static String SEARCH_STRING = "searchFilter";
    
    /** Search filter for the address */
    public final static String SEARCH_STRING_STREET = "searchFilterStreet";
    public final static String SEARCH_STRING_CITY = "searchFilterCity";
    public final static String SEARCH_STRING_ZIP = "searchFilterZip";
    public final static String SEARCH_STRING_STREETNUMBER = "searchFilterStreetNumber";
    
    /** Filter roster by month */
    public final static String ROSTER_MONTH_FILTER = "rosterMonthFilter";
    
    /** Filter roster by year */
    public final static String ROSTER_YEAR_FILTER = "rosterYearFilter";
    
    /** Filter roster by competence */
    public final static String ROSTER_FUNCTION_FILTER = "rosterCompetenceFilter";
    
    /** Filter roster by staff member */
    public final static String ROSTER_STAFF_MEMBER_FILTER = "rosterStaffMemberFilter";
    
    /** Filter by lastname */
    public final static String SICK_PERSON_LASTNAME_FILTER = "lastnameFilter";
    
    /** Filter staff member by primary location */
    public final static String STAFF_MEMBER_LOCATION_FILTER = "staffMemberLocationFilter";
}
