package at.rc.tacos.web.web;

import java.util.ArrayList;
import java.util.Calendar;
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

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		final WebClient connection = userSession.getConnection();
		
		// Job
		final String paramJobId = request.getParameter("jobId");
		int jobId = 0;
		Job job = null;
		if (paramJobId != null) {
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
		if (job != null) {
			params.put("job", job);
		}
		
		// Staff Member
		final String paramStaffMemberId = request.getParameter("staffMemberId");
		int staffMemberId = 0;
		StaffMember staffMember = null;
		if (paramStaffMemberId != null) {
			staffMemberId = Integer.parseInt(paramStaffMemberId);
		}
		final List<AbstractMessage> staffList = new ArrayList<AbstractMessage>();
		final List<AbstractMessage> staffListTemp = connection.sendListingRequest(StaffMember.ID, null);
		if (StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			if (job != null) {
				for (final Iterator<AbstractMessage> itStaffList = staffListTemp.iterator(); itStaffList.hasNext();) {
					final StaffMember sm = (StaffMember)itStaffList.next();
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
					if (sm.getStaffMemberId() == staffMemberId) {
						staffMember = sm;
					}
				}
			} else {
				staffList.addAll(staffListTemp);
			}
			params.put("staffList", staffList);
		}
		if (staffMember != null) {
			params.put("staffMember", staffMember);
		}
		
		// Location
		final String paramLocationId = request.getParameter("locationId");
		int locationId = 0;
		Location location = null;
		if (paramLocationId != null) {
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
		if (location != null) {
			params.put("location", location);
		}
		
		// Service Type
		final String paramServiceTypeId = request.getParameter("serviceTypeId");
		int serviceTypeId = 0;
		ServiceType serviceType = null;
		if (paramServiceTypeId != null) {
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
		}
		if (serviceType != null) {
			params.put("serviceType", serviceType);
		}
		
		// Create Calendar
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 100;
		final int rangeEnd = calendar.get(Calendar.YEAR);
		params.put("dateMilliseconds", calendar.getTimeInMillis());
		params.put("rangeStart", rangeStart);
		params.put("rangeEnd", rangeEnd);
		
		return params;
	}

}
