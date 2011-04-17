package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.stereotype.Component;

import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.entity.CarCareEntry;
import at.redcross.tacos.dbal.helper.CarCareEntryHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.EntityUtils;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@Component
@ManagedBean(name = "vehicleMaintenanceBean")
public class VehicleMaintenanceBean extends BaseBean {

    private static final long serialVersionUID = 464932680405482944L;

    /** the request parameter */
    private long carId = -1;
    private long carCareId;

    /** the entities to manage */
    private Car car;

    /** the available carCareEntries */
    private List<GenericDto<CarCareEntry>> carCareEntries;

    /** the values for the drop down fields */
    private List<SelectItem> locationItems;

    /** the suggested values for the drop down boxes */
    private List<SelectItem> statusItems = new ArrayList<SelectItem>();

    // the maximum length of the information
    private int maxDescLength = -1;

    @Override
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, carId);
            if (!FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditVehicle()) {
                FacesUtils.redirectAccessDenied("Entry '" + car + "' cannot be edited");
                return;
            }
            locationItems = SelectableItemHelper.convertToItems(LocationHelper.list(manager));
            maxDescLength = EntityUtils.getColumnLength(Car.class, "notes");
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
    public void apply(ActionEvent event) {
        saveChanges();
    }

    public String persist() {
        boolean success = saveChanges();
        if (success) {
            return FacesUtils.pretty("info-vehicleOverview");
        }
        return null;
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

    public void removeCarCareEntry(ActionEvent event) {
        Iterator<GenericDto<CarCareEntry>> iter = carCareEntries.iterator();
        while (iter.hasNext()) {
            GenericDto<CarCareEntry> dto = iter.next();
            CarCareEntry carCareEntry = dto.getEntity();
            if (carCareEntry.getId() != carCareId) {
                continue;
            }
            if (dto.getState() == EntryState.NEW) {
                iter.remove();
            }
            dto.setState(EntryState.DELETE);
            return;
        }
    }

    public void unremoveCarCareEntry(ActionEvent event) {
        for (GenericDto<CarCareEntry> dto : carCareEntries) {
            CarCareEntry carCareEntry = dto.getEntity();
            if (carCareEntry.getId() != carCareId) {
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

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager, long id) {
        car = manager.find(Car.class, id);
        if (car == null) {
            carId = -1;
            car = new Car();
        }
        carCareEntries = DtoHelper.fromList(CarCareEntry.class, CarCareEntryHelper.list(manager,
                carId));
    }

    private boolean saveChanges() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            if (isNew()) {
                manager.persist(car);
            } else {
                manager.merge(car);
            }
            DtoHelper.syncronize(manager, carCareEntries);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(carCareEntries);
            return true;
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Fahrzeugeintrag konnte nicht gespeichert werden");
            return false;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setCarId(long carId) {
        this.carId = carId;
    }

    public void setCarCareId(long carCareId) {
        this.carCareId = carCareId;
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

    public long getCarCareId() {
        return carCareId;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public Car getCar() {
        return car;
    }

    public List<GenericDto<CarCareEntry>> getCarCareEntries() {
        return carCareEntries;
    }

    public List<SelectItem> getStatusItems() {
        return statusItems;
    }

    public int getMaxDescLength() {
        return maxDescLength;
    }
}
