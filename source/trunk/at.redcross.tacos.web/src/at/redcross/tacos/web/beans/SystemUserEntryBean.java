package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Gender;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.CompetenceHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.faces.combo.DropDownItem;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;

@KeepAlive
@Component
@ManagedBean(name = "systemUserEntryBean")
public class SystemUserEntryBean extends BaseBean {

	private static final long serialVersionUID = -8941798819713448843L;

	/** the request parameter */
	private long userId = -1;

	/** the entities to manage */
	private SystemUser systemUser;
	private Login login;

	/** the values for the drop down fields */
	private List<SelectItem> genderItems;
	private List<SelectItem> competenceItems;
	private List<SelectItem> locationItems;

	/** passwords -> not directly attached to entity */
	private String password;
	private String password2;

	/** Encode passwords using SHA */
	private PasswordEncoder encoder = new ShaPasswordEncoder(256);

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, userId);
			competenceItems = DropDownHelper.convertToItems(CompetenceHelper.list(manager));
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			genderItems = new ArrayList<SelectItem>();
			genderItems.add(new DropDownItem("männlich", Gender.MALE).getItem());
			genderItems.add(new DropDownItem("weiblich", Gender.FEMALE).getItem());
			genderItems.add(new DropDownItem("unbekannt", Gender.UNKNOWN).getItem());
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
		EntityManager manager = null;
		try {
			// update the password if required
			password = StringUtils.saveString(password);
			password2 = StringUtils.saveString(password2);
			if (!password.isEmpty() && !password2.isEmpty()) {
				login.setPassword(encoder.encodePassword(password, null));
			}
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(login);
				manager.persist(systemUser);
			}
			else {
				manager.merge(login);
				manager.merge(systemUser);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("admin-listUsersView");
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
			loadfromDatabase(manager, systemUser.getId());
			return FacesUtils.pretty("admin-editUserView");
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
	private void loadfromDatabase(EntityManager manager, long id) {
		systemUser = manager.find(SystemUser.class, id);
		if (systemUser == null) {
			userId = -1;
			systemUser = new SystemUser();
			systemUser.setAddress(new Address());
			login = new Login();
			systemUser.setLogin(login);
			login.setSystemUser(systemUser);
		}
		login = systemUser.getLogin();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return userId == -1;
	}

	public long getUserId() {
		return userId;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public List<SelectItem> getCompetenceItems() {
		return competenceItems;
	}

	public List<SelectItem> getGenderItems() {
		return genderItems;
	}

	public Login getLogin() {
		return login;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public String getPassword() {
		return "";
	}

	public String getPassword2() {
		return "";
	}
}
