package at.rc.tacos.web.web;

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

public class EditProfileController implements Controller
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

		if("domemberEntry".equalsIgnoreCase(action))
		{
			String staffId = request.getParameter("employee");
			//request the staff member
			resultList = client.sendListingRequest(StaffMember.ID, new QueryFilter(IFilterTypes.ID_FILTER,staffId));	
			StaffMember staffMember = (StaffMember)resultList.get(0); 
			//planed start
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String birthday =  request.getParameter("birthday");
			String username = request.getParameter("username");
			String eMail = request.getParameter("eMail");
			String phonenumber = request.getParameter("phonenumber");
			String streetname = request.getParameter("streetname");
			String cityname = request.getParameter("cityname");
			String station = request.getParameter("station");

	//		StaffMemberEntry entry = new UserEntry(staffId, firstName, lastName, birthday, username, eMail, phonenumber, streetname, cityname, station);
	//		client.sendAddRequest(RosterEntry.ID, entry);
			
//			MemberEntry entry = new MemberEntry(firstName,lastName,birthday,username,eMail,phonenumber,streetname,cityname,station);
//			client.sendAddRequest(MemberEntry.ID, entry);
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
