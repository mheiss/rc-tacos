package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.helper.CarHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "vehicleOverviewBean")
public class VehicleOverviewBean extends PagingBean {

    private static final long serialVersionUID = 5527463274271756151L;

    /** available locations */
    private List<Location> locations;

    /** active location */
    private String locationName = "*";

    /** queried results for visualization / reporting */
    private List<Car> cars;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            locations = LocationHelper.list(manager);
            loadfromDatabase(manager, getLocationByName(locationName));
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
            loadfromDatabase(manager, getLocationByName(locationName));
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Private API
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager, String locationId) {
        cars = CarHelper.listByLocationName(manager, locationId);
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
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<Location> getLocations() {
        return locations;
    }

    public List<Car> getCars() {
        return cars;
    }

    public String getLocationName() {
        return locationName;
    }
}
