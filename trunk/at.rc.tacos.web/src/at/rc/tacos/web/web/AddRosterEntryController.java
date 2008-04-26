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
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		final WebClient connection = userSession.getConnection();
		
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
					}
				} else {
					staffList.add(sm);
				}
				if (sm.getStaffMemberId() == staffMemberId) {
					staffMember = sm;
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
		
		// Create Calendar
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 100;
		final int rangeEnd = calendar.get(Calendar.YEAR);
		params.put("dateMilliseconds", calendar.getTimeInMillis());
		params.put("rangeStart", rangeStart);
		params.put("rangeEnd", rangeEnd);
		
		
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
			
			final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			
			Date realStartOfWork = null;
			try {
				realStartOfWork = df.parse(from);
			}
			catch (ParseException e) {
				errors.put("realStartOfWork", "Dienst von ist ein Pflichtfeld.");
				valid = false;
			}
			
			Date realEndOfWork = null;
			try {
				realEndOfWork = df.parse(to);
			} catch (ParseException e) {	
				errors.put("realEndOfWork", "Dienst bis ist ein Pflichtfeld.");
				valid = false;
			}
			
			if (realStartOfWork != null && realEndOfWork != null) {
				if (realStartOfWork.getTime() >= realEndOfWork.getTime()) {
					errors.put("period", "Dienst von muss größer sein als Diens bis.");
				}
			}
			
			if (valid) {
				
			}
		}
		params.put("errors", errors);
		return params;
	}

}
