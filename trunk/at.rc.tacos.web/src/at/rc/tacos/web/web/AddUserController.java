package at.rc.tacos.web.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.StaffMember;

public class AddUserController extends Controller
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
		//StaffMember member = userSession.getStaffMember();
		
		if("doAddUser".equalsIgnoreCase(action))
		{
			//User Data
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String birthday = request.getParameter("birthday"); 
			String birthmonth = request.getParameter("birthmonth");
			String birthyear = request.getParameter("birthyear");
			String eMail = request.getParameter("eMail"); 
			String phonenumber = request.getParameter("phonenumber"); 
			String station = request.getParameter("station"); 
			String competenceDriver = request.getParameter("competenceDriver");        
			String competenceSani = request.getParameter("competenceSani");
			String competenceIsp = request.getParameter("competenceIsp");
			String competenceDf = request.getParameter("competenceDf");
			String competenceEmergency = request.getParameter("competenceEmergency");
			String competenceDoctor = request.getParameter("competenceDoctor");
			String competenceOther = request.getParameter("competenceOther");
			String competenceIntervention = request.getParameter("competenceIntervention");
			String streetname = request.getParameter("streetname"); 
			String cityname = request.getParameter("cityname");

			if(firstName.trim().isEmpty() 
					|| lastName.trim().isEmpty() 
					|| birthday.trim().isEmpty() 
					|| birthmonth.trim().isEmpty() 
					|| birthyear.trim().isEmpty() 
					|| eMail.trim().isEmpty() 
					|| phonenumber.trim().isEmpty() 
					|| station.trim().isEmpty() 
					|| streetname.trim().isEmpty() 
					|| cityname.trim().isEmpty())
			{ 
				params.put("entry-error", "Es m&uuml;ssen alle Felder ausgef&uuml;llt werden!");
				return params;
			} 

			if(competenceDriver == null 
					&& competenceSani == null  
					&& competenceIsp == null 
					&& competenceDf == null 
					&& competenceEmergency == null 
					&& competenceDoctor == null 
					&& competenceOther == null 
					&& competenceIntervention == null )
			{ 
				params.put("entry-error", "Es muss mindestens eine F&auml;higkeit angegeben werden!");
				return params;
			} 
			
			if(birthday.trim().isEmpty() 
					|| birthmonth.trim().isEmpty() 
					|| birthyear.trim().isEmpty() 
					|| station.trim().isEmpty())
			{ 
				params.put("entry-error", "Es ist ein unvorhergesehener Fehler aufgetreten! Bitte versuchen Sie zu einem anderen Zeitpunkt wieder.");
				return params;
			}
			 
			String birthData = birthday + "-" + birthmonth + "-" + birthyear;
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			
			List<AbstractMessage> resultList;
			resultList = client.sendListingRequest(StaffMember.ID, null);
			for(AbstractMessage object:resultList)   
			{  
				StaffMember entry = (StaffMember)object; 
				if(entry.getFirstName().equalsIgnoreCase(firstName) &&
						entry.getLastName().equalsIgnoreCase(lastName) &&
						df.format(entry.getBirthday()).equals(birthData)){
					params.put("entry-error", "Dieser Mitarbeiter konnte nicht angelegt werden, da die Komination Vorname, Nachname und Geburtsdatum schon vorhanden sind.");
					return params;
				}
			}
			
//			List<Competence> cList = new ArrayList<Competence>();
//			if(competenceDf != null ) 
//				cList.add(competenceDf);
//			if(!competenceDoctor != null ) 
//				cList.add(competenceDoctor);
//			if(!competenceDriver != null ) 
//				cList.add(competenceDriver);
//			if(!competenceEmergency != null ) 
//				cList.add(competenceEmergency);
//			if(!competenceIntervention != null ) 
//				cList.add(competenceIntervention);
//			if(!competenceIsp != null ) 
//				cList.add(competenceIsp);
//			if(!competenceOther != null ) 
//				cList.add(competenceOther);
//			if(!competenceSani != null ) 
//				cList.add(competenceSani);
			
			/*
			 * TODO
			 * eMail auf gültigkeit prüfen
			 * Übergabe der Werte an den Server -> speichern
			 */

			params.put("entry-success", "Werte muessen noch an den Server gegeben werden");
			
			
			
		}
		return params;
	}
}
