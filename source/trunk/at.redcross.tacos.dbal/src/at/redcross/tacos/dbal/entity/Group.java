package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
    public void setName(String name) {
        this.name = name;
    }

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
