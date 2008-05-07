package at.rc.tacos.web.controller;

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
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.web.session.UserSession;

/**
 * Register Roster Entry
 * @author Payer Martin
 * @version 1.0
 */
public class RegisterRosterEntryController extends Controller {

	private static final String ACTION_REGISTER = "register";
	private static final String ACTION_SIGN_OFF = "signOff";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		final ResourceBundle views = ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH);
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Get Id
		int rosterEntryId = 0;
		final String paramRosterEntryId = request.getParameter("rosterEntryId");
		if (paramRosterEntryId == null || paramRosterEntryId.equals("")) {
			throw new IllegalArgumentException("Fehler: Diese URL muss mit einer gültigen RosterEntry.rosterId als Parameter aufgerufen werden.");
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
			throw new IllegalArgumentException("Fehler: rosterEntry darf nicht null sein.");
		}
		
		// Get Action
		final String action = request.getParameter("action");
		
		if (action.equals(ACTION_REGISTER)) {
			
		} else if (action.equals(ACTION_SIGN_OFF)) {
			
		}
		
		return new HashMap();
	}

}
