package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Roster Month Controller
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthController extends Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		// Location
		
		// Competence
		
		// Staff Member (depends on competence)
		
		// Month
		
		return params;
	}

}
