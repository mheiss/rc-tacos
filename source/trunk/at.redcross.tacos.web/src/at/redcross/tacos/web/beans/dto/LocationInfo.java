package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;

import java.util.List;

import at.redcross.tacos.dbal.entity.Info;
import at.redcross.tacos.dbal.entity.Location;

public class LocationInfo implements Serializable {

	private static final long serialVersionUID = 4475085760715803992L;
	private final Location location;
    private final List<Info> infos;

    public LocationInfo(Location location, List<Info> infos) {
        this.location = location;
        this.infos = infos;
    }

    public Location getLocation() {
        return location;
    }

    public List<Info> getInfos() {
        return infos;
    }
}
