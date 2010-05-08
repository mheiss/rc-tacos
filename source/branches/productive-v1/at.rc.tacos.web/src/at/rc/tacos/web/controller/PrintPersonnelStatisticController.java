package at.rc.tacos.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.support.PropertyComparator;
import org.springframework.util.comparator.CompoundComparator;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.socket.WebClient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.container.AdminStatisticContainer;
import at.rc.tacos.web.container.Month;
import at.rc.tacos.web.container.RosterEntryContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Personal Statistic Controller
 * @author Payer Martin
 * @version 1.0
 */
public class PrintPersonnelStatisticController extends Controller {

	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	
	private static final String PARAM_MONTH_NAME = "month";
	private static final String MODEL_MONTH_NAME = "month";
	
	private static final String PARAM_YEAR_NAME = "year";
	private static final String MODEL_YEAR_NAME = "year";
	
	private static final String MODEL_ROSTER_MONTH_CONTAINER_NAME = "adminStatisticContainer";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		// Location
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		Location location = null;
		final Location defaultLocation = userSession.getDefaultFormValues().getPersonnelStatisticLocation();
		if (paramLocationId != null && !paramLocationId.equals("") && !paramLocationId.equals(PARAM_LOCATION_NO_VALUE)) {
			locationId = Integer.parseInt(paramLocationId);		
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
			final Location l2 = (Location)itLoactionList.next();
			if (l2.getId() == locationId) {
				location = l2;
			}
		}
		if (location != null || (paramLocationId != null && paramLocationId.equals(PARAM_LOCATION_NO_VALUE))) {
			params.put(MODEL_LOCATION_NAME, location);
		} else {
			params.put(MODEL_LOCATION_NAME, defaultLocation);
		}
		location = (Location)params.get(MODEL_LOCATION_NAME);
		
		// Month
		String paramMonth = request.getParameter(PARAM_MONTH_NAME);
		final Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (userSession.getDefaultFormValues().getPersonnelStatisticMonth() != null) {
			month = userSession.getDefaultFormValues().getPersonnelStatisticMonth();
		}
		if (paramMonth != null && !paramMonth.equals("")) {
			month = Month.valueOf(paramMonth).getProperty();
		}
		switch (month) {
			case 0:
				paramMonth = Month.JANUARY.toString();
				break;
			case 1:
				paramMonth = Month.FEBRUARY.toString();
				break;
			case 2:
				paramMonth = Month.MARCH.toString();
				break;
			case 3:
				paramMonth = Month.APRIL.toString();
				break;
			case 4:
				paramMonth = Month.MAY.toString();
				break;
			case 5:
				paramMonth = Month.JUNE.toString();
				break;
			case 6:
				paramMonth = Month.JULY.toString();
				break;
			case 7:
				paramMonth = Month.AUGUST.toString();
				break;
			case 8:
				paramMonth = Month.SEPTEMBER.toString();
				break;
			case 9:
				paramMonth = Month.OCTOBER.toString();
				break;
			case 10:
				paramMonth = Month.NOVEMBER.toString();
				break;
			case 11:
				paramMonth = Month.DECEMBER.toString();
				break;
		}
		params.put(MODEL_MONTH_NAME, paramMonth);
		
		// Year
		final Calendar yearCal = Calendar.getInstance();
		Integer year = yearCal.get(Calendar.YEAR);
		if (userSession.getDefaultFormValues().getPersonnelStatisticYear() != null) {
			year = userSession.getDefaultFormValues().getPersonnelStatisticYear();
		}
		Integer yearP = 0;
		final String yearParam = request.getParameter(PARAM_YEAR_NAME);
		if (yearParam != null && !yearParam.equals("")) {			
			yearP = Integer.valueOf(yearParam);
		}		
		final List<Integer> yearList = new ArrayList<Integer>();
		for (int i = -1; i <= 10; i++) {
			yearList.add(yearCal.get(Calendar.YEAR) - i);
			if (yearP == yearCal.get(Calendar.YEAR) - i) {
				year = yearP;
			}
		}
		params.put(MODEL_YEAR_NAME, year);

		
		// Staff Member (depends on function and location staff member filter)			
		StaffMember staffMember = userSession.getLoginInformation().getUserInformation();
		if (staffMember == null) {
			throw new IllegalArgumentException("Error: Staff Member must not be null.");
		}
		
		// Get Roster Entries
		final QueryFilter rosterFilter = new QueryFilter();
		if (location != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_FILTER, Integer.toString(month + 1));
		rosterFilter.add(IFilterTypes.ROSTER_YEAR_FILTER, year.toString());	
		rosterFilter.add(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER, Integer.toString(staffMember.getStaffMemberId()));	
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER, "true");
		
		// Form RosterEntryContainerList for Table
		final List<AbstractMessage> rosterEntryList = connection.sendListingRequest(RosterEntry.ID, rosterFilter);
		final List<RosterEntryContainer> rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
		if (!RosterEntry.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (Iterator<AbstractMessage> itRosterEntryList = rosterEntryList.iterator(); itRosterEntryList.hasNext();) {
			final RosterEntry rosterEntry = (RosterEntry)itRosterEntryList.next();
			final RosterEntryContainer rosterEntryContainer = new RosterEntryContainer();
			rosterEntryContainer.setRosterEntry(rosterEntry);
			final Calendar deadlineCalendar = Calendar.getInstance();
			deadlineCalendar.setTime(rosterEntryContainer.getPlannedStartOfWork());
			deadlineCalendar.set(Calendar.HOUR, deadlineCalendar.get(Calendar.HOUR) - RosterEntryContainer.EDIT_ROSTER_ENTRY_DEADLINE_HOURS);
			rosterEntryContainer.setDeadline(deadlineCalendar.getTime());
			
			final Calendar registerStartCalendar = Calendar.getInstance();
			registerStartCalendar.setTime(rosterEntryContainer.getPlannedStartOfWork());
			registerStartCalendar.set(Calendar.HOUR, registerStartCalendar.get(Calendar.HOUR) - RosterEntryContainer.REGISTER_ROSTER_ENTRY_DEADLINE_HOURS);
			rosterEntryContainer.setRegisterStart(registerStartCalendar.getTime());
			
			rosterEntryContainerList.add(rosterEntryContainer);
		}
		
		// Group and Sort
		// Create Comparators
		final Comparator staffMemberComparator = new CompoundComparator(new Comparator[] {
			new PropertyComparator("lastName", true, true),
			new PropertyComparator("firstName", true, true)
		});
		final Comparator sortComp = new CompoundComparator(new Comparator[] {
			new PropertyComparator("plannedStartOfWork", true, true)
		});
		
		// Create roster month container
		final AdminStatisticContainer adminStatisticContainer = new AdminStatisticContainer();
		
		// Set roster entry list
		adminStatisticContainer.setRosterEntryContainerList(rosterEntryContainerList);
		
		// Create timetable
		adminStatisticContainer.createTimetable(staffMemberComparator);
		
		// Sort roster entries in timetable
		adminStatisticContainer.sortRosterEntries(sortComp);
		
		// Put timetable to map
		params.put(MODEL_ROSTER_MONTH_CONTAINER_NAME, adminStatisticContainer);
		
		return params;
	}

}
