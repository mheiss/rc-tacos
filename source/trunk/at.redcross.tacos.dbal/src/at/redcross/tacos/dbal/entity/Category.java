package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Category")
public class Category extends EntityImpl {

	private static final long serialVersionUID = 6798163625565312161L;

	@Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;

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
        Category rhs = (Category) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }


    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
