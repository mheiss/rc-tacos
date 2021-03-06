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

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.QueryFilter;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.web.container.Month;
import at.rc.tacos.web.container.RosterEntryContainer;
import at.rc.tacos.web.container.RosterMonthContainer;
import at.rc.tacos.web.net.WebClient;
import at.rc.tacos.web.session.UserSession;

/**
 * Roster Month Controller
 * @author Payer Martin
 * @version 1.0
 * TODO: Stunden/Mitarbeiter und Monat ausgeben, Print Roster Month nicht vergessen 
 */
public class RosterMonthController extends Controller {

	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_FUNCTION_NAME = "functionId";
	private static final String MODEL_FUNCTION_NAME = "function";
	private static final String MODEL_FUNCTION_LIST_NAME = "functionList";
	
	private static final String PARAM_LOCATION_STAFF_MEMBER_NAME = "locationStaffMemberId";
	private static final String PARAM_LOCATION_STAFF_MEMBER_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_STAFF_MEMBER_NAME = "locationStaffMember";
	private static final String MODEL_LOCATION_STAFF_MEMBER_LIST_NAME = "locationStaffMemberList";
	
	private static final String PARAM_STAFF_MEMBER_NAME = "staffMemberId";
	private static final String PARAM_STAFF_MEMBER_NO_VALUE = "noValue";
	private static final String MODEL_STAFF_MEMBER_NAME = "staffMember";
	private static final String MODEL_STAFF_MEMBER_LIST_NAME = "staffList";
	
	private static final String PARAM_MONTH_NAME = "month";
	private static final String MODEL_MONTH_NAME = "month";
	
	private static final String PARAM_YEAR_NAME = "year";
	private static final String MODEL_YEAR_NAME = "year";
	private static final String MODEL_YEAR_LIST_NAME = "yearList";
	
