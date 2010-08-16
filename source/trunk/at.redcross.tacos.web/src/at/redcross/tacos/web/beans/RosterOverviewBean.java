package at.redcross.tacos.web.beans;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.LocationRosterEntry;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

/** Provide base functions for all roster beans */
public abstract class RosterOverviewBean extends BaseBean {

    private static final long serialVersionUID = -63594513702881676L;

    /** the id of the selected roster entry */
    private long entryId;
    
    /* the entry to remove*/
    private RosterEntry rosterEntry;
    
    // filter by location
    protected Location location;
    protected List<SelectItem> locationItems;

    // filter by date
    protected Date date;

    // queried result
    protected List<RosterEntry> entries;
    protected List<LocationRosterEntry> locationEntry;

    // ---------------------------------
    // Initialization
    // ---------------------------------
    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
            locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
            loadfromDatabase(manager, location, date);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void filterChanged(ActionEvent event) {
        EntityManager manager = null;
        try {
            // provide default value if date is null
            if (date == null) {
                date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
            }
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, location, date);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void createPdfReport(ActionEvent event) {
        try {
            // define the parameters for the report
            ReportRenderParameters params = getReportParams();

            // render the report
            ReportRenderer.getInstance().renderReport(params);
        }
        catch (Exception e) {
            FacesUtils.addErrorMessage("Failed to create report");
        }
    }

    public void navigateNext(ActionEvent event) {
        EntityManager manager = null;
        try {
            if (date == null) {
                date = new Date();
            }
            date = getNextDate(date);
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, location, date);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void navigatePrevious(ActionEvent event) {
        EntityManager manager = null;
        try {
            if (date == null) {
                date = new Date();
            }
            date = getPreviousDate(date);
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, location, date);
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
    
    public String markToDelete(ActionEvent event) {	
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, entryId);
			rosterEntry.setToDelete(true);
			manager.merge(rosterEntry);
			EntityManagerHelper.commit(manager);
			loadfromDatabase(manager, location, date);
			return FacesUtils.pretty("roster-dayOverview");
		}
		catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
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
		rosterEntry = manager.find(RosterEntry.class, id);
		if (rosterEntry == null) {
			entryId = -1;
			rosterEntry = new RosterEntry();
		}
	}
	

    /** Loads the roster entries using the given filter parameters */
    protected abstract List<RosterEntry> getEntries(EntityManager manager, Location location, Date date);

    /** Returns the parameters for the report generation */
    protected abstract ReportRenderParameters getReportParams();

    /** Increments the current date by the requested amount */
    protected abstract Date getNextDate(Date date);

    /** Decrements the current date by the requested amount */
    protected abstract Date getPreviousDate(Date date);

    // ---------------------------------
    // Private API
    // ---------------------------------
    protected void loadfromDatabase(EntityManager manager, Location filterLocation, Date date) {
        System.out.println("in load from database man loc date");
    	// build a structure containing all results grouped by locations
        entries = new ArrayList<RosterEntry>();
        Map<Location, List<RosterEntry>> mappedResult = new HashMap<Location, List<RosterEntry>>();
        for (RosterEntry entry : getEntries(manager, filterLocation, date)) {
            entries.add(entry);

            Location location = entry.getLocation();
            List<RosterEntry> list = mappedResult.get(location);
            if (list == null) {
                list = new ArrayList<RosterEntry>();
                mappedResult.put(location, list);
            }
            list.add(entry);
        }
        // map this structure again for visualization
        locationEntry = new ArrayList<LocationRosterEntry>();
        for (Map.Entry<Location, List<RosterEntry>> entry : mappedResult.entrySet()) {
            LocationRosterEntry value = new LocationRosterEntry(entry.getKey(), entry.getValue());
            locationEntry.add(value);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getEntryId(){
    	return entryId;
    }
    
    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public List<RosterEntry> getEntries() {
        return entries;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public List<LocationRosterEntry> getLocationEntry() {
        return locationEntry;
    }
}
