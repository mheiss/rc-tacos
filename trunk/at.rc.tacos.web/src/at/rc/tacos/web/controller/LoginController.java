package at.rc.tacos.web.controller;

import java.util.Enumeration;
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
import at.rc.tacos.web.session.UserSession;
/**
 * This controller is responsible for creating Login View and for doing Login and Logout.
 * @author Payer Martin
 * @version 1.0
 */

public class LoginController extends Controller {
	
	public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		final HttpSession session = request.getSession();
		final Map<String, Object> params = new HashMap<String, Object>();
		
		final ResourceBundle netBundle = ResourceBundle.getBundle(Dispatcher.NET_BUNDLE_PATH);
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");

		if (username.trim().isEmpty() || password.trim().isEmpty()) {
			params.put("loginError", "Bitte geben Sie ihren Benutzernamen und Passwort ein!");
			return params;
		}

		final WebClient client = new WebClient();
		boolean serverListening = false;
		serverListening = client.connect(netBundle.getString("server.host"), Integer.parseInt(netBundle.getString("server.port")));
		if (!serverListening) {
			serverListening = client.connect(netBundle.getString("failover.host"), Integer.parseInt(netBundle.getString("failover.port")));
		}
		if (!serverListening) throw new IllegalArgumentException();
		else {
			AbstractMessage result = client.sendLoginRequest(username, password);
			if (Login.ID.equalsIgnoreCase(client.getContentType())) {
				Login loginResult = (Login) result;
				if (loginResult.isLoggedIn()) {
					UserSession userSession = (UserSession) session.getAttribute("userSession");
					userSession.setLoggedIn(true, loginResult, client);

					List<AbstractMessage> stationList = client.sendListingRequest(Location.ID, null);
					if (Location.ID.equalsIgnoreCase(client.getContentType())) {
						for (AbstractMessage abstractLoaction : stationList)
							userSession.addLocation((Location) abstractLoaction);
					}

					List<AbstractMessage> jobList = client.sendListingRequest(Job.ID, null);
					if (Job.ID.equalsIgnoreCase(client.getContentType())) {
						for (AbstractMessage abstractJob : jobList)
							userSession.addJob((Job) abstractJob);
					}

					List<AbstractMessage> compList = client.sendListingRequest(Competence.ID, null);
					if (Competence.ID.equalsIgnoreCase(client.getContentType())) {
						for (AbstractMessage abstractCompetence : compList)
							userSession.addCompetence((Competence) abstractCompetence);
					}

					List<AbstractMessage> serviceList = client.sendListingRequest(ServiceType.ID, null);
					if (ServiceType.ID.equalsIgnoreCase(client.getContentType())) {
						for (AbstractMessage abstractServiceType : serviceList)
							userSession.addServiceType((ServiceType) abstractServiceType);
					}

					List<AbstractMessage> staffList = client.sendListingRequest(StaffMember.ID, null);
					if (StaffMember.ID.equalsIgnoreCase(client.getContentType())) {
						for (AbstractMessage abstractStaffMember : staffList)
							userSession.addStaffMember((StaffMember) abstractStaffMember);
					}
					if (request.getParameter("savedUrl") == null) {
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
						context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} else {
						final Enumeration<Object> e = request.getParameterNames();
						String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + request.getParameter("savedUrl");
						
						System.out.println("Redirect: " + response.encodeRedirectURL(url));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(url));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
						context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					}
				} else {
					params.put("loginError", "Sie haben einen falschen Benutzernamen oder ein falsches Passwort eingegeben.");
				}
			}
		}
		return params;
	}
	
	public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		
		final UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		Map<String, Object> params = new HashMap<String, Object>();
		
		final Enumeration<Object> e = request.getParameterNames();
		String savedUrl = request.getParameter("savedUrl");
		int i = 0;
		while (e.hasMoreElements()) {
			final String parameterName = (String)e.nextElement();
			final String parameterValue = request.getParameter(parameterName);
			if (!parameterName.equals("savedUrl")) {
				if (i == 0) {
					savedUrl += "?" + parameterName + "=" + parameterValue;
				} else {
					savedUrl += "&" + parameterName + "=" + parameterValue;
				}
			}
			i++;
		}
		request.setAttribute("savedUrl", savedUrl);
		
		if (userSession.getLoggedIn()) {
			System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
			System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
			response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
			
			/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath()));
			context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
		} else {	
			final String action = request.getParameter("action");
			if ("login".equalsIgnoreCase(action)) {
				params = doLogin(request, response, context);
			}
		}
		return params;
	}
}