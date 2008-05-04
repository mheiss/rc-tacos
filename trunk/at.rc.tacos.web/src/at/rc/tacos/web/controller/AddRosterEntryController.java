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
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class AddRosterEntryController extends Controller {

	public static final String ACTION_ADD_ROSTER_ENTRY = "addRosterEntry";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 10;
		final int rangeEnd = calendar.get(Calendar.YEAR) + 1;
		params.put("calendarDefaultDateMilliseconds", calendar.getTimeInMillis());
		params.put("calendarRangeStart", rangeStart);
		params.put("calendarRangeEnd", rangeEnd);
		
		// Job List
		final String paramJobId = request.getParameter("jobId");
		int jobId = 0;
		Job job = null;
		if (paramJobId != null && !paramJobId.equals("")) {
			if (paramJobId.equalsIgnoreCase("noValue")) {
				job = null;
			} else {
				jobId = Integer.parseInt(paramJobId);
			}
		}
		final List<AbstractMessage> jobList = connection.sendListingRequest(Job.ID, null);
		if (Job.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("jobList", jobList);
			for (final Iterator<AbstractMessage> itJobList = jobList.iterator(); itJobList.hasNext();) {
				final Job j = (Job)itJobList.next();
				if (j.getId() == jobId) {
					job = j;
				}
			}
		}
		params.put("job", job);
		
		
		// Staff Member List
		final String paramStaffMemberId = request.getParameter("staffMemberId");
		int staffMemberId = 0;
		StaffMember staffMember = null;
		if (paramStaffMemberId != null && !paramStaffMemberId.equals("")) {
			if (paramStaffMemberId.equalsIgnoreCase("noValue")) {
				staffMember = null;
			} else {
				staffMemberId = Integer.parseInt(paramStaffMemberId);
			}
		}
		final List<AbstractMessage> staffList = new ArrayList<AbstractMessage>();
		final List<AbstractMessage> staffListTemp = connection.sendListingRequest(StaffMember.ID, null);
		if (StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
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
			params.put("staffList", staffList);
		}		
		params.put("staffMember", staffMember);
		
		
		// Location List
		final String paramLocationId = request.getParameter("locationId");
		int locationId = 0;
		Location location = userSession.getFormDefaultValues().getDefaultLocation();
		if (paramLocationId != null && !paramLocationId.equals("")) {
			if (paramLocationId.equalsIgnoreCase("noValue")) {
				location = null;
			} else {
				locationId = Integer.parseInt(paramLocationId);
			}
		}
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (Location.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("locationList", locationList);
			for (final Iterator<AbstractMessage> itLoactionList = locationList.iterator(); itLoactionList.hasNext();) {
				final Location l = (Location)itLoactionList.next();
				if (l.getId() == locationId) {
					location = l;
				}
			}
		}
		params.put("location", location);
		
		
		// Service Type List
		final String paramServiceTypeId = request.getParameter("serviceTypeId");
		int serviceTypeId = 0;
		ServiceType serviceType = null;
		if (paramServiceTypeId != null && !paramServiceTypeId.equals("")) {
			if (paramServiceTypeId.equalsIgnoreCase("noValue")) {
				serviceType = null;
			} else {
				serviceTypeId = Integer.parseInt(paramServiceTypeId);
			}
		}
		List<AbstractMessage> serviceTypeList = new ArrayList<AbstractMessage>();
		if (authorization.equals(Login.AUTH_ADMIN)) {
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, null);
		} else if (authorization.equals(Login.AUTH_USER)) {
			final QueryFilter filter = new QueryFilter();
			filter.add(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER, ServiceType.SERVICETYPE_FREIWILLIG);
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, filter);
		}	
		if (ServiceType.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("serviceTypeList", serviceTypeList);
			for (final Iterator<AbstractMessage> itServiceTypeList = serviceTypeList.iterator(); itServiceTypeList.hasNext();) {
				final ServiceType st = (ServiceType)itServiceTypeList.next();
				if (st.getId() == serviceTypeId) {
					serviceType = st;
				}
			}
		}	
		params.put("serviceType", serviceType);
		
		// Get Standby
		final String paramStandby = request.getParameter("standby");
		boolean standby = false;
		if (paramStandby != null) {
			standby = true;
		}
		params.put("standby", standby);
		
		// Get Comment
		String comment = null;
		if (request.getParameter("comment") != null) {
			comment = request.getParameter("comment");
		}
		params.put("comment", comment);
		
		// Get From
		final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
		final SimpleDateFormat sdfTimeHours = new SimpleDateFormat("HH");
		final SimpleDateFormat sdfTimeMinutes = new SimpleDateFormat("mm");
		
		String dateFromString = sdfDate.format(userSession.getFormDefaultValues().getDefaultDate());
		String timeFromHoursString = null;
		String timeFromMinutesString = null;
		
		if (request.getParameter("dateFrom") != null) {
			dateFromString = request.getParameter("dateFrom");
		}
		if (request.getParameter("timeFromHours") != null) {
			timeFromHoursString = request.getParameter("timeFromHours");
		}
		if (request.getParameter("timeFromMinutes") != null) {
			timeFromMinutesString = request.getParameter("timeFromMinutes");
		}
		params.put("dateFrom", dateFromString);
		params.put("timeFromHours", timeFromHoursString);
		params.put("timeFromMinutes", timeFromMinutesString);
		final String from = dateFromString + " " + timeFromHoursString + ":" + timeFromMinutesString;
			
		// Get To
		String dateToString = sdfDate.format(userSession.getFormDefaultValues().getDefaultDate());
		String timeToHoursString = null;
		String timeToMinutesString = null;
		
		if (request.getParameter("dateTo") != null) {
			dateToString = request.getParameter("dateTo");		
		}
		if (request.getParameter("timeToHours") != null) {
			timeToHoursString = request.getParameter("timeToHours");
		}
		if (request.getParameter("timeToMinutes") != null) {
			timeToMinutesString = request.getParameter("timeToMinutes");
		}
		params.put("dateTo", dateToString);
		params.put("timeToHours", timeToHoursString);
		params.put("timeToMinutes", timeToMinutesString);
		final String to = dateToString + " " + timeToHoursString + ":" + timeToMinutesString;
				
		// Get Action
		final String action = request.getParameter("action");
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
				if(connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
					params.put("addedCount", 1);
				}
			}
		}
		params.put("errors", errors);
		return params;
	}
}