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
import java.util.ResourceBundle;

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
import at.rc.tacos.web.form.RosterEntryContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Edit Roster Entry Controller
 * @author Payer Martin
 * @version 1.0
 */
public class EditRosterEntryController extends Controller {

	private static final String ACTION_NAME = "action";
	private static final String ACTION_UPDATE_ROSTER_ENTRY = "updateRosterEntry";
	
	private static final String PARAM_ROSTER_ENTRY_NAME = "rosterEntryId";
	private static final String MODEL_ROSTER_ENTRY_NAME = "rosterEntry";
	
	private static final String PARAM_SAVED_URL_NAME = "savedUrl";
	private static final String MODEL_SAVED_URL_NAME = "savedUrl";
	
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
	
	private static final String PARAM_STANDBY_HIDDEN_NAME = "standbyHidden";
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
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String PARAM_MESSAGE_CODE_EDITED = "edited";
	
	private static final String MODEL_ERRORS_NAME = "errors";
	
	private static final String ERRORS_JOB = "job";
	private static final String ERRORS_JOB_VALUE = "Verwendung ist ein Pflichtfeld.";
	
	private static final String ERRORS_LOCATION = "location";
	private static final String ERRORS_LOCATION_VALUE = "Ortstelle ist ein Pflichtfeld.";
	
	private static final String ERRORS_STAFF_MEMBER = "staffMember";
	private static final String ERRORS_STAFF_MEMBER_VALUE = "Mitarbeiter ist ein Pflichtfeld.";
	
	private static final String ERRORS_SERVICE_TYPE = "serviceType";
	private static final String ERRORS_SERVICE_TYPE_VALUE = "Dienstverh‰ltnis ist ein Pflichtfeld.";
	
	private static final String ERRORS_PLANNED_START_OF_WORK_MISSING = "plannedStartOfWorkMissing";
	private static final String ERRORS_PLANNED_START_OF_WORK_MISSING_VALUE = "Dienst von ist ein Pflichtfeld.";	
	private static final String ERRORS_PLANNED_START_OF_WORK_ERROR = "plannedStartOfWorkError";
	private static final String ERRORS_PLANNED_START_OF_WORK_ERROR_VALUE = "Das Datumsformat von Dienst von ist nicht korrekt.";
	
	private static final String ERRORS_PLANNED_END_OF_WORK_MISSING = "plannedEndOfWorkMissing";
	private static final String ERRORS_PLANNED_END_OF_WORK_MISSING_VALUE = "Dienst bis ist ein Pflichtfeld.";
	private static final String ERRORS_PLANNED_END_OF_WORK_ERROR = "plannedEndOfWorkError";
	private static final String ERRORS_PLANNED_END_OF_WORK_ERROR_VALUE = "Das Datumsformat von Dienst bis ist nicht korrekt.";
	
	private static final String ERRORS_PLANNED_START_OF_WORK_TOO_SMALL = "plannedStartOfWorkTooSmall";
	private static final String ERRORS_PLANNED_START_OF_WORK_TOO_SMALL_VALUE = "Der Wert von Dienst von ist zu klein.";
	private static final String ERRORS_PLANNED_START_OF_WORK_TOO_BIG = "plannedStartOfWorkTooBig";
	private static final String ERRORS_PLANNED_START_OF_WORK_TOO_BIG_VALUE = "Der Wert von Dienst von ist zu groﬂ.";
	private static final String ERRORS_PLANNED_START_OF_WORK_DEADLINE_EXCEEDED = "plannedStartOfWorkDeadlineExceeded";
	private static final String ERRORS_PLANNED_START_OF_WORK_DEADLINE_EXCEEDED_VALUE = "Von muss mindestens 72 Stunden in der Zukunft liegen.";
	
	private static final String ERRORS_PLANNED_END_OF_WORK_TOO_SMALL = "plannedEndOfWorkTooSmall";
	private static final String ERRORS_PLANNED_END_OF_WORK_TOO_SMALL_VALUE = "plannedEndOfWorkTooSmall";
	private static final String ERRORS_PLANNED_END_OF_WORK_TOO_BIG = "plannedEndOfWorkTooBig";
	private static final String ERRORS_PLANNED_END_OF_WORK_TOO_BIG_VALUE = "Der Wert von Dienst bis ist zu groﬂ.";
	
