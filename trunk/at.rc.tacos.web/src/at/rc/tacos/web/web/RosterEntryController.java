package at.rc.tacos.web.web;

import java.util.Calendar;
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

public class RosterEntryController implements Controller
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

		if("doRosterEntry".equalsIgnoreCase(action))
		{
			String staffId = request.getParameter("employee");
			//request the staff member
			resultList = client.sendListingRequest(StaffMember.ID, new QueryFilter(IFilterTypes.ID_FILTER,staffId));	
			StaffMember staffMember = (StaffMember)resultList.get(0); 
			
			//planed start
			String startDay = request.getParameter("startDay");
			String startMonth = request.getParameter("startMonth");
			String startYear =  request.getParameter("startYear");
			String startHour = request.getParameter("startHour");
			String startMinute = request.getParameter("startMinute");
			//planed end
			String endDay = request.getParameter("endDay");
			String endMonth = request.getParameter("endMonth");
			String endYear =  request.getParameter("endYear");
			String endHour = request.getParameter("endHour");
			String endMinute = request.getParameter("endMinute");
			//construct a startCalendar
			Calendar startEntry = Calendar.getInstance();
			startEntry.set(Calendar.DAY_OF_MONTH, Integer.valueOf(startDay));
			startEntry.set(Calendar.MONTH, Integer.valueOf(startMonth)-1);
			startEntry.set(Calendar.YEAR, Integer.valueOf(startYear));
			startEntry.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startHour));
			startEntry.set(Calendar.MINUTE, Integer.valueOf(startMinute));
			//construct a startCalendar
			Calendar endEntry = Calendar.getInstance();
			endEntry.set(Calendar.DAY_OF_MONTH, Integer.valueOf(endDay));
			endEntry.set(Calendar.MONTH, Integer.valueOf(endMonth)-1);
			endEntry.set(Calendar.YEAR, Integer.valueOf(endYear));
			endEntry.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endHour));
			endEntry.set(Calendar.MINUTE, Integer.valueOf(endMinute));
			
			long plannedStartOfWork = startEntry.getTimeInMillis();
			long plannedEndOfWork = endEntry.getTimeInMillis();
			String station = request.getParameter("station");
			String job = request.getParameter("job");
			String servicetype = request.getParameter("service");

			if(staffId.trim().isEmpty() 
					|| startDay.trim().isEmpty() 
					|| startMonth.trim().isEmpty() 
					|| startYear.trim().isEmpty() 
					|| startHour.trim().isEmpty() 
					|| startMinute.trim().isEmpty() 
					|| endDay.trim().isEmpty() 
					|| endMonth.trim().isEmpty() 
					|| endYear.trim().isEmpty() 
					|| endHour.trim().isEmpty()
					|| endMinute.trim().isEmpty() 
					|| station.trim().isEmpty() 
					|| job.trim().isEmpty()
					|| servicetype.trim().isEmpty())
			{ 
				params.put("loginError", "Keine Daten eingegeben!");
				return params;
			} 

			RosterEntry entry = new RosterEntry(staffMember,servicetype,job,station,plannedStartOfWork,plannedEndOfWork);
			client.sendAddRequest(RosterEntry.ID, entry);
			if(client.getContentType().equalsIgnoreCase(RosterEntry.ID))
			{
				params.put("entry-success", "Dienst erfolgreich eingetragen!");
			}
			else
			{
				//eintrag hat nicht geklappt
			}
		}
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
