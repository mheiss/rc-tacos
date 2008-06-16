package at.rc.tacos.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.web.session.UserSession;

/**
 * Personnel Data Controller
 * @author Payer Martin
 * @version 1.0
 */
public class PersonnelDataController extends Controller {

	private static final String MODEL_TIMESTAMP_NAME = "timestamp";
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		
		final UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		
		// Create timestamp
		params.put(MODEL_TIMESTAMP_NAME, new Date().getTime());
		
		if (userSession.getLoginInformation().getUserInformation().getBirthday() != null) {
			final Date date = sdf1.parse(userSession.getLoginInformation().getUserInformation().getBirthday());
			params.put("date", date);
		}
		
		return params;
	}

}
