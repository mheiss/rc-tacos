package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.stereotype.Component;

import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.EntityUtils;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@Component
@ManagedBean(name = "vehicleMaintenanceBean")
public class VehicleMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 464932680405482944L;

	/** the request parameter */
	private long carId = -1;

	/** the entities to manage */
	private Car car;

	/** the values for the drop down fields */
	private List<SelectItem> locationItems;

	// the maximum length of the information
	private int maxDescLength = -1;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			System.out.println("carId in der init der VehicleMaintenanceBean: " + carId);
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, carId);
			if (!FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditVehicle()) {
				FacesUtils.redirectAccessDenied("Entry '" + car + "' cannot be edited");
				return;
			}
			locationItems = SelectableItemHelper.convertToItems(LocationHelper.list(manager));
			maxDescLength = EntityUtils.getColumnLength(Car.class, "notes");
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
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(car);
			} else {
				manager.merge(car);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("info-vehicleOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Fahrzeugeintrag konnte nicht gespeichert werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public String revert() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, car.getId());
			return FacesUtils.pretty("info-vehicleEditMaintenance");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Fahrzeugeintrag konnte nicht zurückgesetzt werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, long id) {
		car = manager.find(Car.class, id);
		if (car == null) {
			carId = -1;
			car = new Car();
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setCarId(long carId) {
		System.out.println("in setCarId in der VehicleMaintenanceBean: " + carId);
		this.carId = carId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return carId == -1;
	}

	public long getCarId() {
		System.out.println("in getCarId in der VehicleMaintenanceBean: " + carId);
		return carId;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public Car getCar() {
		return car;
	}

	public int getMaxDescLength() {
		return maxDescLength;
	}
}
