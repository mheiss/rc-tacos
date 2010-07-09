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
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@Component
@ManagedBean(name = "carEntryBean")
public class CarEntryBean extends BaseBean {
	
	private static final long serialVersionUID = 464932680405482944L;

	/** the request parameter */
	private long carId = -1;

	/** the entities to manage */
	private Car car;

	/** the values for the drop down fields */
	private List<SelectItem> locationItems;

	
	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, carId);
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
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
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(car);
			}
			else {
				manager.merge(car);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("admin-listCarsView");
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Fahrzeugeintrag konnte nicht gespeichert werden");
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
			loadfromDatabase(manager, car.getId());
			return FacesUtils.pretty("admin-editCarView");
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Fahrzeugeintrag konnte nicht zurückgesetzt werden");
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
		this.carId = carId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return carId == -1;
	}
	
	public long getCarId() {
		return carId;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}


	public Car getCar() {
		return car;
	}
}
