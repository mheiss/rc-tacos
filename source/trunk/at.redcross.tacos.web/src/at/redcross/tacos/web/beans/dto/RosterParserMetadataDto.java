package at.redcross.tacos.web.beans.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import at.redcross.tacos.web.parser.RosterParserMetadata;

public class RosterParserMetadataDto {

    /** the wrapped roster metadata entry */
    private final RosterParserMetadata entry;

    /**
     * Creates a new DTO using the given entry
     */
    public RosterParserMetadataDto(RosterParserMetadata entry) {
        this.entry = entry;
    }

    // ---------------------------------
    // Object related methods
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("district", entry.district).append("date",
                entry.monthAndYear).toString();
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
    // Getters for the properties
    // ---------------------------------
    public String getDistrictName() {
        return entry.district;
    }

    public String getMonthAndYear() {
        return entry.monthAndYear;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setMonthAndYear(String monthAndYear) {
        entry.monthAndYear = monthAndYear;
    }

}
