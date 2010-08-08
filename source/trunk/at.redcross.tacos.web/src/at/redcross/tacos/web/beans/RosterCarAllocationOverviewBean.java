package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.CarHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterCarAllocationOverviewBean")
public class RosterCarAllocationOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = -6923595116203096939L;

	// the entry to edit
	private long rosterId = -1;
	private RosterEntry rosterEntry;

	// filter by date
	protected Date date;

	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	// the suggested values for the drop down boxes
	private List<SelectItem> carItems;
	private List<SelectItem> locationItems;

	@Override
	public void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			GregorianCalendar cal = new GregorianCalendar();
			date = cal.getTime();
			locationItems = DropDownHelper.convertToItems(LocationHelper.list(manager));
			carItems = DropDownHelper.convertToItems(CarHelper.list(manager, false));
			loadfromDatabase(manager, location, date);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	/**
	 * Persists the current entity in the database
	 */
	public void saveEntries() {
		for (RosterEntry entry : entries) {
			System.out.println("Einträge car:" + entry.getCar().getName());
			System.out.println("Einträge name:" + entry.getSystemUser().getLastName());
			EntityManager manager = null;
			try {
				manager = EntityManagerFactory.createEntityManager();
				manager.merge(entry);
				EntityManagerHelper.commit(manager);
			} catch (Exception ex) {
				FacesUtils.addErrorMessage("Die Fahrzeugzuweisung konnte nicht gespeichert werden");
			} finally {
				manager = EntityManagerHelper.close(manager);
			}
		}
	}

	public String persist() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			if (isNew()) {
				manager.persist(rosterEntry);
			} else {
				manager.merge(rosterEntry);
			}
			EntityManagerHelper.commit(manager);
			return FacesUtils.pretty("roster-assignCar");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Die Fahrzeugzuweisung konnte nicht gespeichert werden");
			return null;
		} finally {
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
			loadfromDatabase(manager, rosterEntry.getId());
			return FacesUtils.pretty("roster-rosterCarAllocationOverview");
		} catch (Exception ex) {
			FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht zurückgesetzt werden");
			return null;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, long id) {
		rosterEntry = manager.find(RosterEntry.class, id);
		if (rosterEntry == null) {
			rosterId = -1;
			rosterEntry = new RosterEntry();
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setRosterId(long rosterId) {
		this.rosterId = rosterId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isNew() {
		return rosterId == -1;
	}

	public long getRosterId() {
		return rosterId;
	}

	public List<SelectItem> getCarItems() {
		return carItems;
	}

	public RosterEntry getRosterEntry() {
		return rosterEntry;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	@Override
	protected Date getPreviousDate(Date date) {
		return DateUtils.addDays(date, -1);
	}

	@Override
	protected Date getNextDate(Date date) {
		return DateUtils.addDays(date, +1);
	}

	@Override
	protected List<RosterEntry> getEntries(EntityManager manager, Location location, Date date) {
		return RosterEntryHelper.listByDay(manager, location, date);
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Dienstplan_" + sdf.format(date) + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("rosterList", entries);
		params.arguments.put("reportDate", date);
		return params;
	}

}