	private static final String MODEL_ROSTER_MONTH_CONTAINER_NAME = "rosterMonthContainer";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String MODEL_MESSAGE_CODE_NAME = "messageCode";
	
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
		final Location defaultLocation = userSession.getDefaultFormValues().getRosterMonthLocation();
		if (paramLocationId != null && !paramLocationId.equals("") && !paramLocationId.equals(PARAM_LOCATION_NO_VALUE)) {
			locationId = Integer.parseInt(paramLocationId);		
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
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
		if (userSession.getDefaultFormValues().getRosterMonthMonth() != null) {
			month = userSession.getDefaultFormValues().getRosterMonthMonth();
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
		if (userSession.getDefaultFormValues().getRosterMonthYear() != null) {
			year = userSession.getDefaultFormValues().getRosterMonthYear();
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
		params.put(MODEL_YEAR_LIST_NAME, yearList);
		
		// Function
		final String paramFunctionId = request.getParameter(PARAM_FUNCTION_NAME);
		int functionId = 0;
		Competence function = userSession.getDefaultFormValues().getRosterMonthFunction();;
		if (paramFunctionId != null && !paramFunctionId.equals("")) {
			functionId = Integer.parseInt(paramFunctionId);	
		}
		
		final List<AbstractMessage> functionListTemp = connection.sendListingRequest(Competence.ID, null);
		if (!Competence.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		final List<Competence> functionList = new ArrayList<Competence>();
		for (final Iterator<AbstractMessage> itCompetenceList = functionListTemp.iterator(); itCompetenceList.hasNext();) {
			final Competence c = (Competence)itCompetenceList.next();
			if (c.getCompetenceName().startsWith("_")) {
				if (c.getId() == functionId) {
					function = c;
				}
				functionList.add(c);
			}
		}	
		params.put(MODEL_FUNCTION_LIST_NAME, functionList);
		if (function == null) {
			if (functionList.size() > 0) {
				function = functionList.get(0);
			} else {
				throw new IllegalArgumentException("Error: Function must not be null.");
			}
		}
		params.put(MODEL_FUNCTION_NAME, function);

		
		// Location staff member
		final String paramLocationStaffMemberId = request.getParameter(PARAM_LOCATION_STAFF_MEMBER_NAME);
		int locationStaffMemberId = 0;
		final Location defaultLocationStaffMember = userSession.getDefaultFormValues().getRosterMonthLocationStaffMember();
		Location locationStaffMember = null;
		if (paramLocationStaffMemberId != null && !paramLocationStaffMemberId.equals("") && !paramLocationStaffMemberId.equals(PARAM_LOCATION_STAFF_MEMBER_NO_VALUE)) {
			locationStaffMemberId = Integer.parseInt(paramLocationStaffMemberId);		
		}
		final List<AbstractMessage> locationStaffMemberList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LOCATION_STAFF_MEMBER_LIST_NAME, locationStaffMemberList);
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
		
		final StaffMember defaultStaffMember = userSession.getDefaultFormValues().getRosterMonthStaffMember();		
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
			if (function != null) {
				boolean hasCompetence = false;
				final List<Competence> cL = sm.getCompetenceList();
				for (final Iterator<Competence> itCL = cL.iterator(); itCL.hasNext();) {
					final Competence c = itCL.next();
					if (c.getId() ==  function.getId() || c.getCompetenceName().equals(function.getCompetenceName())) {
						hasCompetence = true;
					}
				}
				if (hasCompetence) {
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
			} else {
				boolean hasAnyCompetence = false;
				final List<Competence> cL = sm.getCompetenceList();
				for (final Iterator<Competence> itCL = cL.iterator(); itCL.hasNext();) {
					final Competence c = itCL.next();
					if (c.getCompetenceName().startsWith("_")) {
						hasAnyCompetence = true;
					}
				}
				if (hasAnyCompetence) {
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
			}
		}		
		params.put(MODEL_STAFF_MEMBER_LIST_NAME, staffList);
		if (staffMember != null || (paramStaffMemberId != null && paramStaffMemberId.equals(PARAM_STAFF_MEMBER_NO_VALUE))) {
			params.put(MODEL_STAFF_MEMBER_NAME, staffMember);
		} else {
			params.put(MODEL_STAFF_MEMBER_NAME, defaultStaffMember);
		}
		staffMember = (StaffMember)params.get(MODEL_STAFF_MEMBER_NAME);
		
		// Get Roster Entries
		final QueryFilter rosterFilter = new QueryFilter();
		if (location != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_FILTER, Integer.toString(month + 1));
		rosterFilter.add(IFilterTypes.ROSTER_YEAR_FILTER, year.toString());
		rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER, function.getCompetenceName());
		if (locationStaffMember != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER, Integer.toString(locationStaffMember.getId()));
		}
		if (staffMember != null) {
			rosterFilter.add(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER, Integer.toString(staffMember.getStaffMemberId()));
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
		final Comparator dayComparator = new PropertyComparator("day", true, true);
		final Comparator staffMemberComparator = new CompoundComparator(new Comparator[] {
			new PropertyComparator("lastName", true, true),
			new PropertyComparator("firstName", true, true)
		});
		final Comparator sortComp = new CompoundComparator(new Comparator[] {
			new PropertyComparator("plannedStartOfWork", true, true)
		});
		
		// Create Staff Member List
		final List<StaffMember> staffMemberList = new ArrayList<StaffMember>();
		for (final AbstractMessage am : staffList) {
			final StaffMember staffM = (StaffMember)am;
			staffMemberList.add(staffM);
		}
		
		// Create roster month container
		final RosterMonthContainer rosterMonthContainer = new RosterMonthContainer();
		
		// Set roster entry list
		rosterMonthContainer.setRosterEntryContainerList(rosterEntryContainerList);
		
		// Fill day list
		rosterMonthContainer.fillDayList(month, year.intValue());
		
		// Fill staff member list
		if (staffMember == null) {
			rosterMonthContainer.setStaffMemberList(staffMemberList);
		} else {
			final List<StaffMember> smList = new ArrayList<StaffMember>();
			smList.add(staffMember);
			rosterMonthContainer.setStaffMemberList(smList);
		}
		rosterMonthContainer.sortStaffMemberList(staffMemberComparator);
		
		// Fill job list
		final List<AbstractMessage> jobListTemp = connection.sendListingRequest(Job.ID, null);
		final List<Job> jobList = new ArrayList<Job>();
		if (!Job.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itJobList = jobListTemp.iterator(); itJobList.hasNext();) {
			final Job job = (Job)itJobList.next();
			jobList.add(job);
		}
		rosterMonthContainer.setJobList(jobList);
		
		// Fill service type list
		final List<AbstractMessage> serviceTypeListTemp = connection.sendListingRequest(ServiceType.ID, null);
		final List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
		if (!ServiceType.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itServiceTypeList = serviceTypeListTemp.iterator(); itServiceTypeList.hasNext();) {
			final ServiceType serviceType = (ServiceType)itServiceTypeList.next();
			serviceTypeList.add(serviceType);
		}
		rosterMonthContainer.setServiceTypeList(serviceTypeList);
		
		// Create timetable
		rosterMonthContainer.createTimetable(dayComparator, staffMemberComparator);
		
		// Sort roster entries in timetable
		rosterMonthContainer.sortRosterEntries(sortComp);
		
		// Put timetable to map
		params.put(MODEL_ROSTER_MONTH_CONTAINER_NAME, rosterMonthContainer);
		
		// Parse message code from other controllers
		if (request.getParameter(PARAM_MESSAGE_CODE_NAME) != null && !request.getParameter(PARAM_MESSAGE_CODE_NAME).equals("")) {
			params.put(MODEL_MESSAGE_CODE_NAME, request.getParameter(PARAM_MESSAGE_CODE_NAME));
		}
		
		userSession.getDefaultFormValues().setRosterMonthLocation(location);
		userSession.getDefaultFormValues().setRosterMonthMonth(month);
		userSession.getDefaultFormValues().setRosterMonthYear(year);
		userSession.getDefaultFormValues().setRosterMonthFunction(function);
		userSession.getDefaultFormValues().setRosterMonthLocationStaffMember(locationStaffMember);
		userSession.getDefaultFormValues().setRosterMonthStaffMember(staffMember);
		
		return params;
	}
}