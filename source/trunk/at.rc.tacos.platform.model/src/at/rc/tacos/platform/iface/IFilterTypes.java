package at.rc.tacos.platform.iface;

/**
 * Interface for the definition of the available filter types
 * 
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
    
    /** Filter by search string */
    public final static String SEARCH_STRING = "searchFilter";
    
    /** Search filter for the address */
    public final static String SEARCH_STRING_STREET = "searchFilterStreet";
    public final static String SEARCH_STRING_CITY = "searchFilterCity";
    public final static String SEARCH_STRING_ZIP = "searchFilterZip";
    public final static String SEARCH_STRING_STREETNUMBER = "searchFilterStreetNumber";
    
    /** Filter roster by location */
    public final static String ROSTER_LOCATION_FILTER = "rosterLocationFilter";
    
    /** Get only roster entries where checkin and checkout not null */
    public final static String ROSTER_MONTH_STATISTIC_FILTER = "rosterStatisticFilter";
    
    /** Filter roster by month */
    public final static String ROSTER_MONTH_FILTER = "rosterMonthFilter";
    
    /** Filter roster by year */
    public final static String ROSTER_YEAR_FILTER = "rosterYearFilter";
    
    /** Filter roster by function (staffMember's competence) */
    public final static String ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER = "rosterFunctionStaffMemberCompetenceFilter";
    
    /** Filter roster by primary location of staff member */
    public final static String ROSTER_LOCATION_STAFF_MEMBER_FILTER = "rosterLocationStaffMemberFilter";
    
    /** Filter roster by staff member */
    public final static String ROSTER_STAFF_MEMBER_FILTER = "rosterStaffMemberFilter";
    
    /** Filter roster by service type */
    public final static String ROSTER_SERVICE_TYPE_FILTER = "rosterServiceTypeFilter";
    
    /** Filter by lastname */
    public final static String SICK_PERSON_LASTNAME_FILTER = "lastnameFilter";
    
    /** Filter staff member by primary location */
    public final static String STAFF_MEMBER_LOCATION_FILTER = "staffMemberLocationFilter";
    
    /** Filters all staff members */
    public final static String STAFF_MEMBER_LOCKED_UNLOCKED_FILTER = "staffMemberLockedUnlockedFilter";
    
    /** Filter locked staff member */
    public final static String STAFF_MEMBER_LOCKED_FILTER = "staffMemberLockedFilter";
    
    /** Filter prebooked transports */
    public final static String TRANSPORT_PREBOOKING_FILTER = "transportPrebookingFilter";
    
    /** Filter transports todo (prebooked and outstanding) */
    public final static String TRANSPORT_TODO_FILTER = "transportTodoFilter";
    
    /** Filter running transports */
    public final static String TRANSPORT_RUNNING_FILTER = "transportRunningFilter";
    
    /** Filter archived transports */
    public final static String TRANSPORT_ARCHIVED_FILTER = "transportArchivedFilter";

    /** Filter archived transports by location */
    public final static String TRANSPORT_LOCATION_FILTER = "transportLocationFilter";
    
    public final static String SERVICETYPE_COMPETENCE_FILTER = "serviceTypeCompetenceFilter";
    
    public final static String TRANSPORT_UNDERWAY_FILTER = "transportUnderwayFilter";
    
    public final static String TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER = "transportJournalShortVehicleFilter";
}
