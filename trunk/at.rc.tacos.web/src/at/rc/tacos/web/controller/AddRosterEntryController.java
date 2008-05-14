package at.rc.tacos.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import at.rc.tacos.web.session.UserSession;

/**
 * Add Roster Entry Controller
 * @author Payer Martin
 * @version 1.0
 */
public class AddRosterEntryController extends Controller {

	private static final String ACTION_NAME = "action";
	private static final String ACTION_ADD_ROSTER_ENTRY = "addRosterEntry";
	
	private static final String PARAM_JOB_NAME = "jobId";
	private static final String PARAM_JOB_NO_VALUE = "noValue";
	private static final String MODEL_JOB_NAME = "job";
	private static final String MODEL_JOB_LIST_NAME = "jobList";
	
	private static final String PARAM_STAFF_MEMBER_NAME = "staffMemberId";
	private static final String PARAM_STAFF_MEMBER_NO_VALUE = "noValue";
	private static final String MODEL_STAFF_MEMBER_NAME = "staffMember";
	private static final String MODEL_STAFF_MEMBER_LIST_NAME = "staffList";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String PARAM_SERVICE_TYPE_NAME = "serviceTypeId";
	private static final String PARAM_SERVICE_TYPE_NO_VALUE = "noValue";
	private static final String MODEL_SERVICE_TYPE_NAME = "serviceType";
	private static final String MODEL_SERVICE_TYPE_LIST_NAME = "serviceTypeList";
	
	private static final String PARAM_STANDBY_NAME = "standby";
	private static final String MODEL_STANDBY_NAME = "standby";
	
	private static final String PARAM_COMMENT_NAME = "comment";
	private static final String MODEL_COMMENT_NAME = "comment";
	
	private static final String MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME = "calendarDefaultDateMilliseconds";
	private static final String MODEL_CALENDAR_RANGE_START_NAME = "calendarRangeStart";
	private static final String MODEL_CALENDAR_RANGE_END_NAME = "calendarRangeEnd";
	private static final int MODEL_CALENDAR_RANGE_START_OFFSET = 10;
	private static final int MODEL_CALENDAR_RANGE_END_OFFSET = 1;
	
	private static final String PARAM_DATE_FROM_NAME = "dateFrom";
	private static final String PARAM_TIME_FROM_HOURS_NAME = "timeFromHours";
	private static final String PARAM_TIME_FROM_MINUTES_NAME = "timeFromMinutes";
	private static final String MODEL_DATE_FROM_NAME = "dateFrom";
	private static final String MODEL_TIME_FROM_HOURS_NAME = "timeFromHours";
	private static final String MODEL_TIME_FROM_MINUTES_NAME = "timeFromMinutes";
	
