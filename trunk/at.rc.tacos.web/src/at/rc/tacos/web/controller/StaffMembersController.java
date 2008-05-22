package at.rc.tacos.web.controller;

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
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.session.UserSession;

/**
 * Staff Members Controller
 * @author Payer Martin
 * @version 1.0
 * TODO: Gruppierung um Location
 */
public class StaffMembersController extends Controller {

	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String MODEL_STAFF_MEMBER_LIST_NAME = "staffMemberList";
	
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
		
		// Location List
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		Location location = userSession.getDefaultFormValues().getDefaultLocation();
		if (paramLocationId != null && !paramLocationId.equals("")) {
			if (paramLocationId.equalsIgnoreCase(PARAM_LOCATION_NO_VALUE)) {
				location = null;
			} else {
				locationId = Integer.parseInt(paramLocationId);
			}
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
		userSession.getDefaultFormValues().setDefaultLocation(location);
		params.put(MODEL_LOCATION_NAME, location);
		
		// Staff Member List
		List<AbstractMessage> staffMemberList;
		
		if (location != null) {
			final QueryFilter staffMemberLocationFilter = new QueryFilter();
			staffMemberLocationFilter.add(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER, Integer.toString(location.getId()));
			staffMemberList = connection.sendListingRequest(StaffMember.ID, staffMemberLocationFilter);
		} else {
			staffMemberList = connection.sendListingRequest(StaffMember.ID, null);
		}
		if (!StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_STAFF_MEMBER_LIST_NAME, staffMemberList);
		
		return params;
	}

}
