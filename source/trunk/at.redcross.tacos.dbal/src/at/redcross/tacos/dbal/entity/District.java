package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "District")
public class District extends EntityImpl {

    private static final long serialVersionUID = 9135510026388921765L;

    @Id
    private String shortName;

    @Column(nullable = false)
    private String fullName;

    @OneToMany
    @JoinColumn(name = "District_Fk")
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
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLocations(Collection<Location> locations) {
        this.locations = locations;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public Collection<Location> getLocations() {
        if (locations == null) {
            locations = new ArrayList<Location>();
        }
        return locations;
    }
}
