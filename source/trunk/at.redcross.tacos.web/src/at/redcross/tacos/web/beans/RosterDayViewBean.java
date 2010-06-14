package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.entity.LocationRosterEntry;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.utils.DateUtils;

@KeepAlive
@ManagedBean(name = "rosterDayViewBean")
public class RosterDayViewBean extends BaseBean {

	private static final long serialVersionUID = 8817078489086816724L;

	// filter by location
	private Location location;
	private List<SelectItem> locationItems;

	// filter by date
	private Date date;

	// queried result
	private List<LocationRosterEntry> locationEntry;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			date = DateUtils.getCalendar(System.currentTimeMillis()).getTime();
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
			if(date == null) {
				date = DateUtils.getCalendar(System.currentTimeMillis()).getTime();
			}
			manager = EntityManagerFactory.createEntityManager();
			loadfromDatabase(manager, location, date);
		}
		finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Private API
	// ---------------------------------
	private void loadfromDatabase(EntityManager manager, Location filterLocation, Date date) {
		// fetch all entries - optionally with location filter
		StringBuilder builder = new StringBuilder();
		builder.append(" select entry from RosterEntry entry ");
		builder.append(" left join entry.location as location ");
		builder.append(" join fetch entry.systemUser ");
		builder.append(" where day(entry.plannedEnd) >= :day AND day(entry.plannedStart) <= :day");
		builder
				.append(" and month(entry.plannedEnd) >= :month AND month(entry.plannedStart) <= :month");
		builder
				.append(" and year(entry.plannedEnd) >= :year AND year(entry.plannedStart) <= :year");
		if (filterLocation != null) {
			builder.append(" and location.id=:locationId");
		}
		TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
		{
			Calendar cal = DateUtils.getCalendar(date.getTime());
			query.setParameter("day", cal.get(Calendar.DAY_OF_MONTH));
			query.setParameter("month", cal.get(Calendar.MONTH) + 1);
			query.setParameter("year", cal.get(Calendar.YEAR));
		}
		if (filterLocation != null) {
			query.setParameter("locationId", filterLocation.getId());
		}

		// build a structure containing all results grouped by locations
		Map<Location, List<RosterEntry>> mappedResult = new HashMap<Location, List<RosterEntry>>();
		for (RosterEntry entry : query.getResultList()) {
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
	public void setDate(Date date) {
		this.date = date;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public Date getDate() {
		return date;
	}

	public Location getLocation() {
		return location;
	}

	public List<SelectItem> getLocationItems() {
		return locationItems;
	}

	public List<LocationRosterEntry> getLocationEntry() {
		return locationEntry;
	}
}
