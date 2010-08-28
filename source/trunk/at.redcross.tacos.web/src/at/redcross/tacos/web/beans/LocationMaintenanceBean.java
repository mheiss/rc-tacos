package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "locationMaintenanceBean")
public class LocationMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = -6150402647997947217L;

	private final static Log logger = LogFactory.getLog(LocationMaintenanceBean.class);

	/** the available locations */
	private List<GenericDto<Location>> locations;

	/** the id of the selected location */
	private long locationId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			locations = DtoHelper.fromList(Location.class, LocationHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

	public void removeLocation(ActionEvent event) {
		Iterator<GenericDto<Location>> iter = locations.iterator();
		while (iter.hasNext()) {
			GenericDto<Location> dto = iter.next();
			Location location = dto.getEntity();
			if (location.getId() != locationId) {
				continue;
			}
			if (dto.getState() == DtoState.NEW) {
				iter.remove();
			}

			dto.setState(DtoState.DELETE);
		}
	}

	public void unremoveLocation(ActionEvent event) {
		for (GenericDto<Location> dto : locations) {
			Location location = dto.getEntity();
			if (location.getId() != locationId) {
				continue;
			}
			dto.setState(DtoState.SYNC);
		}
	}

	public void addLocation(ActionEvent event) {
		GenericDto<Location> dto = new GenericDto<Location>(new Location());
		dto.setState(DtoState.NEW);
		locations.add(dto);
	}

	public void saveLocations() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, locations);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(locations);
		} catch (Exception ex) {
			logger.error("Failed to remove location '" + locationId + "'", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getLocationId() {
		return locationId;
	}

	public List<GenericDto<Location>> getLocations() {
		return locations;
	}
}
