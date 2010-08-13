package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;
import java.util.List;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;

public class LocationUsers implements Serializable {

    private static final long serialVersionUID = -1748313699178349834L;

    private final Location location;
    private final List<SystemUser> entries;

    public LocationUsers(Location location, List<SystemUser> entries) {
        this.location = location;
        this.entries = entries;
    }

    public Location getLocation() {
        return location;
    }

    public List<SystemUser> getEntries() {
        return entries;
    }
}