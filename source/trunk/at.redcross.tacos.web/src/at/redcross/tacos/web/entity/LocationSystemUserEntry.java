package at.redcross.tacos.web.entity;

import java.io.Serializable;
import java.util.List;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;

/**
 * The {@code LocationSystemUserEntry} provides a mapping between a
 * {@linkplain Location location} and the associated {@linkplain SystemUser
 * entries}.
 */
public class LocationSystemUserEntry implements Serializable {

    private static final long serialVersionUID = -1748313699178349834L;

    private final Location location;
    private final List<SystemUser> entries;

    public LocationSystemUserEntry(Location location, List<SystemUser> entries) {
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
