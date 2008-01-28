package at.rc.tacos.web.web; 

import java.text.SimpleDateFormat; 
import java.util.Calendar; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.ArrayList; 

import javax.servlet.ServletContext; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 

import at.rc.tacos.common.AbstractMessage; 
import at.rc.tacos.common.IFilterTypes; 
import at.rc.tacos.core.net.internal.WebClient; 
import at.rc.tacos.model.QueryFilter; 
import at.rc.tacos.model.RosterEntry; 


public class StationController  implements Controller 
{ 
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception 
	{ 
		//values that will be returned to the view 
		Map<String, Object> params = new HashMap<String, Object>(); 
		//the action to do 
		String action = request.getParameter("action"); 
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession"); 
		WebClient client = userSession.getConnection(); 

		//the result listing, that should contain the week result  
		List<AbstractMessage> resultList = new ArrayList<AbstractMessage>();  
		//the calendar instance with the current date  
		Calendar cal = Calendar.getInstance();  
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");  
		//get roster entries  
		for(int i=1; i<=7; i++)  
		{  
			//set up the filter with the date                
			QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,format.format(cal.getTime()));  
			//query the listing for the given day  
			List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter);  
			//check if we got the desired type and  add the entries to the list  
			request.setAttribute("action", action);
			//increment the day by one  
			cal.add(Calendar.DAY_OF_MONTH, 1);  
			for(AbstractMessage object:dayResult)   
			{  
				RosterEntry entry = (RosterEntry)object;  
				if(entry.getStation().getLocationName().equals(action))  
					resultList.add(entry);  
			}  
		}  
		params.put("rosterList", resultList); 
		return params;      
	} 
}