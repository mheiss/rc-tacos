package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.web.net.WebClient;
import at.rc.tacos.web.session.UserSession;

/**
 * Links Controller
 * @author Payer Martin
 * @version 1.0
 */
public class LinksController extends Controller {
	
	private static final String MODEL_LINK_LIST_NAME = "linkList";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String MODEL_MESSAGE_CODE_NAME = "messageCode";

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
		
		// Get links
		final List<AbstractMessage> linkList = connection.sendListingRequest(Link.ID, null);
		if (!Link.ID.equalsIgnoreCase(connection.getContentType())) {
			throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
		}
		params.put(MODEL_LINK_LIST_NAME, linkList);
		
		// Parse message code from other controllers
		if (request.getParameter(PARAM_MESSAGE_CODE_NAME) != null && !request.getParameter(PARAM_MESSAGE_CODE_NAME).equals("")) {
			params.put(MODEL_MESSAGE_CODE_NAME, request.getParameter(PARAM_MESSAGE_CODE_NAME));
		}
		
		return params;
	}

}
