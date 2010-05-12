package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Assignment")
public class Assignment extends EntityImpl {

    private static final long serialVersionUID = -2510131078051328478L;

    @Id
    private String id;

    @Column(nullable = false)
    private String description;

    /**
     * Default protected constructor for JPA
     */
    protected Assignment() {
    }

    /**
     * Creates a new {@code Assignment} using the given name
     * 
     * @param id
     *            the unique name of the assignment
     */
    public Assignment(String id) {
        this.id = id;
    }

    // ---------------------------------
    // 
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
        Assignment rhs = (Assignment) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
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
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
