package at.rc.tacos.web.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class StationController implements Controller {

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
		Date current = new Date();  
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");  
		QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,format.format(current));  
			resultList = client.sendListingRequest(RosterEntry.ID, filter);  
			ArrayList<RosterEntry> filteredList = new ArrayList<RosterEntry>(); 
			for(AbstractMessage object:resultList) 
			{ 
				RosterEntry entry = (RosterEntry)object; 
				if(entry.getStation().equals(action)) 
					filteredList.add(entry); 
			} 
			params.put("rosterList", filteredList);
		return params;
	}

}
