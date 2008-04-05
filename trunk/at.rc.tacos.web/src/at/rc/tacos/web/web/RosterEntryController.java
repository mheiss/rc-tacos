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
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.ServiceType;
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
		Boolean dupl = true;
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();

		if("doRosterEntry".equalsIgnoreCase(action))
		{
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

			String startDate = startDay + "-" + startMonth + "-" + startYear;
			//get roster entries
			QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,startDate);			
			List<AbstractMessage> dayResult = client.sendListingRequest(RosterEntry.ID, filter);
			if(Integer.parseInt(startHour)<10){
				startHour = "0"+startHour;
			}
			if(Integer.parseInt(endHour)<10){
				endHour = "0"+endHour;
			}

			for(AbstractMessage object:dayResult)   
			{  
				RosterEntry entry = (RosterEntry)object;  
				if(entry.getStaffMember().getStaffMemberId() == member.getStaffMemberId()){
					dupl = false;
				}
			}
			//no access to dupl entry
			if(dupl){			
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
					params.put("entry-success", "Keine Daten eingegeben!");
					return params;
				} 

				RosterEntry entry = new RosterEntry(member,service,job, location,plannedStartOfWork, plannedEndOfWork);
				entry.setCreatedByUsername(userSession.getUsername());
				client.sendAddRequest(RosterEntry.ID, entry);
				if(client.getContentType().equalsIgnoreCase(RosterEntry.ID))
				{
					params.put("entry-success", "Dienst erfolgreich eingetragen!");
				}
				else
				{
					params.put("entry-error", "Dienst konnte wegen eines unvorhergesehenen Fehler nicht eingetragen werden! Bitte versuchen Sie es zu einem späteren Zeitpunkt wieder oder kontaktieren Sie Ihre Leitstelle."); 
				}
			}
			else
			{
				params.put("entry-error", "Sie oder die Person die Sie eintragen m&ouml;chten haben/hat an diesem Tag schon einen Dienst eingetragen. Bitte w&auml;hlen Sie einen anderen Tag."); 
			}
		}
		if("doRemoveEntry".equalsIgnoreCase(action))
		{
			//get the roster entry by id 
			List<AbstractMessage> resultList = client.sendListingRequest(RosterEntry.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"))); 
			RosterEntry entry = (RosterEntry )resultList.get(0);  

			client.sendRemoveRequest(RosterEntry.ID,entry );
			response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterDay.url")+"?action=DayView");
		}
		if("doSignIn".equalsIgnoreCase(action))
		{
			List<AbstractMessage> resultList = client.sendListingRequest(RosterEntry.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"))); 
			RosterEntry entry = (RosterEntry )resultList.get(0); 
			entry.setRealStartOfWork(Calendar.getInstance().getTimeInMillis());
			client.sendUpdateRequest(RosterEntry.ID,entry);
			response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterDay.url")+"?action=DayView");
		}
		if("doSignOut".equalsIgnoreCase(action))
		{
			List<AbstractMessage> resultList = client.sendListingRequest(RosterEntry.ID, new QueryFilter(IFilterTypes.ID_FILTER,request.getParameter("id"))); 
			RosterEntry entry = (RosterEntry )resultList.get(0); 
			entry.setRealEndOfWork(Calendar.getInstance().getTimeInMillis()); 		
			client.sendUpdateRequest(RosterEntry.ID,entry);
			response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterDay.url")+"?action=DayView");
		}
		return params;
	}
}
