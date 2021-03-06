package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout Controller
 * @author Payer Martin
 * @version 1.0
 */
public class LogoutController extends Controller {

	public Map<String, Object> doLogout(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		final HttpSession session = request.getSession();
		final ResourceBundle server = ResourceBundle.getBundle(Dispatcher.SERVER_BUNDLE_PATH);
		
		session.invalidate();
		System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("login.url")));
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
		response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("login.url")));
		
		/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + context.getContextPath() + request.getServletPath() + ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("login.url")));
		context.getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
		return new HashMap<String, Object>();
	}
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		return doLogout(request, response, context);
	}
}
