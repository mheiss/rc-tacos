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
import at.rc.tacos.model.Link;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.web.session.UserSession;

/**
 * Delete Link Controller
 * @author Payer Martin
 * @version 1.0
 */
public class DeleteLinkController extends Controller {

	private static final String PARAM_LINK_NAME = "linkId";
	
	private static final String PARAM_SAVED_URL_NAME = "savedUrl";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String PARAM_MESSAGE_CODE_DELETED = "deleted";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Check authorization
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
		
		// Link Id
		int linkId = 0;
		final String paramLinkId = request.getParameter(PARAM_LINK_NAME);
		if (paramLinkId == null || paramLinkId.equals("")) {
			throw new IllegalArgumentException("Error: This URL must be called with Link ID.");
		}
		linkId = Integer.parseInt(paramLinkId);
		
		// Link
		Link link = null;
		final QueryFilter linkFilter = new QueryFilter();
		linkFilter.add(IFilterTypes.ID_FILTER, Integer.toString(linkId));
		final List<AbstractMessage> linkList = connection.sendListingRequest(Link.ID, linkFilter);
		if (!Link.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		link = (Link)linkList.get(0);
		
		// Link must not be null
		if (link == null) {
			throw new IllegalArgumentException("Error: Link must not be null.");
		}
		
		// Get saved url
		final String savedUrl = request.getParameter(PARAM_SAVED_URL_NAME);
		
		connection.sendRemoveRequest(Link.ID, link);
		if(!connection.getContentType().equalsIgnoreCase(Link.ID)) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		
		String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + savedUrl + "?" + PARAM_MESSAGE_CODE_NAME + "=" + PARAM_MESSAGE_CODE_DELETED;
		
		System.out.println("Redirect: " + response.encodeRedirectURL(url));
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
		response.sendRedirect(response.encodeRedirectURL(url));
		
		return new HashMap();
	}

}
