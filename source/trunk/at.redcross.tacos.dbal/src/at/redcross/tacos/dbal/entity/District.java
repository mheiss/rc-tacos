package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "District")
public class District {

	@Id
	private String name;

	@OneToMany(mappedBy = "district")
	private Collection<Location> locations;

	// ---------------------------------
	// public API
	// ---------------------------------
	/**
	 * Adds the given location to this district
	 * 
	 * @param location
	 *            the location to add
	 */
	public void addLocation(Location location) {
		Collection<Location> locations = getLocations();
		if (locations.contains(locations)) {
			return;
		}
		location.setDistrict(this);
		locations.add(location);
	}

	/**
	 * Removes the given location from this district.
	 * 
	 * @param location
	 *            the location to remove
	 */
	public void removeLocation(Location location) {
		if (getLocations().remove(location)) {
			location.setDistrict(null);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setLocations(Collection<Location> locations) {
		this.locations = locations;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getName() {
		return name;
	}

	public Collection<Location> getLocations() {
		if (locations == null) {
			locations = new ArrayList<Location>();
		}
		return locations;
	}
}
