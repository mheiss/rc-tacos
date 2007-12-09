package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

public class RosterEntryController implements Controller 
{
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");

		HttpSession session = request.getSession();

		if("entry".equalsIgnoreCase(action))
		{
			StaffMember staffMember = request.getParameter("staffMember");
			long plannedStartOfWork = request.getParameter("plannedStartOfWork");
			long plannedEndOfWork = request.getParameter("plannedEndOfWork");
			String station = request.getParameter("station");
			String competence = request.getParameter("competence");
			String servicetype = request.getParameter("servicetype");
			//the result
			List<AbstractMessage> result;
			WebClient client = new WebClient();
			//open a connection to the server
			client.connect("81.189.52.155", 4711);
			RosterEntry entry = new RosterEntry(staffMember,plannedStartOfWork, plannedEndOfWork, station, competence, servicetype);
			result = client.sendRequest(username, Login.ID, IModelActions.LOGIN, login);
			//get the content
			if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))
			{
				RosterEntry entryResult = (RosterEntry)result.get(0);
				if(entryResult.isLoggedIn())
				{
					UserSession userSession = (UserSession)session.getAttribute("userSession");
					userSession.setLoggedIn(true, "user", client);
					response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home")); 
				}
				else
				{
					params.put("entryError", entryResult.getErrorMessage());
				}
			}
		}
		return params;
	}  

}
