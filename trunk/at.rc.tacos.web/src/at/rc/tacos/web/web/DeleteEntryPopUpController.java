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

public class DeleteEntryPopUpController implements Controller
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
		
		if("doRemoveEntry".equalsIgnoreCase(action))
		{
			//get the roster entry by id 
			resultList = client.sendListingRequest(RosterEntry.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"))); 
			RosterEntry entry = (RosterEntry )resultList.get(0);  
			 
			client.sendRemoveRequest(RosterEntry.ID,entry );
			response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterDay")+"?action=DayView");
		}
		return params;
	}
}
