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


public class RosterWeekController  implements Controller
{
	private String splitedDate[] = null;
	
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");
		String station = request.getParameter("station");
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();

		if("weekView".equalsIgnoreCase(action)) 
		{ 
			//the result listing, that should contain the week result 
			List<AbstractMessage> resultList = new ArrayList<AbstractMessage>(); 
			//the calendar instance with the current date 
			Calendar cal = Calendar.getInstance(); 
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); 
			String startDate = request.getParameter("startDate");
			
			//if we have no date, use the current date
			//else take the request parameter "startDate"
			if (startDate == null || startDate.trim().isEmpty()){
				startDate = format.format(cal.getTime());
			}else{
				splitedDate = startDate.split("-");
				//cal.set(year, month, day)
				cal.set(Integer.parseInt(splitedDate[2]), Integer.parseInt(splitedDate[1])-1, Integer.parseInt(splitedDate[0]));				
			}
			
			//get roster entries 
			List<RosterEntry> filterdByLocation = new ArrayList<RosterEntry>();
			for(int i=1; i<=7; i++) 
			{ 
				//set up the filter with the date               
				QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER, format.format(cal.getTime())); //cal.getTime()
				//query the listing for the given day 
				List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter); 
				//check if we got the desired type and  add the entries to the list 
				if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))   
					resultList.addAll(dayResult); 
				//increment the day by one 
				cal.add(Calendar.DAY_OF_MONTH, 1);  
				//cal.add(startDate, 1); 
				for(AbstractMessage object:dayResult)   
				{  
					RosterEntry entry = (RosterEntry)object;  
					if(station.equalsIgnoreCase("primary")){
						if(entry.getStation().equals(userSession.getStaffMember().getPrimaryLocation())){
							filterdByLocation.add(entry); 
						}
					}else{
						if(entry.getStation().getLocationName().equals(station)) {
							filterdByLocation.add(entry); 
						}
					}
				}
			} 
			//add the resulting list to the params 
			params.put("rosterList", filterdByLocation);  
		}
		return params;
	}
}
