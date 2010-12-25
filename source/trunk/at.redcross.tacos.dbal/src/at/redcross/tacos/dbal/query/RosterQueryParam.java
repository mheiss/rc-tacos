package at.redcross.tacos.dbal.query;

import java.io.Serializable;
import java.util.Date;

import at.redcross.tacos.dbal.entity.Location;

/** Holds query parameters for roster entries */
public class RosterQueryParam implements Serializable {

    private static final long serialVersionUID = 4232731266426352731L;

    /** the start range for the query */
    public Date startDate;

    /** the end range for the query */
    public Date endDate;

    /** show deleted entries */
    public boolean stateDelete = false;

    /** show normal entries */
    public boolean stateNormal = true;

    /** the location of the entry */
    public Location location;

    // ---------------------------------
    // Getters and Setters
    // ---------------------------------
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isStateDelete() {
        return stateDelete;
    }

    public void setStateDelete(boolean stateDelete) {
        this.stateDelete = stateDelete;
    }

    public boolean isStateNormal() {
        return stateNormal;
    }

    public void setStateNormal(boolean stateNormal) {
        this.stateNormal = stateNormal;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
