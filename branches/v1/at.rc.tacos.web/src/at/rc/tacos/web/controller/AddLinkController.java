package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Link;
import at.rc.tacos.model.Login;
import at.rc.tacos.web.session.UserSession;

/**
 * Add Link Controller
 * @author Payer Martin
 * @version 1.0
 */
public class AddLinkController extends Controller {

	private static final String PARAM_INNER_TEXT_NAME = "innerText";
	private static final String MODEL_INNER_TEXT_NAME = "innerText";
	
	private static final String PARAM_HREF_NAME = "href";
	private static final String MODEL_HREF_NAME = "href";
	
	private static final String PARAM_TITLE_NAME = "title";
	private static final String MODEL_TITLE_NAME = "title";
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_ADD_LINK = "addLink";
	
	private static final String MODEL_ERRORS_NAME = "errors";
	
	private static final String MODEL_ADDED_COUNT_NAME = "addedCount";
	
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
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		
		final String authorization = userSession.getLoginInformation().getAuthorization();
		
		// Check authorization
		if (!authorization.equals(Login.AUTH_ADMIN)) {
			throw new IllegalArgumentException("Error: User has no permission for functionality.");
		}
		
		// Parse parameters
		String paramInnerText = request.getParameter(PARAM_INNER_TEXT_NAME);
		String paramHref = request.getParameter(PARAM_HREF_NAME);
		String paramTitle = request.getParameter(PARAM_TITLE_NAME);
		
		// Get default values
		final String defaultInnerText = null;
		final String defaultHref = null;
		final String defaultTitle = null;
		
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
		if (paramAction != null && paramAction.equals(ACTION_ADD_LINK)) {
			
			//Validate Inner Text
			if (paramInnerText == null || paramInnerText.equals("")) {
				errors.put(ERRORS_INNER_TEXT_MISSING, ERRORS_INNER_TEXT_MISSING_VALUE);
				valid = false;
			} else if (paramInnerText.length() > 250) {
				errors.put(ERRORS_INNER_TEXT_TOO_LONG, ERRORS_INNER_TEXT_TOO_LONG_VALUE);
				valid = false;
			}
			
			//Validate Href
			if (paramHref == null || paramHref.equals("")) {
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
				final Link link = new Link();
				
				link.setInnerText(paramInnerText);
				link.setHref(paramHref);
				if (!paramTitle.equals("")) {
					link.setTitle(paramTitle);
				}
				link.setUsername(userSession.getLoginInformation().getUsername());
				
				connection.sendAddRequest(Link.ID, link);
				if(!connection.getContentType().equalsIgnoreCase(Link.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				
				params.put(MODEL_ADDED_COUNT_NAME, 1);
			}
		
		params.put(MODEL_ERRORS_NAME, errors);
		}
		
		return params;
	}

}
