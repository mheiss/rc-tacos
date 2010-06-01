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
	private Address address;

	// the suggested values for the UI
	// FIXME: Address records should not be listed
	// automatically create a new address record when editing a system-user
	private String selectedAddress;
	private List<Address> addresses;
	private List<SelectItem> addressItems;

	// FIXME: logins should not be listed. A login should be created
	// automatically when creating a new system user.
	// Separate management of Login and SystemUser is not good
	private String selectedLogin;
	private List<Login> logins;
	private List<SelectItem> loginItems;

	private String selectedLocation;
	private List<Location> locations;
	private List<SelectItem> locationItems;

	// ?? TODO
	private Collection<Competence> selectedCompetences;
	private List<Competence> competences;
	private List<SelectItem> competenceItems;

	private Collection<Group> selectedGroups;

	private Calendar birthday;
	private Gender gender;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			systemUser = loadSystemUser(manager, sysUserId);

			// FIXME: Address is stored 'embedded' in the system-user table and
			// cannot be listed
			// addresses = manager.createQuery("from Address",
			// Address.class).getResultList();

			// logins = manager.createQuery("from Login",
			// Login.class).getResultList();
			locations = manager.createQuery("from Location", Location.class).getResultList();

			// loginItems = new ArrayList<SelectItem>();
			// for (Login login : logins) {
			// loginItems.add(new SelectItem(login.getAlias()));
			// }

			// addressItems = new ArrayList<SelectItem>();
			// for (Address address : addresses) {
			// addressItems.add(new SelectItem(address.getStreet() + " " +
			// address.getZipCode()
			// + " " + address.getCity()));
			// }

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
		// systemUser.setAddress(lookupAddress(selectedAddress));
		// systemUser.setCompetences(selectedCompetences);
		// systemUser.setGroups(selectedGroups);
		// systemUser.setLogin(lookupLogin(selectedLogin));
		systemUser.setLocation(lookupLocation(selectedLocation));

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
		selectedAddress = systemUser.getAddress().getStreet() + " "
				+ systemUser.getAddress().getZipCode() + " " + systemUser.getAddress().getCity();
		selectedCompetences = systemUser.getCompetences();
		selectedGroups = systemUser.getGroups();
		selectedLogin = systemUser.getLogin().getAlias();
		selectedLocation = systemUser.getLocation().getName();
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

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setSelectedLogin(String selectedLogin) {
		this.selectedLogin = selectedLogin;
	}

	public void setSelectedAddress(String selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
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

	public String getSelectedLogin() {
		return selectedLogin;
	}

	public String getSelectedAddress() {
		return selectedAddress;
	}

	public List<SelectItem> getLoginItems() {
		return loginItems;
	}

	public List<SelectItem> getAddressItems() {
		return addressItems;
	}

	public List<SelectItem> getLocationItems() {
		return addressItems;
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
