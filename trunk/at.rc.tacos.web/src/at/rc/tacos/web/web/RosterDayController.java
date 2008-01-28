package at.rc.tacos.web.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

public class RosterDayController  implements Controller
{
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
			
		//date to show
		Calendar current = Calendar.getInstance();
		SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
		String startDate = request.getParameter("startDate");
		//if we have no date, use the current date
		if (startDate == null || startDate.trim().isEmpty())
			startDate = formath.format(current.getTime());
		
			//get roster entries
			QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,startDate);			
			List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter);
			List<RosterEntry> filterdByLocation = new ArrayList<RosterEntry>();
			for(AbstractMessage object:dayResult)   
            {  
                RosterEntry entry = (RosterEntry)object;  
                if(entry.getStation().equals(userSession.getStaffMember().getPrimaryLocation()))
                	filterdByLocation.add(entry); 
            }
			params.put("rosterList", filterdByLocation);
		return params;
	}
}
