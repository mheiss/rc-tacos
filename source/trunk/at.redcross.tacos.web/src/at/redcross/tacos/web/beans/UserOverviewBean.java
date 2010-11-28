package at.redcross.tacos.web.beans;

import java.util.ArrayList;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "userOverviewBean")
public class UserOverviewBean extends PagingBean {

	private static final long serialVersionUID = -5114023802685654841L;

	/** available locations */
	private List<Location> locations;

	/** filter by user status */
	private boolean locked;

	/** the values for the drop down fields */
	private List<SelectItem> stateItems;

	/** active location */
	private String locationName = "*";

	/** queried results for visualization / reporting */
	private List<GenericDto<SystemUser>> users;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			locations = LocationHelper.list(manager);
			stateItems = new ArrayList<SelectItem>();
			stateItems.add(new SelectableItem("nicht gesperrt", false).getItem());
			stateItems.add(new SelectableItem("gesperrt", true).getItem());
			// load not locked users
			loadfromDatabase(manager, getLocationByName(locationName), locked);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	public void tabChanged(ValueChangeEvent event) {
		EntityManager manager = null;
		try {
			page = 1;
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, getLocationByName(locationName), locked);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void filterChanged(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, getLocationByName(locationName), locked);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Private API
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, String locationId, boolean locked) {
		users = DtoHelper.fromList(SystemUser.class, SystemUserHelper.listByLocationName(manager,
				locationId, locked));
	}

	private String getLocationByName(String locationName) {
		if (locationName == null || "*".equals(locationName)) {
			return null;
		}
		for (Location location : locations) {
			if (location.getName().equals(locationName)) {
				return location.getName();
			}
		}
		return null;
	}

	/**
	 * Returns whether or not the current authenticated user can edit (delete or
	 * change) a user entry. The following restrictions are considered:
	 * <ul>
	 * <li>Principal must have the permission to edit a role</li>
	 * </ul>
	 */
	public boolean isEditUserEnabled() {
		// editing is allowed for principals with permission
		if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditUser()) {
			return true;
		}
		// edit denied
		return false;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public List<Location> getLocations() {
		return locations;
	}

	public List<GenericDto<SystemUser>> getUsers() {
		return users;
	}

	public String getLocationName() {
		return locationName;
	}

	public boolean getLocked() {
		return locked;
	}

	public List<SelectItem> getStateItems() {
		return stateItems;
	}
}
