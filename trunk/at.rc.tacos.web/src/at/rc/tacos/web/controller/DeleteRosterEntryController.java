package at.rc.tacos.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.web.form.RosterEntryContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Delete Roster Entry Controller
 * @author Payer Martin
 * @version 1.0
 */
public class DeleteRosterEntryController extends Controller {

	private static final String MESSAGE_CODE_DELETED = "deleted";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		final ResourceBundle views = ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH);
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Get Id
		int rosterEntryId = 0;
		final String paramRosterEntryId = request.getParameter("rosterEntryId");
		if (paramRosterEntryId == null || paramRosterEntryId.equals("")) {
			throw new IllegalArgumentException("Error: This URL must be called with Roster Entry ID.");
		}
		rosterEntryId = Integer.parseInt(paramRosterEntryId);
		
		// Get Roster Entry By Id
		RosterEntry rosterEntry = null;
		final QueryFilter rosterFilter = new QueryFilter();
		rosterFilter.add(IFilterTypes.ID_FILTER, Integer.toString(rosterEntryId));
		final List<AbstractMessage> rosterEntryList = connection.sendListingRequest(RosterEntry.ID, rosterFilter);
		if (RosterEntry.ID.equalsIgnoreCase(connection.getContentType())) {
			rosterEntry = (RosterEntry)rosterEntryList.get(0);
		}
		
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
			deadlineCalendar.set(Calendar.HOUR, deadlineCalendar.get(Calendar.HOUR) - RosterEntryContainer.DEADLINE_HOURS);
			final Date currDate = new Date();
			if (currDate.getTime() > deadlineCalendar.getTimeInMillis()) {
				throw new IllegalArgumentException("Error: Deadline for Roster Entry exceeded.");
			}
		}
		
		connection.sendRemoveRequest(RosterEntry.ID, rosterEntry);
		if(!connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		
		String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + views.getString("roster.url") + "?messageCode=" + MESSAGE_CODE_DELETED;
		
		System.out.println("Redirect: " + response.encodeRedirectURL(url));
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
		response.sendRedirect(response.encodeRedirectURL(url));
		
		/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
		context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
		
		return new HashMap<String, Object>();
	}

}
