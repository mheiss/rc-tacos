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
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.form.RosterEntryContainer;
import at.rc.tacos.web.form.RosterMonthContainer;
import at.rc.tacos.web.form.Month;
import at.rc.tacos.web.session.UserSession;

/**
 * Roster Month Controller
 * @author Payer Martin
 * @version 1.0
 * TODO: Beim function job service type filter über die Id gehen
 */
public class RosterMonthController extends Controller {

	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_FUNCTION_NAME = "functionId";
	private static final String PARAM_FUNCTION_NO_VALUE = "noValue";
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
		Location location = userSession.getDefaultFormValues().getDefaultLocation();
		if (paramLocationId != null && !paramLocationId.equals("")) {
			locationId = Integer.parseInt(paramLocationId);		
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
			final Location l = (Location)itLoactionList.next();
			if (l.getId() == locationId) {
				location = l;
			}
		}
		if (location == null) {
			if (locationList.size() > 0) {
				location = (Location)locationList.get(0);
			} else {
				throw new IllegalArgumentException("Error: Location has an illegal state.");
			}
		} 
		params.put(MODEL_LOCATION_NAME, location);
		
		// Month
		String paramMonth = request.getParameter(PARAM_MONTH_NAME);
		final Calendar cal = Calendar.getInstance();
		if (userSession.getDefaultFormValues().getDefaultDate() != null) {
			cal.setTime(userSession.getDefaultFormValues().getDefaultDate());
		} else {
			cal.setTime(new Date());
		}
		int month = cal.get(Calendar.MONTH);
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
		final Competence defaultFunction = userSession.getDefaultFormValues().getDefaultFunction();
		Competence function = null;
		if (paramFunctionId != null && !paramFunctionId.equals("") && !paramFunctionId.equals(PARAM_FUNCTION_NO_VALUE)) {
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
		if (function != null || (paramFunctionId != null && paramFunctionId.equals(PARAM_FUNCTION_NO_VALUE))) {
			params.put(MODEL_FUNCTION_NAME, function);
		} else {
			params.put(MODEL_FUNCTION_NAME, defaultFunction);
		}
		function = (Competence)params.get(MODEL_FUNCTION_NAME);
		
		// Location staff member
		final String paramLocationStaffMemberId = request.getParameter(PARAM_LOCATION_STAFF_MEMBER_NAME);
		int locationStaffMemberId = 0;
		final Location defaultLocationStaffMember = userSession.getDefaultFormValues().getDefaultLocation();
		Location locationStaffMember = null;
		if (paramLocationStaffMemberId != null && !paramLocationStaffMemberId.equals("")) {
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
		
		StaffMember defaultStaffMember = userSession.getDefaultFormValues().getDefaultStaffMember();		
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
		rosterFilter.add(IFilterTypes.ROSTER_LOCATION_FILTER, Integer.toString(location.getId()));
		rosterFilter.add(IFilterTypes.ROSTER_MONTH_FILTER, Integer.toString(month + 1));
		rosterFilter.add(IFilterTypes.ROSTER_YEAR_FILTER, year.toString());
		if (function != null) {
			if (function.getCompetenceName().equals(Competence.FUNCTION_LS)) {
				rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_JOB_SERVICE_TYPE_FILTER, Job.JOB_LEITSTELLENDISPONENT);
			} else if (function.getCompetenceName().equals(Competence.FUNCTION_HA)) {
				rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_JOB_SERVICE_TYPE_FILTER, ServiceType.SERVICETYPE_HAUPTAMTLICH);
			} else if (function.getCompetenceName().equals(Competence.FUNCTION_ZD)) {
				rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_JOB_SERVICE_TYPE_FILTER, ServiceType.SERIVCETYPE_ZIVILDIENER);
			}
			rosterFilter.add(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER, function.getCompetenceName());
		}
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
			
			if (rosterEntry.getJob().getJobName().equals(Job.JOB_LEITSTELLENDISPONENT)) {
				for (final Iterator<Competence> itFl = functionList.iterator(); itFl.hasNext();) {
					final Competence f = (Competence)itFl.next();
					if (f.getCompetenceName().equals(Competence.FUNCTION_LS)) {
						rosterEntryContainer.setFunction(f);
					}
				}
			} else if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_HAUPTAMTLICH)) {
				for (final Iterator<Competence> itFl = functionList.iterator(); itFl.hasNext();) {
					final Competence f = (Competence)itFl.next();
					if (f.getCompetenceName().equals(Competence.FUNCTION_HA)) {
						rosterEntryContainer.setFunction(f);
					}
				}
			} else if (rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERIVCETYPE_ZIVILDIENER)) {
				for (final Iterator<Competence> itFl = functionList.iterator(); itFl.hasNext();) {
					final Competence f = (Competence)itFl.next();
					if (f.getCompetenceName().equals(Competence.FUNCTION_ZD)) {
						rosterEntryContainer.setFunction(f);
					}
				}
			}
			
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
		
		// Set function list
		if (function == null) {
			rosterMonthContainer.setFunctionList(functionList);
		} else {
			final List<Competence> fList = new ArrayList<Competence>();
			fList.add(function);
			rosterMonthContainer.setFunctionList(fList);
		}
		rosterMonthContainer.sortFunctionList(functionComparator);
		
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
		
		// Create timetable
		rosterMonthContainer.createTimetable(functionComparator, dayComparator, staffMemberComparator);
		
		// Sort roster entries in timetable
		rosterMonthContainer.sortRosterEntries(sortComp);
		
		params.put(MODEL_ROSTER_MONTH_CONTAINER_NAME, rosterMonthContainer);
				
		userSession.getDefaultFormValues().setDefaultLocation(location);
		final Calendar cale = Calendar.getInstance();
		if (userSession.getDefaultFormValues().getDefaultDate() != null) {
			cale.setTime(userSession.getDefaultFormValues().getDefaultDate());
		}
		cale.set(Calendar.MONTH, month);
		userSession.getDefaultFormValues().setDefaultDate(cale.getTime());	
		userSession.getDefaultFormValues().setDefaultFunction(function);
		userSession.getDefaultFormValues().setDefaultStaffMember(staffMember);
		
		return params;
	}
}