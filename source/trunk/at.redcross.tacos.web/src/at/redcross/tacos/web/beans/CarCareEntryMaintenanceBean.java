package at.redcross.tacos.web.beans;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.entity.CarCareEntry;
import at.redcross.tacos.dbal.helper.CarCareEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "carCareEntryMaintenanceBean")
public class CarCareEntryMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = -4905250662686238760L;

	private final static Log logger = LogFactory.getLog(CarCareEntryMaintenanceBean.class);

	/** the available carCareEntries */
	private List<GenericDto<CarCareEntry>> carCareEntries;

	/** the id of the selected carCareEntry */
	private long carCareEntryId;
	
	/** the id of the car - the request parameter */
	private long carId;
	
	/** the entities to manage */
	private Car car;

	// the suggested values for the drop down boxes
	private List<SelectItem> statusItems;
	
	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			carCareEntries = DtoHelper.fromList(CarCareEntry.class, CarCareEntryHelper.list(manager,carId));
			//manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, carId);
			statusItems = new ArrayList<SelectItem>();
			statusItems.add(new SelectableItem("offen", "offen").getItem());
			statusItems.add(new SelectableItem("in Bearbeitung", "in Bearbeitung").getItem());
			statusItems.add(new SelectableItem("abgeschlossen", "abgeschlossen").getItem());
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public void removeCarCareEntry(ActionEvent event) {
		Iterator<GenericDto<CarCareEntry>> iter = carCareEntries.iterator();
		while (iter.hasNext()) {
			GenericDto<CarCareEntry> dto = iter.next();
			CarCareEntry carCareEntry = dto.getEntity();
			if (carCareEntry.getId() != carCareEntryId) {
				continue;
			}
			if (dto.getState() == EntryState.NEW) {
				iter.remove();
			}
			dto.setState(EntryState.DELETE);
		}
	}

	public void unremoveCarCareEntry(ActionEvent event) {
		for (GenericDto<CarCareEntry> dto : carCareEntries) {
			CarCareEntry carCareEntry = dto.getEntity();
			if (carCareEntry.getId() != carCareEntryId) {
				continue;
			}
			dto.setState(EntryState.SYNC);
		}
	}

	public void addCarCareEntry(ActionEvent event) {
		GenericDto<CarCareEntry> dto = new GenericDto<CarCareEntry>(new CarCareEntry());
		dto.setState(EntryState.NEW);
		dto.getEntity().setCar(car);
		carCareEntries.add(dto);
	}

	public void saveCarCareEntries() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, carCareEntries);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(carCareEntries);
		} catch (Exception ex) {
			logger.error("Failed to persist carCareEntries", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
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
	public void setCarCareEntryId(long carCareEntryId) {
		this.carCareEntryId = carCareEntryId;
	}
	
	public void setCarId(long carId){
		this.carId = carId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getCarCareEntryId() {
		return carCareEntryId;
	}

	public List<GenericDto<CarCareEntry>> getCarCareEntries() {
		return carCareEntries;
	}
	
	public long getCarId(){
		return carId;
	}
	
	public Car getCar() {
		return car;
	}
	
	public List<SelectItem> getStatusItems() {
		return statusItems;
	}
}
