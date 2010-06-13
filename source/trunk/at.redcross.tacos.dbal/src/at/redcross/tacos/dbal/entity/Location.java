package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Location")
public class Location extends EntityImpl {

    private static final long serialVersionUID = 6997613929181751597L;

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @ManyToOne
    private District district;
    

    /**
     * Default protected constructor for JPA
     */
    protected Location() {
    }

    /**
     * Creates a new {@code Location} using the id name and district
     * 
     * @param district
     *            the district of the location
     * @param name
     *            the short name of the location
     */
    public Location(District district, String id) {
        this.id = id;
        this.district = district;
    }
    
    // ---------------------------------
    // EntityImpl
    // ---------------------------------
    @Override
    public String getDisplayString() {
        return name;
    }

    // ---------------------------------
    // Object related methods
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Location rhs = (Location) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public District getDistrict() {
        return district;
    }
}
