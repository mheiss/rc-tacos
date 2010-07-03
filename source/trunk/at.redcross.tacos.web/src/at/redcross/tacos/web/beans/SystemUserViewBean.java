package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "systemUserViewBean")
public class SystemUserViewBean extends BaseBean {

	private static final long serialVersionUID = -5114023802685654841L;

	/** all available locations */
	private List<Location> locations;

	/** the active location */
	private String locationId;

	/** the system users for the location */
	private List<SystemUser> users;

	/** the paging */
	private int page = 0;
	private int maxResults = 30;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			locations = LocationHelper.list(manager);
			users = SystemUserHelper.list(manager);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setPage(int page) {
		this.page = page;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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

	public String getLocationId() {
		return locationId;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public int getPage() {
		return page;
	}

}
