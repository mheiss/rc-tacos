package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "ServiceType")
public class ServiceType extends EntityImpl {

    private static final long serialVersionUID = 7780994907233444353L;

    @Id
    private String name;

    @Column(nullable = false)
    private String description;

    /**
     * Default protected constructor for JPA
     */
    protected ServiceType() {
    }

    /**
     * Creates a new {@code ServiceType} using the given unique id
     * 
     * @param name
     *            the name of the service type
     */
    public ServiceType(String name) {
        this.name = name;
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
        return new ToStringBuilder(this).append("name", name).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).hashCode();
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
        ServiceType rhs = (ServiceType) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setDescription(String description) {
        this.description = description;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
