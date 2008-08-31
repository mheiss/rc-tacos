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
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.container.AdminStatisticContainer;
import at.rc.tacos.web.container.Month;
import at.rc.tacos.web.container.RosterEntryContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Print Admin Statistic Controller
 * @author Payer Martin
 * @version 1.0
 */
public class PrintAdminStatisticController extends Controller {

	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";

	private static final String PARAM_LOCATION_STAFF_MEMBER_NAME = "locationStaffMemberId";
	private static final String PARAM_LOCATION_STAFF_MEMBER_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_STAFF_MEMBER_NAME = "locationStaffMember";
	
	private static final String PARAM_STAFF_MEMBER_NAME = "staffMemberId";
	private static final String PARAM_STAFF_MEMBER_NO_VALUE = "noValue";
	private static final String MODEL_STAFF_MEMBER_NAME = "staffMember";
	
	private static final String PARAM_MONTH_NAME = "month";
	private static final String MODEL_MONTH_NAME = "month";
	
	private static final String PARAM_YEAR_NAME = "year";
	private static final String MODEL_YEAR_NAME = "year";
	
	private static final String PARAM_SERVICE_TYPE_NAME = "serviceTypeId";
	private static final String PARAM_SERVICE_TYPE_NO_VALUE = "noValue";
	private static final String MODEL_SERVICE_TYPE_NAME = "serviceType";
	
	private static final String MODEL_ROSTER_MONTH_CONTAINER_NAME = "adminStatisticContainer";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Check authorization
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
		
		// Location
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		Location location = null;
		final Location defaultLocation = userSession.getDefaultFormValues().getAdminStatisticLocation();
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
		if (userSession.getDefaultFormValues().getAdminStatisticMonth() != null) {
			month = userSession.getDefaultFormValues().getAdminStatisticMonth();
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
		if (userSession.getDefaultFormValues().getAdminStatisticYear() != null) {
			year = userSession.getDefaultFormValues().getAdminStatisticYear();
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
	
		// Location staff member
		final String paramLocationStaffMemberId = request.getParameter(PARAM_LOCATION_STAFF_MEMBER_NAME);
		int locationStaffMemberId = 0;
		final Location defaultLocationStaffMember = userSession.getDefaultFormValues().getAdminStatisticLocationStaffMember();
		Location locationStaffMember = null;
		if (paramLocationStaffMemberId != null && !paramLocationStaffMemberId.equals("") && !paramLocationStaffMemberId.equals(PARAM_LOCATION_STAFF_MEMBER_NO_VALUE)) {
			locationStaffMemberId = Integer.parseInt(paramLocationStaffMemberId);		
		}
		final List<AbstractMessage> locationStaffMemberList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itLocationStaffMemberList = locationStaffMemberList.iterator(); itLocationStaffMemberList.hasNext();) {
			final Location l = (Location)itLocationStaffMemberList.next();
			if (l.getId() == locationStaffMemberId) {
				locationStaffMember = l;
			}
		}
		if (locationStaffMember != null || (paramLocationStaffMemberId != null && paramLocationStaffMemberId.equals(PARAM_LOCATION_STAFF_MEMBER_NO_VALUE))) {
			params.put(MODEL_LOCATION_STAFF_MEMBER_NAME, locationStaffMember);
		} else {
			params.put(MODEL_LOCATION_STAFF_MEMBER_NAME, defaultLocationStaffMember);
		}
		locationStaffMember = (Location)params.get(MODEL_LOCATION_STAFF_MEMBER_NAME);
		
		// Staff Member (depends on function and location staff member filter)		
		final String paramStaffMemberId = request.getParameter(PARAM_STAFF_MEMBER_NAME);
		int staffMemberId = 0;
		
		final StaffMember defaultStaffMember = userSession.getDefaultFormValues().getAdminStatisticStaffMember();		
		StaffMember staffMember = null;
		if (paramStaffMemberId != null && !paramStaffMemberId.equals("") && !paramStaffMemberId.equalsIgnoreCase(PARAM_STAFF_MEMBER_NO_VALUE)) {
			staffMemberId = Integer.parseInt(paramStaffMemberId);		
		}
		final List<AbstractMessage> staffList = new ArrayList<AbstractMessage>();
		final List<AbstractMessage> staffListTemp = connection.sendListingRequest(StaffMember.ID, null);
		if (!StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itStaffList = staffListTemp.iterator(); itStaffList.hasNext();) {
			final StaffMember sm = (StaffMember)itStaffList.next();

			final List<Competence> cL = sm.getCompetenceList();
			for (final Iterator<Competence> itCL = cL.iterator(); itCL.hasNext();) {
				final Competence c = itCL.next();
			}
				
			if (locationStaffMember != null) {
				if (sm.getPrimaryLocation().getId() == locationStaffMember.getId()) {
					staffList.add(sm);
				}
			} else {
				staffList.add(sm);
			}
			if (sm.getStaffMemberId() == staffMemberId) {
				staffMember = sm;
			}
				
			
		}		
		if (staffMember != null || (paramStaffMemberId != null && paramStaffMemberId.equals(PARAM_STAFF_MEMBER_NO_VALUE))) {
			params.put(MODEL_STAFF_MEMBER_NAME, staffMember);
		} else {
			params.put(MODEL_STAFF_MEMBER_NAME, defaultStaffMember);
		}
		staffMember = (StaffMember)params.get(MODEL_STAFF_MEMBER_NAME);
		
		// Service Type
		final String paramServiceTypeId = request.getParameter(PARAM_SERVICE_TYPE_NAME);
		int serviceTypeId = 0;
		final ServiceType defaultServiceType = userSession.getDefaultFormValues().getAdminStatisticServiceType();
		ServiceType serviceType = null;
		if (paramServiceTypeId != null && !paramServiceTypeId.equals("") &&!paramServiceTypeId.equals(PARAM_SERVICE_TYPE_NO_VALUE)) {
			serviceTypeId = Integer.parseInt(paramServiceTypeId);		
		}
		List<AbstractMessage> serviceTypeList = new ArrayList<AbstractMessage>();
		if (authorization.equals(Login.AUTH_ADMIN)) {
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, null);
		}	
		if (!ServiceType.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itServiceTypeList = serviceTypeList.iterator(); itServiceTypeList.hasNext();) {
			final ServiceType st = (ServiceType)itServiceTypeList.next();
			if (st.getId() == serviceTypeId) {
				serviceType = st;
			}
		}
		if (serviceType != null || (paramServiceTypeId != null && paramServiceTypeId.equals(PARAM_SERVICE_TYPE_NO_VALUE))) {
			params.put(MODEL_SERVICE_TYPE_NAME, serviceType);
		} else {
			params.put(MODEL_SERVICE_TYPE_NAME, defaultServiceType);
		}
		serviceType = (ServiceType)params.get(MODEL_SERVICE_TYPE_NAME);
		
		// Get Roster Entries
		final QueryFilter rosterFilter = new QueryFilter();
		if (location != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_FILTER, Integer.toString(month + 1));
		rosterFilter.add(IFilterTypes.ROSTER_YEAR_FILTER, year.toString());
		if (locationStaffMember != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER, Integer.toString(locationStaffMember.getId()));
		}
		if (staffMember != null) {
			rosterFilter.add(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER, Integer.toString(staffMember.getStaffMemberId()));
		}
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER, "true");
		if (serviceType != null) {
			rosterFilter.add(IFilterTypes.ROSTER_SERVICE_TYPE_FILTER, Integer.toString(serviceType.getId()));
		}
		
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
