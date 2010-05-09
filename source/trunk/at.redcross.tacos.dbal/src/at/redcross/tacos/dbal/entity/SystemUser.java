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
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SystemUser")
public class SystemUser extends EntityImpl {

	private static final long serialVersionUID = 1556985467901977440L;

	@Id
	private long id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Temporal(TemporalType.DATE)
	private Calendar birthday;

	@Embedded
	@Column(nullable = false)
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	private Login login;

	@ManyToMany
	private Collection<Group> groups;
	
	@OneToMany
	private Collection<Competence> competences;

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
	public void setId(long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
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

	public Gender getGender() {
		return gender;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public Address getAddress() {
		return address;
	}

	public Login getLogin() {
		return login;
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
