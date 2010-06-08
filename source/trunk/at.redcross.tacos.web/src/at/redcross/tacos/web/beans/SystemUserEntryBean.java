package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.Gender;
import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "systemUserEntryBean")
public class SystemUserEntryBean extends BaseBean {

	private static final long serialVersionUID = 3440196753805921232L;

	// the system user and login
	private long sysUserId = -1;
	private SystemUser systemUser;
	private Login login;

	private String selectedLocation;
	private List<Location> locations;
	private List<SelectItem> locationItems;

	// ?? TODO
	private Collection<Competence> selectedCompetences;
	private List<Competence> competences;
	private List<SelectItem> competenceItems;

	//TODO groups?
	private Collection<Group> selectedGroups;

	private Address address;
	private Calendar birthday;
	private Gender gender;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			systemUser = loadSystemUser(manager, sysUserId);
			
			competences = manager.createQuery("from Competence", Competence.class).getResultList();
			competenceItems = new ArrayList<SelectItem>();
			for (Competence competence : competences){
				competenceItems.add(new SelectItem(competence.getDescription()));
			}
			
			
			locations = manager.createQuery("from Location", Location.class).getResultList();
			locationItems = new ArrayList<SelectItem>();
			for (Location location : locations) {
				locationItems.add(new SelectItem(location.getName()));
			}
		}
		finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	/**
	 * Persists the current entity in the database
	 */
	public String persist() {
		// set the missing attributes
		// systemUser.setCompetences(selectedCompetences);
		// systemUser.setGroups(selectedGroups);
		systemUser.setLocation(lookupLocation(selectedLocation));
		systemUser.setCompetences((lookupCompetence(selectedCompetences)));

		// write to the database
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(systemUser);
				manager.persist(login);
			}
			else {
				manager.merge(systemUser);
				manager.merge(login);
			}
			EntityManagerHelper.commit(manager);
			return "pretty:employee-systemUserView";
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht gespeichert werden");
			return null;
		}
		finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/**
	 * Reverts any changes that may have been done
	 */
	public String revert() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			systemUser = loadSystemUser(manager, systemUser.getId());
			return "pretty:employee-edit";
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht zurückgesetzt werden");
			return null;
		}
		finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	private SystemUser loadSystemUser(EntityManager manager, long id) {
		systemUser = manager.find(SystemUser.class, id);
		if (systemUser == null) {
			sysUserId = -1;
			return new SystemUser();
		}
		selectedCompetences = systemUser.getCompetences();
		selectedGroups = systemUser.getGroups();
		selectedLocation = systemUser.getLocation().getName();
		login = systemUser.getLogin();
		address = systemUser.getAddress();
		gender = systemUser.getGender();
		birthday = systemUser.getBirthday();
		return systemUser;
	}

	private Location lookupLocation(Object value) {
		for (Location location : locations) {
			String locationId = location.getName();
			if (locationId.equals(value)) {
				return location;
			}
		}
		return null;
	}
	
	// TODO ?? return competence or competences?
	private Collection<Competence> lookupCompetence(Object value) {
		for (Competence competence : competences) {
			String competenceId = competence.getDescription();
			if (competenceId.equals(value)) {
				return competences;
			}
		}
		return null;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setLogin(Login login) {
		this.login = login;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}
	
	public void setSelectedCompetences(Collection<Competence> selectedCompetences){
		this.selectedCompetences = selectedCompetences;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return sysUserId == -1;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}
	
	public List<SelectItem> getCompetenceItems() {
		return competenceItems;
	}
	
	public Address getAddress(){
		return address;
	}
	
	public Login getLogin(){
		return login;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
}
