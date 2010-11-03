package at.redcross.tacos.web.beans.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import at.redcross.tacos.web.parser.RosterParserEntry;

public class RosterParserEntryDto {

    /** the wrapped roster parser entry */
    private final RosterParserEntry entry;
    
    /** custom message field */
    private String message;

    /**
     * Creates a new DTO using the given entry
     */
    public RosterParserEntryDto(RosterParserEntry entry) {
        this.entry = entry;
    }

    // ---------------------------------
    // Object related methods
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", entry.personalNumber).append("time",
                entry.startTime).append("-").append(entry.endTime).toString();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setMessage(String message) {
        this.message = message;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public RosterParserEntry getEntry() {
        return entry;
    }
    
    public String getMessage() {
        return message;
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

    public String getDay() {
        return entry.day;
    }

    public String getStartTime() {
        return entry.startTime;
    }

    public String getEndTime() {
        return entry.endTime;
    }

}
