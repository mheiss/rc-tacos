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
import at.rc.tacos.web.form.RosterEntryContainer;
import at.rc.tacos.web.session.UserSession;

/**
 * Register Roster Entry Controller
 * @author Payer Martin
 * @version 1.0
 */
public class RegisterRosterEntryController extends Controller {

	private static final String ACTION_NAME = "action";
	private static final String ACTION_REGISTER = "register";
	private static final String ACTION_SIGN_OFF = "signOff";
	
	private static final String PARAM_ROSTER_ENTRY_NAME = "rosterEntryId";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String PARAM_MESSAGE_CODE_REGISTERED = "registered";
	private static final String PARAM_MESSAGE_CODE_SIGNED_OFF = "signedOff";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		final ResourceBundle views = ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH);
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Check if request is internal
		if (!userSession.isInternalSession()) {
			throw new IllegalArgumentException("Error: This URL must be called from internal net.");
		}
		
		// Get Id
		int rosterEntryId = 0;
		final String paramRosterEntryId = request.getParameter(PARAM_ROSTER_ENTRY_NAME);
		if (paramRosterEntryId == null || paramRosterEntryId.equals("")) {
			throw new IllegalArgumentException("Error: This URL must be called with Roster Entry ID.");
		}
		rosterEntryId = Integer.parseInt(paramRosterEntryId);
		
		// Get Roster Entry By Id
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
		
		// Create Roster Entry Container
		final RosterEntryContainer rosterEntryContainer = new RosterEntryContainer();
		rosterEntryContainer.setRosterEntry(rosterEntry);
		rosterEntryContainer.setPlannedStartOfWork(new Date(rosterEntry.getPlannedStartOfWork()));
		rosterEntryContainer.setPlannedEndOfWork(new Date(rosterEntry.getPlannedEndOfWork()));
		if (rosterEntry.getRealStartOfWork() == 0) {
			rosterEntryContainer.setRealStartOfWork(null);
		} else {
			rosterEntryContainer.setRealStartOfWork(new Date(rosterEntry.getRealStartOfWork()));
		}
		if (rosterEntry.getRealEndOfWork() == 0) {
			rosterEntryContainer.setRealEndOfWork(null);
		} else {
			rosterEntryContainer.setRealEndOfWork(new Date(rosterEntry.getRealEndOfWork()));
		}
		final Calendar registerStartCalendar = Calendar.getInstance();
		registerStartCalendar.setTime(rosterEntryContainer.getPlannedStartOfWork());
		registerStartCalendar.set(Calendar.HOUR, registerStartCalendar.get(Calendar.HOUR) - 24);
		rosterEntryContainer.setRegisterStart(registerStartCalendar.getTime());
		
		// Get current date
		final Date currDate = new Date();
		
		// Get Action
		final String action = request.getParameter(ACTION_NAME);
		String messageCode = "";
		
		if (action.equals(ACTION_REGISTER)) {
			// Check state of roster entry
			if (rosterEntryContainer.getPlannedStartOfWork() == null || rosterEntryContainer.getPlannedEndOfWork() == null || rosterEntryContainer.getRealStartOfWork() != null || rosterEntryContainer.getRealEndOfWork() != null) {
				throw new IllegalArgumentException("Error: Roster Entry has an illegal state.");
			}
			
			//Check register start if authorization eq Benutzer
			if (authorization.equals(Login.AUTH_USER)) {
				if (currDate.getTime() < rosterEntryContainer.getRegisterStart().getTime()) {
					throw new IllegalArgumentException("Error: Earliest possible register timestamp has not been reached yet.");
				}
			}
			rosterEntry.setRealStartOfWork(currDate.getTime());
			connection.sendUpdateRequest(RosterEntry.ID, rosterEntry);
			if(!connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
				throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			}
			messageCode = PARAM_MESSAGE_CODE_REGISTERED;
			
		} else if (action.equals(ACTION_SIGN_OFF)) {
			// Check state of roster entry
			if (rosterEntryContainer.getPlannedStartOfWork() == null || rosterEntryContainer.getPlannedEndOfWork() == null || rosterEntryContainer.getRealStartOfWork() == null || rosterEntryContainer.getRealEndOfWork() != null) {
				throw new IllegalArgumentException("Error: Roster Entry has an illegal state.");
			}
			
			//Check planned end of work if authorization eq Benutzer
			if (authorization.equals(Login.AUTH_USER)) {
				if (currDate.getTime() < rosterEntryContainer.getPlannedEndOfWork().getTime()) {
					throw new IllegalArgumentException("Error: Earliest possible sign off timestamp has not been reached yet.");
				}			
			}
			rosterEntry.setRealEndOfWork(currDate.getTime());
			connection.sendUpdateRequest(RosterEntry.ID, rosterEntry);
			if(!connection.getContentType().equalsIgnoreCase(RosterEntry.ID)) {
				throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			}
			messageCode = PARAM_MESSAGE_CODE_SIGNED_OFF;
		}
		
		String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + views.getString("roster.url") + "?" + PARAM_MESSAGE_CODE_NAME + "=" + messageCode;
		
		System.out.println("Redirect: " + response.encodeRedirectURL(url));
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
		response.sendRedirect(response.encodeRedirectURL(url));
		
		/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
		context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
		
		return new HashMap<String, Object>();
	}

}
