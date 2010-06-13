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
@Table(name = "ChangelogEntry")
public class ChangelogEntry extends EntityImpl {

    private static final long serialVersionUID = -6956043329210269429L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String message;

    // ---------------------------------
    // EntityImpl
    // ---------------------------------
    @Override
    public String getDisplayString() {
        return message;
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
        ChangelogEntry rhs = (ChangelogEntry) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // -----------------------------------
    // Setters for the properties
    // -----------------------------------
    public void setMessage(String message) {
        this.message = message;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
