package at.rc.tacos.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Login;
import at.rc.tacos.web.session.UserSession;

/**
 * Personal Data Controller
 * @author Payer Martin
 * @version 1.0
 */
public class PersonalDataController extends Controller {

	private static final String MODEL_TIMESTAMP_NAME = "timestamp";
	
	private static final String PARAM_PASSWORD_NAME = "passwd";
	
	private static final String PARAM_REPEATED_PASSWORD_NAME = "repeatedPassword";
	
	private static final String ACTION_NAME = "action";
	private static final String ACTION_UPDATE_STAFF_MEMBER = "updateStaffMember";
	
	private static final String ERRORS_PASSWORD_MISSING = "passwordMissing";
	private static final String ERRORS_PASSWORD_MISSING_VALUE = "Passwort ist ein Pflichtfeld.";
	private static final String ERRORS_PASSWORD_TOO_LONG = "passwordTooLong";
	private static final String ERRORS_PASSWORD_TOO_LONG_VALUE = "Passwort ist zu lang. Es sind maximal 255 Zeichen erlaubt.";
	
	private static final String ERRORS_REPEATED_PASSWORD_MISSING = "repeatedPasswordMissing";
	private static final String ERRORS_REPEATED_PASSWORD_MISSING_VALUE = "Passwort wiederholen ist ein Pflichtfeld.";
	private static final String ERRORS_REPEATED_PASSWORD_TOO_LONG = "repeatedPasswordTooLong";
	private static final String ERRORS_REPEATED_PASSWORD_TOO_LONG_VALUE = "Passwort wiederholen ist zu lang. Es sind maximal 255 Zeichen erlaubt.";
	
	private static final String ERRORS_PASSWORDS_NOT_EQUAL = "passwordsNotEqual";
	private static final String ERRORS_PASSWORDS_NOT_EQUAL_VALUE = "Die zwei eingegebenen Passwörter stimmen nicht überein.";
	
	private static final String MODEL_EDITED_COUNT_NAME = "editedCount";
	
	private static final String MODEL_ERRORS_NAME = "errors";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final WebClient connection = userSession.getConnection();
		final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		
		// Create timestamp
		params.put(MODEL_TIMESTAMP_NAME, new Date().getTime());
		
		if (userSession.getLoginInformation().getUserInformation().getBirthday() != null) {
			final Date date = sdf1.parse(userSession.getLoginInformation().getUserInformation().getBirthday());
			params.put("date", date);
		}
		
		// Get Password
		final String password = request.getParameter(PARAM_PASSWORD_NAME);
		
		// Get repeated password
		final String repeatedPassword = request.getParameter(PARAM_REPEATED_PASSWORD_NAME);
		
		// Do Action
		final String action = request.getParameter(ACTION_NAME);
		final Map<String, String> errors = new HashMap<String, String>();
		boolean valid = true;
		if (action != null && action.equals(ACTION_UPDATE_STAFF_MEMBER)) {

	        // Validate password
	        if (password == null || password.trim().equals("")) {
	        	valid = false;
	        	errors.put(ERRORS_PASSWORD_MISSING, ERRORS_PASSWORD_MISSING_VALUE);
	        } else if (password.length() > 255) {
	        	valid = false;
	        	errors.put(ERRORS_PASSWORD_TOO_LONG, ERRORS_PASSWORD_TOO_LONG_VALUE);
	        }
	        
	        // Validate repeated password
	        if (repeatedPassword == null || repeatedPassword.trim().equals("")) {
	        	valid = false;
	        	errors.put(ERRORS_REPEATED_PASSWORD_MISSING, ERRORS_REPEATED_PASSWORD_MISSING_VALUE);
	        } else if (repeatedPassword.length() > 255) {
	        	valid = false;
	        	errors.put(ERRORS_REPEATED_PASSWORD_TOO_LONG, ERRORS_REPEATED_PASSWORD_TOO_LONG_VALUE);
	        }
	        
	        // Validate passwords
	        if (!password.equals(repeatedPassword)) {
	        	valid = false;
	        	errors.put(ERRORS_PASSWORDS_NOT_EQUAL, ERRORS_PASSWORDS_NOT_EQUAL_VALUE);
	        }
			
			
			if (valid) {
				final Login login = userSession.getLoginInformation();
				login.setPassword(password);
				connection.sendUpdateRequest(Login.ID, login);
				if(!connection.getContentType().equalsIgnoreCase(Login.ID)) {
					throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
				}
				params.put(MODEL_EDITED_COUNT_NAME, 1);
			}
			
			params.put(MODEL_ERRORS_NAME, errors);
		}
		
		return params;
	}

}
