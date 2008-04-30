package at.rc.tacos.web.web;

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

public class AddRosterEntryController extends Controller {

	public static final String ACTION_ADD_ROSTER_ENTRY = "addRosterEntry";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		// Get current login information from server
		Login login = null;
		final QueryFilter usernameFilter = new QueryFilter();
		usernameFilter.add(IFilterTypes.USERNAME_FILTER, userSession.getLoginInformation().getUsername());
		final List<AbstractMessage> loginList = connection.sendListingRequest(Login.ID, usernameFilter);
		if (Login.ID.equalsIgnoreCase(connection.getContentType())) {
			login = (Login)loginList.get(0);
		}
		userSession.getLoginInformation().setAuthorization(login.getAuthorization());
		userSession.getLoginInformation().setUserInformation(login.getUserInformation());
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Job List
		final String paramJobId = request.getParameter("jobId");
		int jobId = 0;
		Job job = null;
		if (paramJobId != null && !paramJobId.equals("")) {
			jobId = Integer.parseInt(paramJobId);
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
			staffMemberId = Integer.parseInt(paramStaffMemberId);
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
		Location location = null;
		if (paramLocationId != null && !paramLocationId.equals("")) {
			locationId = Integer.parseInt(paramLocationId);
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
			serviceTypeId = Integer.parseInt(paramServiceTypeId);
		}
		List<AbstractMessage> serviceTypeList = new ArrayList<AbstractMessage>();
		if (authorization.equals(Login.AUTH_ADMIN)) {
			serviceTypeList = connection.sendListingRequest(ServiceType.ID, null);
		} else if (authorization.equals(Login.AUTH_USER)) {
			final QueryFilter filter = new QueryFilter();
			filter.add(IFilterTypes.SERVICETYPE_FILTER, ServiceType.SERVICETYPE_FREIWILLIG);
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
		final String comment = request.getParameter("comment");
		params.put("comment", comment);
		
		
		// Get From
		final String dateFrom = request.getParameter("dateFrom");
		final String timeFromHours = request.getParameter("timeFromHours");
		final String timeFromMinutes = request.getParameter("timeFromMinutes");
		params.put("dateFrom", dateFrom);
		params.put("timeFromHours", timeFromHours);
		params.put("timeFromMinutes", timeFromMinutes);
		final String from = dateFrom + " " + timeFromHours + ":" + timeFromMinutes;
			
		// Get To
		final String dateTo = request.getParameter("dateTo");
		final String timeToHours = request.getParameter("timeToHours");
		final String timeToMinutes = request.getParameter("timeToMinutes");
		params.put("dateTo", dateTo);
		params.put("timeToHours", timeToHours);
		params.put("timeToMinutes", timeToMinutes);
		final String to = dateTo + " " + timeToHours + ":" + timeToMinutes;
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 10;
		final int rangeEnd = calendar.get(Calendar.YEAR) + 1;
		params.put("calendarDefaultDateMilliseconds", calendar.getTimeInMillis());
		params.put("calendarRangeStart", rangeStart);
		params.put("calendarRangeEnd", rangeEnd);
		
		
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
			try {
				plannedStartOfWork = df.parse(from);
			}
			catch (ParseException e) {
				errors.put("plannedStartOfWork", "Dienst von ist ein Pflichtfeld.");
				valid = false;
			}
				
			Date plannedEndOfWork = null;
			try {
				plannedEndOfWork = df.parse(to);
			} catch (ParseException e) {	
				errors.put("plannedEndOfWork", "Dienst bis ist ein Pflichtfeld.");
				valid = false;
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
			}
		}
		params.put("errors", errors);
		return params;
	}

}
