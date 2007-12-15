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
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

public class RosterController implements Controller
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

		//get roster entries
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
			String startDay = "15";
			String startMonth = "12";
			String startYear =  "2007";
			String startHour = "15";
			String startMinute = "30";
			//planed end
			String endDay = "15";
			String endMonth = "12";
			String endYear =  "2007";
			String endHour = "18";
			String endMinute = "30";
			//construct a startCalendar
			Calendar startEntry = Calendar.getInstance();
			startEntry.set(Calendar.DAY_OF_MONTH, Integer.valueOf(startDay));
			startEntry.set(Calendar.MONTH, Integer.valueOf(startMonth));
			startEntry.set(Calendar.YEAR, Integer.valueOf(startYear));
			startEntry.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startHour));
			startEntry.set(Calendar.MINUTE, Integer.valueOf(startMinute));
			//construct a startCalendar
			Calendar endEntry = Calendar.getInstance();
			endEntry.set(Calendar.DAY_OF_MONTH, Integer.valueOf(endDay));
			endEntry.set(Calendar.MONTH, Integer.valueOf(endMonth));
			endEntry.set(Calendar.YEAR, Integer.valueOf(endYear));
			endEntry.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endHour));
			endEntry.set(Calendar.MINUTE, Integer.valueOf(endMinute));

			long plannedStartOfWork = startEntry.getTimeInMillis();
			long plannedEndOfWork = endEntry.getTimeInMillis();
			String station = request.getParameter("station");
			String competence = request.getParameter("competence");
			String servicetype = request.getParameter("service");

			RosterEntry entry = new RosterEntry(staffMember,servicetype,competence,station,plannedStartOfWork,plannedEndOfWork);
			client.sendAddRequest(RosterEntry.ID, entry);
			if(client.getContentType().equalsIgnoreCase(RosterEntry.ID))
			{
				//eintrag erfolgreich
			}
			else
			{
				//eintrag hat nicht geklappt
			}
		}
		return params;
	}
}
