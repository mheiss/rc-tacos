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
import at.redcross.tacos.web.faces.combo.DropDownItem;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "userOverviewBean")
public class UserOverviewBean extends BaseBean {

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
	private List<SystemUser> users;

	/** paging */
	private int page = 1;
	private int maxResults = 30;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			locations = LocationHelper.list(manager);
			stateItems = new ArrayList<SelectItem>();
			stateItems.add(new DropDownItem("nicht gesperrt", false).getItem());
			stateItems.add(new DropDownItem("gesperrt", true).getItem());
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
		users = SystemUserHelper.listByLocationName(manager, locationId, locked);
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

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setPage(int page) {
		this.page = page;
	}

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

	public List<SystemUser> getUsers() {
		return users;
	}

	public String getLocationName() {
		return locationName;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public int getPage() {
		return page;
	}

	public boolean getLocked() {
		return locked;
	}

	public List<SelectItem> getStateItems() {
		return stateItems;
	}
}
