package at.redcross.tacos.web.beans.dto;

import at.redcross.tacos.web.parser.RosterParserEntry;

public class RosterParserDto {

    /** the wrapped roster parser entry */
    private final RosterParserEntry entry;

    /**
     * Creates a new DTO using the given entry
     */
    public RosterParserDto(RosterParserEntry entry) {
        this.entry = entry;
    }

    public String getPersonalNumber() {
        return entry.personalNumber;
    }

    public String getLocationName() {
        return entry.locationName;
    }

    public String getServiceTypeName() {
        return entry.serviceTypeName;
    }

    public String getAssignmentName() {
        return entry.assignmentName;
    }

    public String getDate() {
        return entry.date;
    }

    public String getStartTime() {
        return entry.startTime;
    }

    public String getEndTime() {
        return entry.endTime;
    }

}