	private static final String ERRORS_PERIOD = "period";
	private static final String ERRORS_PERIOD_VALUE = "Dienst von muss grˆﬂer sein als Dienst bis.";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		final ResourceBundle views = ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH);
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Roster Entry Id
		int rosterEntryId = 0;
		final String paramRosterEntryId = request.getParameter(PARAM_ROSTER_ENTRY_NAME);
		if (paramRosterEntryId == null || paramRosterEntryId.equals("")) {
			throw new IllegalArgumentException("Error: This URL must be called with Roster Entry ID.");
		}
		rosterEntryId = Integer.parseInt(paramRosterEntryId);
		
		// Roster Entry
		RosterEntry rosterEntry = null;
		final QueryFilter rosterFilter = new QueryFilter();
		rosterFilter.add(IFilterTypes.ID_FILTER, Integer.toString(rosterEntryId));
		final List<AbstractMessage> rosterEntryList = connection.sendListingRequest(RosterEntry.ID, rosterFilter);
		if (!RosterEntry.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		rosterEntry = (RosterEntry)rosterEntryList.get(0);
		
		// Roster Entry must not be null
		if (rosterEntry == null) {
			throw new IllegalArgumentException("Error: Roster Entry must not be null.");
		}
		
		// If authorization eq Benutzer and ServiceType neq Freiwillig throw Exception
		if (authorization.equals(Login.AUTH_USER) && !rosterEntry.getServicetype().getServiceName().equals(ServiceType.SERVICETYPE_FREIWILLIG)) {
			throw new IllegalArgumentException("Error: User has no permission for object.");
		}
		
		// Check deadline if authorization eq Benutzer
		if (authorization.equals(Login.AUTH_USER)) {
			final Calendar deadlineCalendar = Calendar.getInstance();
			deadlineCalendar.setTime(new Date(rosterEntry.getPlannedStartOfWork()));
			deadlineCalendar.set(Calendar.HOUR, deadlineCalendar.get(Calendar.HOUR) - RosterEntryContainer.EDIT_ROSTER_ENTRY_DEADLINE_HOURS);
			final Date currDate = new Date();
			if (currDate.getTime() > deadlineCalendar.getTimeInMillis()) {
				throw new IllegalArgumentException("Error: Deadline for Roster Entry exceeded.");
			}
		}		
		params.put(MODEL_ROSTER_ENTRY_NAME, rosterEntry);
		
		// Get saved url
		final String savedUrl = request.getParameter(PARAM_SAVED_URL_NAME);
		params.put(MODEL_SAVED_URL_NAME, savedUrl);
		
		// Job
		final String paramJobId = request.getParameter(PARAM_JOB_NAME);
		int jobId = 0;
		final Job defaultJob = rosterEntry.getJob();
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
		
		
		// Staff Member (depends on Job)
		final String paramStaffMemberId = request.getParameter(PARAM_STAFF_MEMBER_NAME);
		int staffMemberId = 0;	
		StaffMember defaultStaffMember = rosterEntry.getStaffMember();	
		StaffMember staffMember = null;
		if (paramStaffMemberId != null && !paramStaffMemberId.equals("") && !paramStaffMemberId.equalsIgnoreCase(PARAM_STAFF_MEMBER_NO_VALUE)) {
			staffMemberId = Integer.parseInt(paramStaffMemberId);		
		}
		final List<AbstractMessage> staffList = new ArrayList<AbstractMessage>();
		final List<AbstractMessage> staffListTemp = connection.sendListingRequest(StaffMember.ID, null);
		if (!StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		final Job defaulJob = (Job)params.get(MODEL_JOB_NAME);
		for (final Iterator<AbstractMessage> itStaffList = staffListTemp.iterator(); itStaffList.hasNext();) {
			final StaffMember sm = (StaffMember)itStaffList.next();
			if (defaulJob != null) {
				boolean hasCompetence = false;
				final List<Competence> competenceList = sm.getCompetenceList();
				for (final Iterator<Competence> itCompetenceList = competenceList.iterator(); itCompetenceList.hasNext();) {
					final Competence competence = itCompetenceList.next();
					if (competence.getId() == defaulJob.getId() || competence.getCompetenceName().equals(defaulJob.getJobName())) {
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
				
		// Location
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		final Location defaultLocation = rosterEntry.getStation();
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
		
		
		// Service Type
		final String paramServiceTypeId = request.getParameter(PARAM_SERVICE_TYPE_NAME);
		int serviceTypeId = 0;
		final ServiceType defaultServiceType = rosterEntry.getServicetype();
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
		params.put(MODEL_SERVICE_TYPE_LIST_NAME, serviceTypeList);
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
		
		// Standby
		final String paramStandbyHidden = request.getParameter(PARAM_STANDBY_HIDDEN_NAME);
		boolean defaultStandby = rosterEntry.getStandby();
		boolean standby = false;
		if (paramStandbyHidden != null) {
			if (paramStandbyHidden.equalsIgnoreCase("true")) {
				standby = true;
			} else {
				standby = false;
			}		
			params.put(MODEL_STANDBY_NAME, standby);
		} else {
			params.put(MODEL_STANDBY_NAME, defaultStandby);
		}
		
		// Comment
		final String defaultComment = rosterEntry.getRosterNotes();
		String comment = null;
		if (request.getParameter(PARAM_COMMENT_NAME)!= null) {
			comment = request.getParameter(PARAM_COMMENT_NAME);
		}
		if (comment != null) {
			params.put(MODEL_COMMENT_NAME, comment);
		} else {
			params.put(MODEL_COMMENT_NAME, defaultComment);
		}
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET;
		final int rangeEnd = calendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET;
		params.put(MODEL_CALENDAR_DEFAULT_DATE_MILLISECONDS_NAME, calendar.getTimeInMillis());
		params.put(MODEL_CALENDAR_RANGE_START_NAME, rangeStart);
		params.put(MODEL_CALENDAR_RANGE_END_NAME, rangeEnd);
		
		// From
		final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		final SimpleDateFormat sdfTimeHours = new SimpleDateFormat("HH");
		final SimpleDateFormat sdfTimeMinutes = new SimpleDateFormat("mm");
		
		String dateFromString = null;
		String timeFromHoursString = null;
		String timeFromMinutesString = null;
		
		final String defaultDateFromString = sdfDate.format(new Date(rosterEntry.getPlannedStartOfWork()));
		final String defaultTimeFromHoursString = sdfTimeHours.format(new Date(rosterEntry.getPlannedStartOfWork()));
		final String defaultTimeFromMinutesString = sdfTimeMinutes.format(new Date(rosterEntry.getPlannedStartOfWork()));
		
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
			
		// To
		String dateToString = null;
		String timeToHoursString = null;
		String timeToMinutesString = null;
		
		final String defaultDateToString = sdfDate.format(new Date(rosterEntry.getPlannedEndOfWork()));
		final String defaultTimeToHoursString = sdfTimeHours.format(new Date(rosterEntry.getPlannedEndOfWork()));
		final String defaultTimeToMinutesString = sdfTimeMinutes.format(new Date(rosterEntry.getPlannedEndOfWork()));
		
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
				
		// Do Action
		final String action = request.getParameter(ACTION_NAME);
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_UPDATE_ROSTER_ENTRY)) {
			if (job == null) {
				errors.put(ERRORS_JOB, ERRORS_JOB_VALUE);
				valid = false;
			}
			if (location == null) {
				errors.put(ERRORS_LOCATION, ERRORS_LOCATION_VALUE);
				valid = false;
			}
			if (staffMember == null) {
				errors.put(ERRORS_STAFF_MEMBER, ERRORS_STAFF_MEMBER_VALUE);
				valid = false;
			}
			if (serviceType == null) {
				errors.put(ERRORS_SERVICE_TYPE, ERRORS_SERVICE_TYPE_VALUE);
				valid = false;
			}
			
			final Calendar rangeStartCalendar = Calendar.getInstance();
			rangeStartCalendar.set(Calendar.YEAR, rangeStartCalendar.get(Calendar.YEAR) - MODEL_CALENDAR_RANGE_START_OFFSET);
			
			final Calendar rangeEndCalendar = Calendar.getInstance();
			rangeEndCalendar.set(Calendar.YEAR, rangeEndCalendar.get(Calendar.YEAR) + MODEL_CALENDAR_RANGE_END_OFFSET);
			
			final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			Date plannedStartOfWork = null;
			if (dateFromString == null || dateFromString.equals("") || timeFromHoursString == null || timeFromMinutesString == null) {
				errors.put(ERRORS_PLANNED_START_OF_WORK_MISSING, ERRORS_PLANNED_START_OF_WORK_MISSING_VALUE);
				valid = false;
			} else {
				try {
					plannedStartOfWork = df.parse(from);
				}
				catch (ParseException e) {
					errors.put(ERRORS_PLANNED_START_OF_WORK_ERROR, ERRORS_PLANNED_START_OF_WORK_ERROR_VALUE);
					valid = false;
				}
			}
				
			Date plannedEndOfWork = null;
			if (dateToString == null || dateToString.equals("") || timeToHoursString == null || timeToMinutesString == null) {
				errors.put(ERRORS_PLANNED_END_OF_WORK_MISSING, ERRORS_PLANNED_END_OF_WORK_MISSING_VALUE);
				valid = false;
			} else {
				try {
					plannedEndOfWork = df.parse(to);
				} catch (ParseException e) {	
					errors.put(ERRORS_PLANNED_END_OF_WORK_ERROR, ERRORS_PLANNED_END_OF_WORK_ERROR_VALUE);
					valid = false;
				}
			}
			
			final Calendar deadlineCalendar = Calendar.getInstance();
			deadlineCalendar.setTime(plannedStartOfWork);
			deadlineCalendar.set(Calendar.HOUR, deadlineCalendar.get(Calendar.HOUR) - RosterEntryContainer.EDIT_ROSTER_ENTRY_DEADLINE_HOURS);
			
			if (plannedStartOfWork != null) {
				if (plannedStartOfWork.getTime() < rangeStartCalendar.getTimeInMillis()) {
					errors.put(ERRORS_PLANNED_START_OF_WORK_TOO_SMALL, ERRORS_PLANNED_START_OF_WORK_TOO_SMALL_VALUE);
					valid = false;
				} else if (authorization.equals(Login.AUTH_USER) && new Date().getTime() > deadlineCalendar.getTimeInMillis()) {
					errors.put(ERRORS_PLANNED_START_OF_WORK_DEADLINE_EXCEEDED, ERRORS_PLANNED_START_OF_WORK_DEADLINE_EXCEEDED_VALUE);
					valid = false;
				} else if (plannedStartOfWork.getTime() > rangeEndCalendar.getTimeInMillis()) {
					errors.put(ERRORS_PLANNED_START_OF_WORK_TOO_BIG, ERRORS_PLANNED_START_OF_WORK_TOO_BIG_VALUE);
					valid = false;
				}
			}
			
			if (plannedEndOfWork != null) {
				if (plannedEndOfWork.getTime() < rangeStartCalendar.getTimeInMillis()) {
					errors.put(ERRORS_PLANNED_END_OF_WORK_TOO_SMALL, ERRORS_PLANNED_END_OF_WORK_TOO_SMALL_VALUE);
					valid = false;
				} else if (plannedEndOfWork.getTime() > rangeEndCalendar.getTimeInMillis()) {
					errors.put(ERRORS_PLANNED_END_OF_WORK_TOO_BIG, ERRORS_PLANNED_END_OF_WORK_TOO_BIG_VALUE);
					valid = false;
				}
			}
			
			if (plannedStartOfWork != null && plannedEndOfWork != null) {
				if (plannedStartOfWork.getTime() >= plannedEndOfWork.getTime()) {
					errors.put(ERRORS_PERIOD, ERRORS_PERIOD_VALUE);
					valid = false;
				}
			}
			
			if (valid) {
				rosterEntry.setStation(location);
				rosterEntry.setStaffMember(staffMember);
				rosterEntry.setPlannedStartOfWork(plannedStartOfWork.getTime());
				rosterEntry.setPlannedEndOfWork(plannedEndOfWork.getTime());
				rosterEntry.setServicetype(serviceType);
				rosterEntry.setJob(job);
				rosterEntry.setCreatedByUsername(userSession.getLoginInformation().getUsername());
				if (comment != null && !comment.equals("")) {
					rosterEntry.setRosterNotes(comment);
				}
				rosterEntry.setStandby(standby);
				connection.sendUpdateRequest(RosterEntry.ID, rosterEntry);
				if(!connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				
				userSession.getDefaultFormValues().setDefaultJob(job);
				userSession.getDefaultFormValues().setDefaultStaffMember(staffMember);
				userSession.getDefaultFormValues().setDefaultLocation(location);
				userSession.getDefaultFormValues().setDefaultServiceType(serviceType);
				userSession.getDefaultFormValues().setDefaultStandBy(standby);
				userSession.getDefaultFormValues().setDefaultDate(plannedStartOfWork);

				String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + views.getString("roster.url") + "?" + PARAM_MESSAGE_CODE_NAME + "=" + PARAM_MESSAGE_CODE_EDITED;
				
				System.out.println("Redirect: " + response.encodeRedirectURL(url));
				System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
				response.sendRedirect(response.encodeRedirectURL(url));
				
				/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
				context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
			} 
		}
		params.put(MODEL_ERRORS_NAME, errors);
		return params;
	}
}