	private static final String PARAM_DATE_TO_NAME = "dateTo";
	private static final String PARAM_TIME_TO_HOURS_NAME = "timeToHours";
	private static final String PARAM_TIME_TO_MINUTES_NAME= "timeToMinutes";
	private static final String MODEL_DATE_TO_NAME = "dateTo";
	private static final String MODEL_TIME_TO_HOURS_NAME = "timeToHours";
	private static final String MODEL_TIME_TO_MINUTES_NAME= "timeToMinutes";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Job List
		final String paramJobId = request.getParameter(PARAM_JOB_NAME);
		int jobId = 0;
		final Job defaultJob = null;
		Job job = null;
		if (paramJobId != null && !paramJobId.equals("") && !paramJobId.equals(PARAM_JOB_NO_VALUE)) {
			jobId = Integer.parseInt(paramJobId);	
		}
		final List<AbstractMessage> jobList = connection.sendListingRequest(Job.ID, null);
		if (!Job.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itJobList = jobList.iterator(); itJobList.hasNext();) {
			final Job j = (Job)itJobList.next();
			if (j.getId() == jobId) {
				job = j;
			}
		}
		params.put(MODEL_JOB_LIST_NAME, jobList);
		if (job != null || (paramJobId != null && paramJobId.equals(PARAM_JOB_NO_VALUE))) {
			params.put(MODEL_JOB_NAME, job);
		} else {
			params.put(MODEL_JOB_NAME, defaultJob);
		}
		
		
		// Staff Member List
		final String paramStaffMemberId = request.getParameter(PARAM_STAFF_MEMBER_NAME);
		int staffMemberId = 0;
		final StaffMember defaultStaffMember = null;
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
			if (job != null) {
				boolean hasCompetence = false;
				final List<Competence> competenceList = sm.getCompetenceList();
				for (final Iterator<Competence> itCompetenceList = competenceList.iterator(); itCompetenceList.hasNext();) {
					if (itCompetenceList.next().getId() == job.getId()) {
						hasCompetence = true;
					}
				}
				if (hasCompetence) {
					staffList.add(sm);
					if (sm.getStaffMemberId() == staffMemberId) {
						staffMember = sm;
					}
				}
			} else {
				staffList.add(sm);
				if (sm.getStaffMemberId() == staffMemberId) {
					staffMember = sm;
				}
			}
		}
		params.put(MODEL_STAFF_MEMBER_LIST_NAME, staffList);
		if (staffMember != null || (paramStaffMemberId != null && paramStaffMemberId.equals(PARAM_STAFF_MEMBER_NO_VALUE))) {
			params.put(MODEL_STAFF_MEMBER_NAME, staffMember);
		} else {
			params.put(MODEL_STAFF_MEMBER_NAME, defaultStaffMember);
		}
		
		
		// Location List
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		final Location defaultLocation = userSession.getFormDefaultValues().getDefaultLocation();
		Location location = null;
		if (paramLocationId != null && !paramLocationId.equals("") && !paramLocationId.equals(PARAM_LOCATION_NO_VALUE)) {
			locationId = Integer.parseInt(paramLocationId);
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (!Location.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
			final Location l = (Location)itLoactionList.next();
			if (l.getId() == locationId) {
				location = l;
			}
		}
		params.put(MODEL_LOCATION_LIST_NAME, locationList);
		if (location != null || (paramLocationId != null && paramLocationId.equals(PARAM_LOCATION_NO_VALUE))) {
			params.put(MODEL_LOCATION_NAME, location);
		} else {
			params.put(MODEL_LOCATION_NAME, defaultLocation);
		}
		
		
		// Service Type List
		final String paramServiceTypeId = request.getParameter(PARAM_SERVICE_TYPE_NAME);
		int serviceTypeId = 0;
		final ServiceType defaultServiceType = null;
		ServiceType serviceType = null;
		if (paramServiceTypeId != null && !paramServiceTypeId.equals("") &&!paramServiceTypeId.equals(PARAM_SERVICE_TYPE_NO_VALUE)) {
			serviceTypeId = Integer.parseInt(paramServiceTypeId);		
		}
		List<AbstractMessage> serviceTypeList = new ArrayList<AbstractMessage>();
		if (authorization.equals(Login.AUTH_ADMIN)) {
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, null);
		} else if (authorization.equals(Login.AUTH_USER)) {
			final QueryFilter filter = new QueryFilter();
			filter.add(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER, ServiceType.SERVICETYPE_FREIWILLIG);
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, filter);
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
		params.put(MODEL_SERVICE_TYPE_LIST_NAME, serviceTypeList);
		if (serviceType != null || (paramServiceTypeId != null && paramServiceTypeId.equals(PARAM_SERVICE_TYPE_NO_VALUE))) {
			params.put(MODEL_SERVICE_TYPE_NAME, serviceType);
		} else {
			params.put(MODEL_SERVICE_TYPE_NAME, defaultServiceType);
		}
		
		// Get Standby
		final String paramStandby = request.getParameter(PARAM_STANDBY_NAME);
		boolean standby = false;
		if (paramStandby != null) {
			standby = true;
		}
		params.put(MODEL_STANDBY_NAME, standby);
		
		// Get Comment
		String comment = null;
		if (request.getParameter(PARAM_COMMENT_NAME) != null) {
			comment = request.getParameter(PARAM_COMMENT_NAME);
		}
		params.put(MODEL_COMMENT_NAME, comment);
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET;
		final int rangeEnd = calendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET;
		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, calendar.getTimeInMillis());
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		// Get From
		final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		
		String dateFromString = null;
		String timeFromHoursString = null;
		String timeFromMinutesString = null;
		
		final String defaultDateFromString = sdfDate.format(userSession.getFormDefaultValues().getDefaultDate());
		final String defaultTimeFromHoursString = "00";
		final String defaultTimeFromMinutesString = "00";
		
		if (request.getParameter(PARAM_DATE_FROM_NAME) != null) {
			dateFromString = request.getParameter(PARAM_DATE_FROM_NAME);
		} 
		if (request.getParameter(PARAM_TIME_FROM_HOURS_NAME) != null) {
			timeFromHoursString = request.getParameter(PARAM_TIME_FROM_HOURS_NAME);
		}
		if (request.getParameter(PARAM_TIME_FROM_MINUTES_NAME) != null) {
			timeFromMinutesString = request.getParameter(PARAM_TIME_FROM_MINUTES_NAME);
		}
		if (dateFromString != null) {
			params.put(MODEL_DATE_FROM_NAME, dateFromString);
		} else {
			params.put(MODEL_DATE_FROM_NAME, defaultDateFromString);
		}
		if (timeFromHoursString != null) {
			params.put(MODEL_TIME_FROM_HOURS_NAME, timeFromHoursString);
		} else {
			params.put(MODEL_TIME_FROM_HOURS_NAME, defaultTimeFromHoursString);
		}
		if (timeFromMinutesString != null) {
			params.put(MODEL_TIME_FROM_MINUTES_NAME, timeFromMinutesString);
		} else {
			params.put(MODEL_TIME_FROM_MINUTES_NAME, defaultTimeFromMinutesString);
		}
		final String from = dateFromString + " " + timeFromHoursString + ":" + timeFromMinutesString;
			
		// Get To
		String dateToString = null;
		String timeToHoursString = null;
		String timeToMinutesString = null;
		
		final String defaultDateToString = sdfDate.format(userSession.getFormDefaultValues().getDefaultDate());
		final String defaultTimeToHoursString = "00";
		final String defaultTimeToMinutesString = "00";
		
		if (request.getParameter(PARAM_DATE_TO_NAME) != null) {
			dateToString = request.getParameter(PARAM_DATE_TO_NAME);
		} 
		if (request.getParameter(PARAM_TIME_TO_HOURS_NAME) != null) {
			timeToHoursString = request.getParameter(PARAM_TIME_TO_HOURS_NAME);
		}
		if (request.getParameter(PARAM_TIME_TO_MINUTES_NAME) != null) {
			timeToMinutesString = request.getParameter(PARAM_TIME_TO_MINUTES_NAME);
		}
		if (dateToString != null) {
			params.put(MODEL_DATE_TO_NAME, dateToString);
		} else {
			params.put(MODEL_DATE_TO_NAME, defaultDateToString);
		}
		if (timeToHoursString != null) {
			params.put(MODEL_TIME_TO_HOURS_NAME, timeToHoursString);
		} else {
			params.put(MODEL_TIME_TO_HOURS_NAME, defaultTimeToHoursString);
		}
		if (timeToMinutesString != null) {
			params.put(MODEL_TIME_TO_MINUTES_NAME, timeToMinutesString);
		} else {
			params.put(MODEL_TIME_TO_MINUTES_NAME, defaultTimeToMinutesString);
		}
		final String to = dateToString + " " + timeToHoursString + ":" + timeToMinutesString;
				
		// Get Action
		final String action = request.getParameter(ACTION_NAME);
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_ADD_ROSTER_ENTRY)) {
			if (job == null) {
				errors.put("job", "Verwendung ist ein Pflichtfeld.");
				valid = false;
			}
			if (location == null) {
				errors.put("location", "Ortstelle ist ein Pflichtfeld.");
				valid = false;
			}
			if (staffMember == null) {
				errors.put("staffMember", "Mitarbeiter ist ein Pflichtfeld.");
				valid = false;
			}
			if (serviceType == null) {
				errors.put("serviceType", "Dienstverhältnis ist ein Pflichtfeld.");
				valid = false;
			}
			
			final Calendar rangeStartCalendar = Calendar.getInstance();
			rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - 10);
			
			final Calendar rangeEndCalendar = Calendar.getInstance();
			rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + 1);
			
			final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			Date plannedStartOfWork = null;
			if (dateFromString == null || dateFromString.equals("") || timeFromHoursString == null || timeFromMinutesString == null) {
				errors.put("plannedStartOfWorkMissing", "Dienst von ist ein Pflichtfeld.");
				valid = false;
			} else {
				try {
					plannedStartOfWork = df.parse(from);
				}
				catch (ParseException e) {
					errors.put("plannedStartOfWorkError", "Das Datumsformat von Dienst von ist nicht korreckt.");
					valid = false;
				}
			}
				
			Date plannedEndOfWork = null;
			if (dateToString == null || dateToString.equals("") || timeToHoursString == null || timeToMinutesString == null) {
				errors.put("plannedEndOfWorkMissing", "Dienst bis ist ein Pflichtfeld.");
				valid = false;
			} else {
				try {
					plannedEndOfWork = df.parse(to);
				} catch (ParseException e) {	
					errors.put("plannedEndOfWorkError", "Das Datumsformat von Dienst bis ist nicht korreckt.");
					valid = false;
				}
			}
			
			if (plannedStartOfWork != null) {
				if (plannedStartOfWork.getTime() < rangeStartCalendar.getTimeInMillis()) {
					errors.put("plannedStartOfWorkTooSmall", "Der Wert von Dienst von ist zu klein.");
					valid = false;
				}
				if (plannedStartOfWork.getTime() > rangeEndCalendar.getTimeInMillis()) {
					errors.put("plannedStartOfWorkTooBig", "Der Wert von Dienst von ist zu groß.");
					valid = false;
				}
			}
			
			if (plannedEndOfWork != null) {
				if (plannedEndOfWork.getTime() < rangeStartCalendar.getTimeInMillis()) {
					errors.put("plannedEndOfWorkTooSmall", "Der Wert von Dienst bis ist zu klein.");
					valid = false;
				}
				if (plannedEndOfWork.getTime() > rangeEndCalendar.getTimeInMillis()) {
					errors.put("plannedEndOfWorkTooBig", "Der Wert von Dienst bis ist zu groß.");
					valid = false;
				}
			}
			
			if (plannedStartOfWork != null && plannedEndOfWork != null) {
				if (plannedStartOfWork.getTime() >= plannedEndOfWork.getTime()) {
					errors.put("period", "Dienst von muss größer sein als Dienst bis.");
					valid = false;
				}
			}
			
			if (valid) {
				final RosterEntry rosterEntry = new RosterEntry();
				rosterEntry.setStation(location);
				rosterEntry.setStaffMember(staffMember);
				rosterEntry.setPlannedStartOfWork(plannedStartOfWork.getTime());
				rosterEntry.setPlannedEndOfWork(plannedEndOfWork.getTime());
				rosterEntry.setServicetype(serviceType);
				rosterEntry.setJob(job);
				rosterEntry.setCreatedByUsername(userSession.getUsername());
				if (comment != null && !comment.equals("")) {
					rosterEntry.setRosterNotes(comment);
				}
				rosterEntry.setStandby(standby);
				connection.sendAddRequest(RosterEntry.ID, rosterEntry);
				if(!connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				params.put("addedCount", 1);
				userSession.getFormDefaultValues().setDefaultLocation(location);
				userSession.getFormDefaultValues().setDefaultDate(plannedStartOfWork);
			}
		}
		params.put("errors", errors);
		return params;
	}
}