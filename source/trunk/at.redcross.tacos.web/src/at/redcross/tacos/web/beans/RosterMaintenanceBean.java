package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "rosterMaintenanceBean")
public class RosterMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 3440196753805921232L;

	// the entry to create or edit
	private long rosterId = -1;
	private RosterEntry rosterEntry;

	// the suggested values for the drop down boxes
	private List<SelectItem> userItems;
	private List<SelectItem> locationItems;
	private List<SelectItem> serviceTypeItems;
	private List<SelectItem> assignmentItems;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, rosterId);
			userItems = DropDownHelper.convertToItems(SystemUserHelper.list(manager));
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			serviceTypeItems = DropDownHelper.convertToItems(ServiceTypeHelper.list(manager));
			assignmentItems = DropDownHelper.convertToItems(AssignmentHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	@Action
	public String persist() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(rosterEntry);
			} else {
				manager.merge(rosterEntry);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("roster-dayOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gespeichert werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	@Action
	public String revert() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, rosterEntry.getId());
			return FacesUtils.pretty("roster-editMaintenance");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht zurückgesetzt werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, long id) {
		rosterEntry = manager.find(RosterEntry.class, id);
		if (rosterEntry == null) {
			rosterId = -1;
			rosterEntry = new RosterEntry();
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public long getRosterId() {
		return rosterId;
	}

	public void setRosterId(long rosterId) {
		this.rosterId = rosterId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return rosterId == -1;
	}

	public List<SelectItem> getUserItems() {
		return userItems;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public List<SelectItem> getServiceTypeItems() {
		return serviceTypeItems;
	}

	public List<SelectItem> getAssignmentItems() {
		return assignmentItems;
	}

	public RosterEntry getRosterEntry() {
		return rosterEntry;
	}
}
