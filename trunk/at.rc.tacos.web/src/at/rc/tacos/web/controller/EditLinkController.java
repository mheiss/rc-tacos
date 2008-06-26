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
 * Edit Link Controller
 * @author Payer Martin
 * @version 1.0
 */
public class EditLinkController extends Controller {

	private static final String PARAM_LINK_NAME = "linkId";
	private static final String MODEL_LINK_NAME = "link";
	
	private static final String PARAM_SAVED_URL_NAME = "savedUrl";
	private static final String MODEL_SAVED_URL_NAME = "savedUrl";
	
	private static final String PARAM_INNER_TEXT_NAME = "innerText";
	private static final String MODEL_INNER_TEXT_NAME = "innerText";
	
	private static final String PARAM_HREF_NAME = "href";
	private static final String MODEL_HREF_NAME = "href";
	
	private static final String PARAM_TITLE_NAME = "title";
	private static final String MODEL_TITLE_NAME = "title";
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_UPDATE_LINK = "updateLink";
	
	private static final String PARAM_MESSAGE_CODE_NAME = "messageCode";
	private static final String PARAM_MESSAGE_CODE_EDITED = "edited";
	
	private static final String MODEL_ERRORS_NAME = "errors";
		
	private static final String ERRORS_INNER_TEXT_MISSING = "innerTextMissing";
	private static final String ERRORS_INNER_TEXT_MISSING_VALUE = "Text ist ein Pflichtfeld.";
	private static final String ERRORS_INNER_TEXT_TOO_LONG = "innerTextTooLong";
	private static final String ERRORS_INNER_TEXT_TOO_LONG_VALUE = "Text ist zu lang. Es sind maximal 250 Zeichen erlaubt.";
	
	private static final String ERRORS_HREF_MISSING = "hrefMissing";
	private static final String ERRORS_HREF_MISSING_VALUE = "URL ist ein Pflichtfeld.";
	private static final String ERRORS_HREF_TOO_LONG = "hrefTooLong";
	private static final String ERRORS_HREF_TOO_LONG_VALUE = "URL ist zu lang. Es sind maximal 250 Zeichen erlaubt.";
	
	private static final String ERRORS_TITLE_TOO_LONG = "titleTooLong";
	private static final String ERRORS_TITLE_TOO_LONG_VALUE = "Beschreibung ist zu lang. Es sind maximal 1000 Zeichen erlaubt.";
	
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
		
		// Get saved url
		final String savedUrl = request.getParameter(PARAM_SAVED_URL_NAME);
		params.put(MODEL_SAVED_URL_NAME, savedUrl);
		
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
		
		params.put(MODEL_LINK_NAME, link);
		
		// Parse parameters
		String paramInnerText = request.getParameter(PARAM_INNER_TEXT_NAME);
		String paramHref = request.getParameter(PARAM_HREF_NAME);
		String paramTitle = request.getParameter(PARAM_TITLE_NAME);
		
		// Get default values
		final String defaultInnerText = link.getInnerText();
		final String defaultHref = link.getHref();
		final String defaultTitle = link.getTitle();
		
		if (paramInnerText != null) {
			params.put(MODEL_INNER_TEXT_NAME, paramInnerText);
		} else {
			params.put(MODEL_INNER_TEXT_NAME, defaultInnerText);
		}
		
		if (paramHref != null) {
			params.put(MODEL_HREF_NAME, paramHref);
		} else {
			params.put(MODEL_HREF_NAME, defaultHref);
		}
		
		if (paramTitle != null) {
			params.put(MODEL_TITLE_NAME, paramTitle);
		} else {
			params.put(MODEL_TITLE_NAME, defaultTitle);
		}
		
		// Do Action
		final String paramAction = request.getParameter(ACTION_NAME);
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (paramAction != null && paramAction.equals(ACTION_UPDATE_LINK)) {
			
			//Validate Inner Text
			if (paramInnerText == null) {
				errors.put(ERRORS_INNER_TEXT_MISSING, ERRORS_INNER_TEXT_MISSING_VALUE);
				valid = false;
			} else if (paramInnerText.length() > 250) {
				errors.put(ERRORS_INNER_TEXT_TOO_LONG, ERRORS_INNER_TEXT_TOO_LONG_VALUE);
				valid = false;
			}
			
			//Validate Href
			if (paramHref == null) {
				errors.put(ERRORS_HREF_MISSING, ERRORS_HREF_MISSING_VALUE);
				valid = false;
			} else if (paramHref.length() > 250) {
				errors.put(ERRORS_HREF_TOO_LONG, ERRORS_HREF_TOO_LONG_VALUE);
				valid = false;
			}
			
			//Validate Title
			if (paramTitle != null) {
				if (paramTitle.length() > 1000) {
					errors.put(ERRORS_TITLE_TOO_LONG, ERRORS_TITLE_TOO_LONG_VALUE);
					valid = false;
				}
			}
			
			if (valid) {
				
				link.setInnerText(paramInnerText);
				link.setHref(paramHref);
				if (!paramTitle.equals("")) {
					link.setTitle(paramTitle);
				}
				link.setUsername(userSession.getLoginInformation().getUsername());
				
				connection.sendUpdateRequest(Link.ID, link);
				if(!connection.getContentType().equalsIgnoreCase(Link.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				
				String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + savedUrl + "?" + PARAM_MESSAGE_CODE_NAME + "=" + PARAM_MESSAGE_CODE_EDITED;
				
				System.out.println("Redirect: " + response.encodeRedirectURL(url));
				System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
				response.sendRedirect(response.encodeRedirectURL(url));
				
			}
		
			params.put(MODEL_ERRORS_NAME, errors);
		}
		
		return params;
	}

}
