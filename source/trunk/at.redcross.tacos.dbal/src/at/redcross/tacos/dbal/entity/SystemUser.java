package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "SystemUser")
public class SystemUser extends EntityImpl {

	private static final long serialVersionUID = 1556985467901977440L;

	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true)
	private int pnr;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender = Gender.UNKNOWN;

	@Column
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(length=1024)
	private String notes;

	@Embedded
	@Column(nullable = false)
	private Address address;

	@OneToOne(optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
	private Login login;

	@ManyToOne(optional = false)
	private Location location;

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Group> groups;

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Competence> competences;

	@Column
	private boolean toDelete;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return lastName + " " + firstName;
	}

	// ---------------------------------
	// Object related methods
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
	// Setters for the properties
	// ---------------------------------
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setPnr(int pnr) {
		this.pnr = pnr;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void setCompetences(List<Competence> competences) {
		this.competences = competences;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
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

	public int getPnr() {
		return pnr;
	}

	public Location getLocation() {
		return location;
	}

	public Gender getGender() {
		return gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Address getAddress() {
		return address;
	}

	public Login getLogin() {
		return login;
	}

	public String getNotes() {
		return notes;
	}

	public List<Group> getGroups() {
		if (groups == null) {
			groups = new ArrayList<Group>();
		}
		return groups;
	}

	public List<Competence> getCompetences() {
		if (competences == null) {
			competences = new ArrayList<Competence>();
		}
		return competences;
	}

	public boolean isToDelete() {
		return toDelete;
	}

}
