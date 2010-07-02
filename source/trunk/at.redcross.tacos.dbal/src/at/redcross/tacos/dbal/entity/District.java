package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "District")
public class District extends EntityImpl {

    private static final long serialVersionUID = 9135510026388921765L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String shortName;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "District_Fk")
    private Collection<Location> locations;

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
        District rhs = (District) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setLocations(Collection<Location> locations) {
        this.locations = locations;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------

    public long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

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
