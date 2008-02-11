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


public class statisticEmployeeController  implements Controller {

	private String splitedDate[] = null;
	private String form = null;
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String action = request.getParameter("action");
		String startDate = request.getParameter("startDate");
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		
		if("doEmployeeStat".equalsIgnoreCase(action)){
//			//the result listing, that should contain the week result 
//			List<AbstractMessage> resultList = new ArrayList<AbstractMessage>(); 
//			//the calendar instance with the current date 
//			Calendar cal = Calendar.getInstance(); 
//			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy"); 
//			
//			
//			//if we have no date, use the current date
//			//else take the request parameter "startDate"
//			if (startDate == null || startDate.trim().isEmpty()){
//				startDate = format.format(cal.getTime());
//				
//			}else{
//				splitedDate = startDate.split("-");
//				//cal.set(year, month, day)
//				cal.set(Integer.parseInt(splitedDate[2]), Integer.parseInt(splitedDate[1])-1, 1);		
//			}
//			
//			//get roster entries 
//			List<String> filterdByDate = new ArrayList<String>();
//			for(int i=1; i<=cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) 
//			{ 
//				//set up the filter with the date               
//				QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER, format.format(cal.getTime())); //cal.getTime()
//				//query the listing for the given day 
//				List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter); 
//				//check if we got the desired type and  add the entries to the list 
//				if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))   
//					resultList.addAll(dayResult); 
//				//increment the day by one 
//				cal.add(Calendar.DAY_OF_MONTH, 1);  
//				//cal.add(startDate, 1); 
//				for(AbstractMessage object:dayResult)   
//				{  
//					RosterEntry entry = (RosterEntry)object;
//					form = entry.getStaffMember().getLastName() + ", " + entry.getStaffMember().getFirstName();
//					filterdByDate.add(form);
//					
//					
//				}
//			} 
			params.put("statistic", null); 
		}
		return params;
	}

}
