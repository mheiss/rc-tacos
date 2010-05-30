package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "SystemUser")
public class SystemUser extends EntityImpl {

    private static final long serialVersionUID = 1556985467901977440L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    
    @Column
    private int pnr;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Calendar birthday;
    
    @Column
    private String phoneI;
    
    @Column
    private String phoneII;
    
    @Column
    private String notes;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @OneToOne(mappedBy = "systemUser", orphanRemoval = true, cascade = CascadeType.ALL)
    private Login login;
    
    @OneToOne
    private Location location;

    @ManyToMany
    private Collection<Group> groups;

    @ManyToMany
    private Collection<Competence> competences;

    // ---------------------------------
    // 
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("firstName", firstName).append(
                "lastName", lastName).toString();
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
        SystemUser rhs = (SystemUser) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Custom methods
    // ---------------------------------
    /**
     * Adds this user to the given group
     * 
     * @param group
     *            the group to add this user
     */
    public void addGroup(Group group) {
        getGroups().add(group);
    }

    /**
     * Returns whether or not this user has the given authority. All current
     * roles are considered when the authority is queried.
     * 
     * @param authority
     *            the authority to query
     */
    public boolean hasAuthority(String name) {
        List<Authority> authorities = new ArrayList<Authority>();
        for (Group group : getGroups()) {
            authorities.addAll(group.getAuthority());
        }
        Iterator<Authority> authIter = authorities.iterator();
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
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setPNr(int pnr) {
        this.pnr = pnr;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
    
    public void setPhoneI(String phoneI) {
        this.phoneI = phoneI;
    }
    
    public void setPhoneII(String phoneII) {
        this.phoneII = phoneII;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    public void setCompetences(Collection<Competence> competences) {
        this.competences = competences;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getPNr() {
        return pnr;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public Gender getGender() {
        return gender;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public Address getAddress() {
        return address;
    }
    
    public String getPhoneI() {
        return phoneI;
    }
    
    public String getPhoneII() {
        return phoneII;
    }

    public Login getLogin() {
        return login;
    }
    
    public String getNotes() {
        return notes;
    }

    public Collection<Group> getGroups() {
        if (groups == null) {
            groups = new ArrayList<Group>();
        }
        return groups;
    }

    public Collection<Competence> getCompetences() {
        if (competences == null) {
            competences = new ArrayList<Competence>();
        }
        return competences;
    }
}
