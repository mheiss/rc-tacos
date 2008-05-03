package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.web.session.UserSession;

public class OldStatisticTransportController extends Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String action = request.getParameter("action");
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		
		if("doTransportStat".equalsIgnoreCase(action)){
			
			params.put("statistic", "Under Construction");
		}
		return params;
	}

}