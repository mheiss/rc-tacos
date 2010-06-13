package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "UserGroup")
public class Group extends EntityImpl {

    private static final long serialVersionUID = 3623563317740713699L;

    @Id
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    private Collection<Authority> authority;

    /**
     * Default protected constructor for JPA
     */
    protected Group() {
    }

    /**
     * Creates a new {@code Group} using the given name
     * 
     * @param name
     *            the name of the group
     */
    public Group(String name) {
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
        Group rhs = (Group) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    // ---------------------------------
    // Custom methods
    // ---------------------------------
    /**
     * Returns whether or not this group has the given authority
     * 
     * @param authority
     *            the authority to query
     */
    public boolean hasAuthority(String name) {
        Iterator<Authority> authIter = getAuthority().iterator();
        while (authIter.hasNext()) {
            Authority authority = authIter.next();
            if (authority.getId().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setDescription(String description) {
        this.description = description;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Authority> getAuthority() {
        if (authority == null) {
            authority = new ArrayList<Authority>();
        }
        return authority;
    }
}
