package at.rc.tacos.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
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
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.container.Month;
import at.rc.tacos.web.container.RosterEntryContainer;
import at.rc.tacos.web.container.RosterMonthContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Print Roster Controller
 * @author Payer Martin
 * @version 1.0
 */
public class PrintRosterMonthController extends Controller {
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_FUNCTION_NAME = "functionId";
	private static final String MODEL_FUNCTION_NAME = "function";
	
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
	private static final String MODEL_YEAR_LIST_NAME = "yearList";
	
	private static final String MODEL_ROSTER_MONTH_CONTAINER_NAME = "rosterMonthContainer";

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
		int month = -1;
		if (userSession.getDefaultFormValues().getRosterMonthMonth() != null) {
			month = userSession.getDefaultFormValues().getRosterMonthMonth();
		}
		if (paramMonth != null && !paramMonth.equals("")) {
			month = Month.valueOf(paramMonth).getProperty();
		}
		if (month != 0 && month != 1 && month != 2 && month != 3 && month != 4 && month != 5 && month != 6 && month != 7 && month != 8 && month != 9 && month != 10 && month != 11 && month != 12) {
			throw new IllegalArgumentException("Error: Value of month is invalid.");
		}
		params.put(MODEL_MONTH_NAME, paramMonth);
		
		// Year
		final Calendar yearCal = Calendar.getInstance();
		Integer year = -1;
		Integer yearP = -1;
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
		if (year == -1) {
			throw new IllegalArgumentException("Error: Year must not be -1.");
		}
		params.put(MODEL_YEAR_NAME, year);
		
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
		if (function == null) {
			throw new IllegalArgumentException("Error: Function must not be null.");
		}
		params.put(MODEL_FUNCTION_NAME, function);
		
		// Location staff member
		final String paramLocationStaffMemberId = request.getParameter(PARAM_LOCATION_STAFF_MEMBER_NAME);
		int locationStaffMemberId = -1;
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
			params.put(MODEL_LOCATION_STAFF_MEMBER_NAME, null);
		}
		locationStaffMember = (Location)params.get(MODEL_LOCATION_STAFF_MEMBER_NAME);
		
		// Staff Member (depends on function and location staff member filter)		
		final String paramStaffMemberId = request.getParameter(PARAM_STAFF_MEMBER_NAME);
		int staffMemberId = -1;			
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
		if (staffMember != null || (paramStaffMemberId != null && paramStaffMemberId.equals(PARAM_STAFF_MEMBER_NO_VALUE))) {
			params.put(MODEL_STAFF_MEMBER_NAME, staffMember);
		} else {
			params.put(MODEL_STAFF_MEMBER_NAME, null);
		}
		staffMember = (StaffMember)params.get(MODEL_STAFF_MEMBER_NAME);
		
		// Get Roster Entries
		final QueryFilter rosterFilter = new QueryFilter();
		if (location != null) {
			rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_FILTER, Integer.toString(month + 1));
		rosterFilter.add(IFilterTypes.ROSTER_YEAR_FILTER, year.toString());
		if (function.getCompetenceName().equals(Competence.FUNCTION_HA)) {
			rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_SERVICE_TYPE_FILTER, ServiceType.SERVICETYPE_HAUPTAMTLICH);
		} else if (function.getCompetenceName().equals(Competence.FUNCTION_ZD)) {
			rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_SERVICE_TYPE_FILTER, ServiceType.SERIVCETYPE_ZIVILDIENER);
		}
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
			rosterEntryContainer.setPlannedStartOfWork(new Date(rosterEntry.getPlannedStartOfWork()));
			rosterEntryContainer.setPlannedEndOfWork(new Date(rosterEntry.getPlannedEndOfWork()));
			if (rosterEntry.getRealStartOfWork() == 0) {
				rosterEntryContainer.setRealStartOfWork(null);
			} else {
				rosterEntryContainer.setRealStartOfWork(new Date(rosterEntry.getRealStartOfWork()));
			}
			if (rosterEntry.getRealEndOfWork() == 0) {
				rosterEntryContainer.setRealEndOfWork(null);
			} else {
				rosterEntryContainer.setRealEndOfWork(new Date(rosterEntry.getRealEndOfWork()));
			}
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
		final Comparator functionComparator = new PropertyComparator("function.competenceName", true, true);
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
		
		return params;
	}		
}
