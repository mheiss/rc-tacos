package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.CarCareEntry;
import at.redcross.tacos.dbal.helper.CarCareEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "carCareEntryMaintenanceBean")
public class CarCareEntryMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 7433415254202431575L;

	private final static Log logger = LogFactory.getLog(CarCareEntryMaintenanceBean.class);

	/** the available carCareEntries */
	private List<GenericDto<CarCareEntry>> carCareEntries;

	/** the id of the selected carCareEntry */
	private long carCareEntryId;
	
	/** the id of the car - the request parameter */
	private long carId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			System.out.println("carId in der init der CarCareEntryMaintenanceBean" +carId);
			manager = EntityManagerFactory.createEntityManager();
			carCareEntries = DtoHelper.fromList(CarCareEntry.class, CarCareEntryHelper.list(manager,carId));
			System.out.println("carCareEntries: " +carCareEntries.size());
		} finally {
			System.out.println("close......--------------------------------");
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
		System.out.println("add ......................................");
		GenericDto<CarCareEntry> dto = new GenericDto<CarCareEntry>(new CarCareEntry());
		dto.setState(EntryState.NEW);
		//TODO dto.getEntity().setCar(car)
		
		carCareEntries.add(dto);
	}

	public void saveCarCareEntrys() {
		EntityManager manager = null;
		try {
			System.out.println("save 1");
			manager = EntityManagerFactory.createEntityManager();
			System.out.println("save 2");
			DtoHelper.syncronize(manager, carCareEntries);
			System.out.println("save 3");
			EntityManagerHelper.commit(manager);
			System.out.println("save 4");
			DtoHelper.filter(carCareEntries);
			System.out.println("save 5");
		} catch (Exception ex) {
			logger.error("Failed to persist carCareEntries", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			System.out.println("save 6");
			EntityManagerHelper.close(manager);
			System.out.println("save 7");
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setCarCareEntryId(long carCareEntryId) {
		this.carCareEntryId = carCareEntryId;
	}
	
	public void setCarId(long carId){
		System.out.println("in setCarId der CarCareEntrymaintenanceBean: " +carId);
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
		System.out.println("in getCarId der CarCareEntrymaintenanceBean: " +carId);
		return carId;
	}
}
