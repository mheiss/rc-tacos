package at.rc.tacos.web.controller;

import java.util.ArrayList;
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

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.QueryFilter;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.web.container.StaffMemberListContainer;
import at.rc.tacos.web.net.WebClient;
import at.rc.tacos.web.session.UserSession;

/**
 * Staff Members Controller
 * @author Payer Martin
 * @version 1.0
 */
public class StaffMembersController extends Controller {

	private static final String MODEL_TIMESTAMP_NAME = "timestamp";
	
	private static final String PARAM_LOCATION_NAME = "locationId";
	private static final String PARAM_LOCATION_NO_VALUE = "noValue";
	private static final String MODEL_LOCATION_NAME = "location";
	private static final String MODEL_LOCATION_LIST_NAME = "locationList";
	
	private static final String MODEL_STAFF_MEMBER_LIST_CONTAINER = "staffMemberListContainer";
	
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
		
		// Create timestamp
		params.put(MODEL_TIMESTAMP_NAME, new Date().getTime());
		
		// Location List
		final String paramLocationId = request.getParameter(PARAM_LOCATION_NAME);
		int locationId = 0;
		Location location = userSession.getLoginInformation().getUserInformation().getPrimaryLocation();
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
		params.put(MODEL_LOCATION_NAME, location);
		
		// Staff Member List
		final QueryFilter staffMembersFilter = new QueryFilter();	
		if (location != null) {
			staffMembersFilter.add(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER, Integer.toString(location.getId()));
		}
		final List<AbstractMessage> staffMemberList = connection.sendListingRequest(StaffMember.ID, staffMembersFilter);
		if (!StaffMember.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		
		final List<Login> loginList = new ArrayList<Login>();
		for (AbstractMessage am : staffMemberList) {
			final StaffMember sm = (StaffMember)am;
			final QueryFilter loginFilter = new QueryFilter();
			loginFilter.add(IFilterTypes.USERNAME_FILTER, sm.getUserName());
			final List<AbstractMessage> loginListTemp = connection.sendListingRequest(Login.ID, loginFilter);
			if (!Login.ID.equalsIgnoreCase(connection.getContentType())) {
				throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			}
			final Login login = (Login)loginListTemp.get(0);
			loginList.add(login);
		}
		
		// Group and Sort
		final StaffMemberListContainer container = new StaffMemberListContainer(loginList);
		final Comparator<Location> locationComparator = new PropertyComparator("locationName", true, true);
		final Comparator sortComp = new CompoundComparator(new Comparator[] {
				new PropertyComparator("islocked", true, true),
				new PropertyComparator("userInformation.lastName", true, true),
				new PropertyComparator("userInformation.firstName", true, true)
		});
		container.groupStaffMembersBy(locationComparator);
		container.sortRosterEntries(sortComp);
		
		params.put(MODEL_STAFF_MEMBER_LIST_CONTAINER, container);
		
		return params;
	}

}
