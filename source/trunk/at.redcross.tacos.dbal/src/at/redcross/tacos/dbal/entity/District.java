package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "District")
public class District {

	@Id
	private String name;

	@ManyToOne
	@JoinColumn(name = "location")
	private Collection<Location> locations;

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
