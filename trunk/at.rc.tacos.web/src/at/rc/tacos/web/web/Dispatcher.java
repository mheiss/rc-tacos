package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Dispatcher (Front Controller):
 * This class is responsible for URL resolving, security, forwarding to view and loads template.
 * @author Payer Martin
 * @version 1.0
 */
public class Dispatcher extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static final String VIEWS_BUNDLE_PATH = "at.rc.tacos.web.web.urls";
	private static final String SERVER_BUNDLE_PATH = "at.rc.tacos.web.web.server";
	public static final String NET_BUNDLE_PATH = "at.rc.tacos.web.net.net";


	/**
	 * Calls doPost.
	 */
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * Does Dispatching. Checks relative URL. Gets controller. Forwards to View.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		final ResourceBundle views = ResourceBundle.getBundle(VIEWS_BUNDLE_PATH);
		final ResourceBundle server = ResourceBundle.getBundle(SERVER_BUNDLE_PATH);
		
		//Assert we have a valid session
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		//Assert we have a valid user session
		if (userSession == null) {
			userSession = new UserSession();
			session.setAttribute("userSession", userSession);
		}

		//Get the relative Path from request URL
		final String relativePath = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");
		final String relativePathPrefix = relativePath.replaceFirst("/", "").replaceFirst(".do", "");
		
		final String ip = request.getRemoteAddr();
		
		final Set<String> set = views.keySet();
		boolean urlFound = false;
		boolean loginRequired = true;
		boolean controllerFound = false;
		String controllerClassName = null;
		boolean viewFound = false;
		String viewPath = null;
		boolean templateFound = false;
		String templatePath = null;
		String viewTitle = null;
		String viewHeaderTitle = null;
		String js = null;
		String css = null;
		
		//Redirect if request is not send over SSL connection

		if (request.getServerPort() == Integer.parseInt(server.getString("server.default.port"))) {
			response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath));

		}
		//If no URL is specified send redirect to home.do.

		else if (relativePath.equals("") || relativePath.equals("/")) {

			response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath()+ request.getServletPath() + views.getString("addRosterEntry.url")));
		} else {
			//Get login requirement view and template
			for (final Iterator<String> it = set.iterator(); it.hasNext();) {
				final String key = it.next();
				final String prefix = key.replaceAll("\\..*", "");
				if (prefix.equals(relativePathPrefix) && key.contains(".url")) {
					urlFound = true;
				} else if (prefix.equals(relativePathPrefix) && key.contains(".loginRequired")) {
					if (views.getString(key).equalsIgnoreCase("false")) {
						loginRequired = false;
					}
				} else if (prefix.equals(relativePathPrefix) && key.contains(".controller")) {
					controllerFound = true;
					controllerClassName = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".view")) {
					viewFound = true;
					viewPath = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".template")) {
					templateFound = true;
					templatePath = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".title")) {
					viewTitle = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".htitle")) {
					viewHeaderTitle = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".js")) {
					js = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".css")) {
					css = views.getString(key).trim();
				}
			}			
			if (urlFound == false || controllerFound == false) {
				response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url"))); 
			} else {
				if (loginRequired == true && !userSession.getLoggedIn()) {
					final Enumeration<Object> e = request.getParameterNames();
					String url = getServletContext().getContextPath() + request.getServletPath() + views.getString("login.url") + "?responseUrl=" + relativePath;
					while (e.hasMoreElements()) {
						final String parameterName = (String)e.nextElement();
						final String parameterValue = request.getParameter(parameterName);
						url += "&" + parameterName + "=" + parameterValue;
					}
					response.sendRedirect(response.encodeRedirectURL(url));
				} else {
					try {
						final Controller controller = (Controller)Class.forName(controllerClassName).newInstance();
						final Map<String, Object> params = controller.handleRequest(request, response, this.getServletContext());
						request.setAttribute("params", params);
						//Forward to view if response is not commited and view is found in views.properties
						if (!response.isCommitted() && viewFound) {
							//Differentiate if View uses model.jsp or not
							if (templateFound) {
								request.setAttribute("title", viewTitle);
								request.setAttribute("htitle", viewHeaderTitle);
								request.setAttribute("view", viewPath);
								request.setAttribute("js", js);
								request.setAttribute("css", css);
								getServletContext().getRequestDispatcher(response.encodeURL(templatePath)).forward(request, response);
							} else {
								getServletContext().getRequestDispatcher(response.encodeURL(viewPath)).forward(request, response);
							}
						}
					} catch (InstantiationException e1) {
						e1.printStackTrace();
						response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
						response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
						response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
					} catch (Exception e) {
						e.printStackTrace();
						response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
					}
				}

			}
		}
	}
}
