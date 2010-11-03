package at.redcross.tacos.web.parser;

/**
 * The {@code RosterParserEntry} represents a single entry that is parsed out of
 * the source file.
 */
public class RosterParserEntry {

    /** the date of the entry */
    public String day;

    /** the start time of the entry */
    public String startTime;

    /** the end time of the entry */
    public String endTime;

    /** the personal number of the employee */
    public String personalNumber;

    /** the full name of the location */
    public String locationName;

    /** the service type */
    public String serviceTypeName;

    /** the assignment */
    public String assignmentName;
}
