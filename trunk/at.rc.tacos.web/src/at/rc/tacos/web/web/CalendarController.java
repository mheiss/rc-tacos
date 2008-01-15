package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.StaffMember;

public class CalendarController implements Controller{

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		List<AbstractMessage> resultList;

		resultList = client.sendListingRequest(StaffMember.ID, null);
		if(StaffMember.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("employeeList", resultList); 
			
		return params;
	}

}
