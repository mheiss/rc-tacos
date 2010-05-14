package at.redcross.tacos.web.entity;

import java.io.Serializable;
import java.util.List;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;

/**
 * The {@code LocationRosterEntry} provides a mapping between a
 * {@linkplain Location location} and the associated {@linkplain RosterEntry
 * entries}.
 */
public class LocationRosterEntry implements Serializable {

    private static final long serialVersionUID = -1748313699178349834L;

    private final Location location;
    private final List<RosterEntry> entries;

    public LocationRosterEntry(Location location, List<RosterEntry> entries) {
        this.location = location;
        this.entries = entries;
    }

    public Location getLocation() {
        return location;
    }

    public List<RosterEntry> getEntries() {
        return entries;
    }
}
