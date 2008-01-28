package at.rc.tacos.web.web;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class TimetableController implements Controller{

	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		List<AbstractMessage> resultList;

		Date current = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		//get roster entries
		QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,format.format(current));
		resultList = client.sendListingRequest(RosterEntry.ID, filter);
		if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("rosterList", resultList); 

		List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter);
		for(AbstractMessage object:dayResult)   
		{  
			RosterEntry entry = (RosterEntry)object;  
			if(entry.getStation().equals("Kapfenberg"))
				// statt Kapfenberg dann: StaffMember.getPrimaryLocation()
				resultList.add(entry);  
		}
		return params;
	}
}
