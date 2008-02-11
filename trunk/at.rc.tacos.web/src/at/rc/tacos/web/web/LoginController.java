package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

/**
 * Servlet implementation class for Servlet: LoginController
 */
public class LoginController implements Controller 
{
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if("login".equalsIgnoreCase(action))
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if(username.trim().isEmpty() || password.trim().isEmpty())
			{ 
				params.put("loginError", "Bitte geben Sie ihren Usernamen und Passwort ein!");
				return params;
			} 

			//the result
			WebClient client = new WebClient();
			//open a connection to the server
			client.connect("81.189.52.155", 4711);
			//client.connect("localhost", 4711);
			AbstractMessage result = client.sendLoginRequest(username, password);
			//get the content

			if(Login.ID.equalsIgnoreCase(client.getContentType()))
			{
				Login loginResult = (Login)result;
				if(loginResult.isLoggedIn()) 
				{ 
					UserSession userSession = (UserSession)session.getAttribute("userSession"); 
					userSession.setLoggedIn(true, loginResult, client); 
					
					//request the frequently used object from the server
					List<AbstractMessage> stationList = client.sendListingRequest(Location.ID, null);
					if(Location.ID.equalsIgnoreCase(client.getContentType())) 
					{
						for(AbstractMessage abstractLoaction:stationList)
							userSession.addLocation((Location)abstractLoaction);
					}

					List<AbstractMessage> jobList = client.sendListingRequest(Job.ID, null);
					if(Job.ID.equalsIgnoreCase(client.getContentType())) 
					{
						for(AbstractMessage abstractJob:jobList)
							userSession.addJob((Job)abstractJob);
					}

					List<AbstractMessage> compList = client.sendListingRequest(Competence.ID, null);
					if(Competence.ID.equalsIgnoreCase(client.getContentType()))    
					{
						for(AbstractMessage abstractCompetence:compList)
							userSession.addCompetence((Competence)abstractCompetence);
					}

					List<AbstractMessage> serviceList = client.sendListingRequest(ServiceType.ID, null);
					if(ServiceType.ID.equalsIgnoreCase(client.getContentType()))  
					{
						for(AbstractMessage abstractServiceType:serviceList)
							userSession.addServiceType((ServiceType)abstractServiceType);
					}

					List<AbstractMessage> staffList = client.sendListingRequest(StaffMember.ID, null);
					if(StaffMember.ID.equalsIgnoreCase(client.getContentType()))  
					{
						for(AbstractMessage abstractStaffMember:staffList)
							userSession.addStaffMember((StaffMember)abstractStaffMember);
					}

					//redirect to the roster day view
					response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterDay"));   
				}
				else
				{
					params.put("loginError", "Sie haben einen falschen Usernamen oder ein falsches Passwort eingegeben. Korrigieren Sie bitte ihre Eingaben!");
				}
			}
		}
		if("logout".equalsIgnoreCase(action))
		{
			session.invalidate();
			params.put("logout-success", "Sie wurden erfolgreich ausgeloggt!");
		}
		return params;
	}   	  	    
}