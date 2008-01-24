package at.rc.tacos.web.web;

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
import at.rc.tacos.model.StaffMember;

public class EditUserController implements Controller
{
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("id");

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		QueryFilter filter = new QueryFilter(IFilterTypes.ID_FILTER, request.getParameter("id")); 
		List<AbstractMessage> Lista;  
		Lista = client.sendListingRequest(StaffMember.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id")));
		if(StaffMember.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("employeeList", Lista); 

		return params;
	}
}
