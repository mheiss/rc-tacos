package at.rc.tacos.web.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

public class RosterDayController2 extends Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		final WebClient connection = userSession.getConnection();
		
		final List<AbstractMessage> staffList = connection.sendListingRequest(StaffMember.ID, null);
		if (StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("staffList", staffList);
		}
		
		final List<AbstractMessage> locationList = connection.sendListingRequest(Location.ID, null);
		if (Location.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("locationList", locationList);
		}
		
		final List<AbstractMessage> jobList = connection.sendListingRequest(Job.ID, null);
		if (Job.ID.equalsIgnoreCase(connection.getContentType())) {
			params.put("jobList", jobList);
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
		
		return params;
	}

}
