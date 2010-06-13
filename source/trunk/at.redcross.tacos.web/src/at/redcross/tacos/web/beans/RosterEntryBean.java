package at.redcross.tacos.web.beans;

import java.util.Date;
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
import at.redcross.tacos.web.utils.DateUtils;

@KeepAlive
@ManagedBean(name = "rosterEntryBean")
public class RosterEntryBean extends BaseBean {

	private static final long serialVersionUID = 3440196753805921232L;

	// the entry to create or edit
	private long rosterId = -1;
	private RosterEntry rosterEntry;

	// the suggested values for the drop down boxes
	private List<SelectItem> userItems;
	private List<SelectItem> locationItems;
	private List<SelectItem> serviceTypeItems;
	private List<SelectItem> assignmentItems;

	// start and end time
	private Date plannedStartTime;
	private Date plannedStartDate;

	private Date plannedEndTime;
	private Date plannedEndDate;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, rosterId);
			// fetch values for the drop-down components on the page
			userItems = DropDownHelper.convertToItems(SystemUserHelper.list(manager));
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			serviceTypeItems = DropDownHelper.convertToItems(ServiceTypeHelper.list(manager));
			assignmentItems = DropDownHelper.convertToItems(AssignmentHelper.list(manager));
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
		// merge the separate date and time values
		rosterEntry.setPlannedStart(DateUtils.mergeDateAndTime(plannedStartDate, plannedStartTime));
		rosterEntry.setPlannedEnd(DateUtils.mergeDateAndTime(plannedEndDate, plannedEndTime));

		// write to the database
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(rosterEntry);
			}
			else {
				manager.merge(rosterEntry);
			}
			EntityManagerHelper.commit(manager);
			return "pretty:roster-dayView";
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gespeichert werden");
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
			loadfromDatabase(manager, rosterEntry.getId());
			return "pretty:roster-edit";
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht zurückgesetzt werden");
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
		rosterEntry = manager.find(RosterEntry.class, id);
		if (rosterEntry == null) {
			rosterId = -1;
			rosterEntry = new RosterEntry();
		}
		plannedStartDate = rosterEntry.getPlannedStart();
		plannedStartTime = rosterEntry.getPlannedStart();
		plannedEndDate = rosterEntry.getPlannedEnd();
		plannedEndTime = rosterEntry.getPlannedEnd();
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

	public void setPlannedStartTime(Date plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public void setPlannedEndTime(Date plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
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

	public Date getPlannedStartTime() {
		return plannedStartTime;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public Date getPlannedEndTime() {
		return plannedEndTime;
	}

	public RosterEntry getRosterEntry() {
		return rosterEntry;
	}
}
