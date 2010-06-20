package at.redcross.tacos.web.beans;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.web.entity.LocationRosterEntry;

/** Provide base functions for all roster beans */
public abstract class RosterViewBean extends BaseBean {

    private static final long serialVersionUID = -63594513702881676L;

    // filter by location
    protected Location location;
    protected List<SelectItem> locationItems;

    // filter by date
    protected Date date;

    // queried result
    protected List<RosterEntry> entries;
    protected List<LocationRosterEntry> locationEntry;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public List<RosterEntry> getEntries() {
        return entries;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public List<LocationRosterEntry> getLocationEntry() {
        return locationEntry;
    }
}
