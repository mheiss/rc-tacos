package at.redcross.tacos.web.beans;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.Gender;
import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.CompetenceHelper;
import at.redcross.tacos.dbal.helper.GroupHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.faces.combo.DropDownItem;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;

@KeepAlive
@Component
@ManagedBean(name = "userMaintenanceBean")
public class UserMaintenanceBean extends BaseBean {

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
	private List<SelectItem> groupItems;

	/** passwords -> not directly attached to entity */
	private String password;
	private String password2;

	/** selected values */
	private Group selectedGroup;
	private Competence selectedCompetence;

	private long selectedGroupId;
	private long selectedCompetenceId;

	/** Encode passwords using SHA */
	private transient PasswordEncoder encoder;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, userId);
			competenceItems = DropDownHelper.convertToItems(CompetenceHelper.list(manager));
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			groupItems = DropDownHelper.convertToItems(GroupHelper.list(manager));
			genderItems = new ArrayList<SelectItem>();
			genderItems.add(new DropDownItem("männlich", Gender.MALE).getItem());
			genderItems.add(new DropDownItem("weiblich", Gender.FEMALE).getItem());
			genderItems.add(new DropDownItem("unbekannt", Gender.UNKNOWN).getItem());
			encoder = new ShaPasswordEncoder(256);
			// filter duplicate elements
			filterDuplicate(groupItems, systemUser.getGroups());
			filterDuplicate(competenceItems, systemUser.getCompetences());
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

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
				//systemUser before login!
				manager.persist(systemUser);
				manager.persist(login);
				
			} else {
				manager.merge(login);
				manager.merge(systemUser);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("admin-userOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht gespeichert werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public String revert() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, systemUser.getId());
			return FacesUtils.pretty("admin-userEditMaintenance");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Mitarbeitereintrag konnte nicht zurückgesetzt werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void addGroup(ActionEvent event) {
		// silently ignore invalid requests
		if (selectedGroup == null) {
			return;
		}
		List<Group> groups = systemUser.getGroups();
		if (!groups.contains(selectedGroup)) {
			groups.add(selectedGroup);
		}
		filterDuplicate(groupItems, systemUser.getGroups());
		selectedGroup = null;
	}

	public void addCompetence(ActionEvent event) {
		// silently ignore invalid requests
		if (selectedCompetence == null) {
			return;
		}
		List<Competence> competences = systemUser.getCompetences();
		if (!competences.contains(selectedCompetence)) {
			competences.add(selectedCompetence);
		}
		filterDuplicate(competenceItems, systemUser.getCompetences());
		selectedCompetence = null;
	}

	public void removeGroup(ActionEvent event) {
		Iterator<Group> groupIter = systemUser.getGroups().iterator();
		while (groupIter.hasNext()) {
			Group group = groupIter.next();
			if (group.getId() == selectedGroupId) {
				groupIter.remove();
				groupItems.add(new DropDownItem(group).getItem());
			}
		}
	}

	public void removeCompetence(ActionEvent event) {
		Iterator<Competence> comptIter = systemUser.getCompetences().iterator();
		while (comptIter.hasNext()) {
			Competence competence = comptIter.next();
			if (competence.getId() == selectedCompetenceId) {
				comptIter.remove();
				competenceItems.add(new DropDownItem(competence).getItem());
			}
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

	/** Removes all elements from the source list that are in the target list */
	private void filterDuplicate(List<SelectItem> sourceList, Collection<? extends EntityImpl> targetList) {
		Iterator<SelectItem> iter = sourceList.iterator();
		while (iter.hasNext()) {
			DropDownItem dropDown = (DropDownItem) iter.next().getValue();
			EntityImpl impl = (EntityImpl) dropDown.getValue();
			if (targetList.contains(impl)) {
				iter.remove();
			}
		}
	}
	
	/**
	 * Returns whether or not the current authenticated user can edit (delete or change) a role
	 * entry. The following restrictions are considered:
	 * <ul>
	 * <li>Principal must have the permission to edit a role</li>
	 * </ul>
	 */
	public boolean isEditRoleEnabled() {
		// editing is allowed for principals with permission
		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditRole()) {
			return true;
		}
		// edit denied
		return false;
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

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public void setSelectedCompetence(Competence selectedCompetence) {
		this.selectedCompetence = selectedCompetence;
	}

	public void setSelectedCompetenceId(long selectedCompetenceId) {
		this.selectedCompetenceId = selectedCompetenceId;
	}

	public void setSelectedGroupId(long selectedGroupId) {
		this.selectedGroupId = selectedGroupId;
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

	public List<SelectItem> getGroupItems() {
		return groupItems;
	}

	public Login getLogin() {
		return login;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public Competence getSelectedCompetence() {
		return selectedCompetence;
	}

	public String getPassword() {
		return "";
	}

	public String getPassword2() {
		return "";
	}
}
