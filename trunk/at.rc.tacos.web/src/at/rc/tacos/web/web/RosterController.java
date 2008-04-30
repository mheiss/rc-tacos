package at.rc.tacos.web.web;

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
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;

public class RosterController extends Controller {

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
		
		// Location List
		final String paramLocationId = request.getParameter("locationId");
		int locationId = 0;
		Location location = userSession.getLoginInformation().getUserInformation().getPrimaryLocation();
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
		
		// Create Calendar for DatePicker
		final Calendar calendar = Calendar.getInstance();
		final int rangeStart = calendar.get(Calendar.YEAR) - 10;
		final int rangeEnd = calendar.get(Calendar.YEAR) + 1;
		params.put("calendarDefaultDateMilliseconds", calendar.getTimeInMillis());
		params.put("calendarRangeStart", rangeStart);
		params.put("calendarRangeEnd", rangeEnd);
		
		return params;
		
	}

}
