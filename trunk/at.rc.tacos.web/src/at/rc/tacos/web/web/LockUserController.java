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
import at.rc.tacos.model.StaffMember;

public class LockUserController implements Controller
{
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		List<AbstractMessage> resultList;

		resultList = client.sendListingRequest(StaffMember.ID, null);
		if(StaffMember.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("employeeList", resultList); 

		if("doLockUser".equalsIgnoreCase(action))
		{
			//get the roster entry by id 
			resultList = client.sendListingRequest(StaffMember.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"))); 
			StaffMember user = (StaffMember)resultList.get(0);  

			client.sendRemoveRequest(StaffMember.ID,user);
			response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("deleteUser.url"));
		}
		return params;
	}
}
