package at.rc.tacos.web.web;

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
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

public class UpdateEntryController implements Controller
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
		if("doUpdateEntry".equalsIgnoreCase(action))
		{
			//request the roster from the server
			QueryFilter filter = new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"));
			List<AbstractMessage> rosterList = client.sendListingRequest(RosterEntry.ID,filter);
			params.put("rosterEntry",rosterList.get(0));
		}
		if("doSaveEntry".equalsIgnoreCase(action))
		{
			QueryFilter filter = new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"));
			List<AbstractMessage> rosterList = client.sendListingRequest(RosterEntry.ID,filter);
			//convert to roster entry
			RosterEntry entry = (RosterEntry)rosterList.get(0);
			
			//TODO: 
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
			//get the objects from the session
			Location location = userSession.getLocationById(Integer.valueOf(request.getParameter("station")));
			Job job = userSession.getJobById(Integer.valueOf(request.getParameter("job")));
			ServiceType service = userSession.getServiceTypeById(Integer.valueOf(request.getParameter("service")));
			StaffMember member = userSession.getStaffMemberById(Integer.valueOf(request.getParameter("employee")));
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
			
			if(member == null 
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
					|| location == null 
					|| job == null
					|| service == null)
			{ 
				params.put("loginError", "Keine Daten eingegeben!");
				return params;
			} 
			
			entry.setServicetype(service);
			entry.setJob(job);
			entry.setStation(location);
			entry.setStaffMember(member);
			entry.setPlannedStartOfWork(plannedStartOfWork);
			entry.setPlannedEndOfWork(plannedEndOfWork);
			entry.setCreatedByUsername(userSession.getUsername());
			//update the entry
			client.sendUpdateRequest(RosterEntry.ID, entry);
		}
		return params;
	}
}
