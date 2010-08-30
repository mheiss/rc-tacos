package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterReportParam;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

/** Provide base functions for all roster beans */
@KeepAlive
public abstract class RosterOverviewBean extends BaseBean {

	private static final long serialVersionUID = -63594513702881676L;

	/** the id of the selected roster entry */
	private long entryId;

	/** query result */
	protected List<RosterEntry> entries;

	/** the available locations */
	protected List<Location> locations;

	/** filter by location name */
	protected String locationName = "*";

	/** filter by location */
	protected List<SelectItem> locationItems;

	/** filter by delete flag */
	protected boolean showDeleted;

	/** filter by date */
	protected Date date;

	// ---------------------------------
	// Initialization
	// ---------------------------------
	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
			locations = LocationHelper.list(manager);
			locationItems = DropDownHelper.convertToItems(locations);
			entries = getEntries(manager, getParamForQuery());
		} finally {
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
			entries = getEntries(manager, getParamForQuery());
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void tabChanged(ValueChangeEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			entries = getEntries(manager, getParamForQuery());
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

	public void navigateNext(ActionEvent event) {
		EntityManager manager = null;
		try {
			if (date == null) {
				date = new Date();
			}
			date = getNextDate(date);
			manager = EntityManagerFactory.createEntityManager();
			entries = getEntries(manager, getParamForQuery());
		} finally {
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
			entries = getEntries(manager, getParamForQuery());
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void markToDelete(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			Iterator<RosterEntry> iter = entries.iterator();
			while (iter.hasNext()) {
				RosterEntry entry = iter.next();
				if (entry.getId() != entryId) {
					continue;
				}
				entry.setToDelete(true);
				manager.merge(entry);
				iter.remove();
			}
			EntityManagerHelper.commit(manager);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void signIn(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			for (RosterEntry entry : entries) {
				if (entry.getId() != entryId) {
					continue;
				}
				Date now = Calendar.getInstance().getTime();
				entry.setRealStartDate(now);
				entry.setRealStartTime(now);
				manager.merge(entry);
			}
			EntityManagerHelper.commit(manager);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public void signOut(ActionEvent event) {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			for (RosterEntry entry : entries) {
				if (entry.getId() != entryId) {
					continue;
				}
				Date now = Calendar.getInstance().getTime();
				entry.setRealEndDate(now);
				entry.setRealEndTime(now);
				manager.merge(entry);
			}
			EntityManagerHelper.commit(manager);
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gelöscht werden");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/** Loads the roster entries using the given filter parameters */
	protected abstract List<RosterEntry> getEntries(EntityManager manager, RosterQueryParam param);

	/** Returns the parameters for the report generation */
	protected abstract ReportRenderParameters getReportParams();

	/** Increments the current date by the requested amount */
	protected abstract Date getNextDate(Date date);

	/** Decrements the current date by the requested amount */
	protected abstract Date getPreviousDate(Date date);

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	protected List<RosterReportParam> getParamForReport() {
		List<RosterReportParam> params = new ArrayList<RosterReportParam>();
		for (RosterEntry entry : entries) {
			params.add(new RosterReportParam(entry));
		}
		return params;
	}

	protected RosterQueryParam getParamForQuery() {
		RosterQueryParam param = new RosterQueryParam();
		param.date = date;
		param.location = getLocationByName(locationName);
		param.toDelete = showDeleted;
		return param;
	}

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

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setEntryId(long entryId) {
		this.entryId = entryId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setShowDeleted(boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getEntryId() {
		return entryId;
	}

	public Date getDate() {
		return date;
	}

	public boolean isShowDeleted() {
		return showDeleted;
	}

	public String getLocationName() {
		return locationName;
	}

	public List<RosterEntry> getEntries() {
		return entries;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}
}
