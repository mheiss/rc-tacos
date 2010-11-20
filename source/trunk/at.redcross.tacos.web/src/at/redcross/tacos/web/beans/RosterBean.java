package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

public abstract class RosterBean extends PagingBean {

    private static final long serialVersionUID = 4823260653983742860L;

    /** the id of the selected roster entry */
    protected long entryId;

    /** query result */
    protected List<RosterDto> entries;

    /** the available locations */
    protected List<Location> locations;

    /** filter by location name */
    protected String locationName = "*";

    /** filter by location */
    protected List<SelectItem> locationItems;

    /** filter by date */
    protected Date date;

    // ---------------------------------
    // Initialization
    // ---------------------------------
    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            // query data from the database
            manager = EntityManagerFactory.createEntityManager();
            date = getInitialDate();
            locations = LocationHelper.list(manager);
            locationItems = SelectableItemHelper.convertToItems(locations);
            init(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public void createPdfReport(ActionEvent event) {
        try {
            // define the parameters for the report
            ReportRenderParameters params = getReportParams();

            // render the report
            ReportRenderer.getInstance().renderReport(params);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Failed to create report");
        }
    }

    public void filterChanged(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            entries = getEntries(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void tabChanged(ValueChangeEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            entries = getEntries(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Roster API
    // ---------------------------------
    /** Loads the roster entries using the query parameters */
    protected List<RosterDto> getEntries(EntityManager manager) {
        return RosterDto.fromList(RosterEntryHelper.list(manager, getQueryParams()));
    }

    /** Returns a first location that matches the given name */
    protected Location getLocationByName(String locationName) {
        if (locationName == null || "*".equals(locationName)) {
            return null;
        }
        for (Location location : locations) {
            if (location.getName().equals(locationName)) {
                return location;
            }
        }
        return null;
    }

    /** Returns a filtered list of entries that are not deleted */
    protected List<RosterEntry> getFilteredEntries() {
        List<RosterEntry> list = new ArrayList<RosterEntry>();
        for (RosterDto dto : entries) {
            if (dto.getEntity().isToDelete()) {
                continue;
            }
            list.add(dto.getEntity());
        }
        return list;
    }

    /** Additional initialization using the given manager */
    protected void init(EntityManager manager) {
        // do nothing by default
    }

    /** Gets the initial date value */
    protected abstract Date getInitialDate();

    /** Returns the parameters for the report generation */
    protected abstract ReportRenderParameters getReportParams();

    /** Returns the parameters for the query */
    protected abstract RosterQueryParam getQueryParams();

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getLocationName() {
        return locationName;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public Date getDate() {
        return date;
    }

    public List<RosterDto> getEntries() {
        return entries;
    }

    public long getEntryId() {
        return entryId;
    }

    public RosterDto getEntry() {
        for (RosterDto dto : entries) {
            if (dto.getEntity().getId() != entryId) {
                continue;
            }
            return dto;
        }
        return null;
    }

}
