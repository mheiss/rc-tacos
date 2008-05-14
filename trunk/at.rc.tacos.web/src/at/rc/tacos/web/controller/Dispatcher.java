package at.rc.tacos.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.web.session.FormDefaultValues;
import at.rc.tacos.web.session.UserSession;

/**
 * Dispatcher (Front Controller):
 * This class is responsible for URL resolving, security, forwarding to view and loads template.
 * @author Payer Martin
 * @version 1.0
 */
public class Dispatcher extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static final String VIEWS_BUNDLE_PATH = "at.rc.tacos.web.config.urls";
	public static final String SERVER_BUNDLE_PATH = "at.rc.tacos.web.config.server";
	public static final String NET_BUNDLE_PATH = "at.rc.tacos.web.config.net";


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
		
		//Set charakter encoding
		request.setCharacterEncoding("ISO-8859-1");
		
		String localAddr = request.getLocalAddr();
		String localName = request.getLocalName();
		int localPort = request.getLocalPort();
		String remoteAdr = request.getRemoteAddr();
		String remoteHost = request.getRemoteHost();
		String remoteUser = request.getRemoteUser();
		int remotePort = request.getRemotePort();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String servletPath = request.getServletPath();
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getRequestURI();
		String requestContextPath = request.getContextPath();
		String pathInfo = request.getPathInfo();
		String pathTranslated = request.getPathTranslated();	
		String servletContextPath = getServletContext().getContextPath();
		String servletContextContextName = getServletContext().getServletContextName();
		String servletContextServerInfo = getServletContext().getServerInfo();
		
		System.out.println("localAddr: " + localAddr + "\n");
		System.out.println("localName: " + localName + "\n");
		System.out.println("localPort: " + localPort + "\n");
		System.out.println("remoteAdr: " + remoteAdr + "\n");
		System.out.println("remoteHost: " + remoteHost + "\n");
		System.out.println("remoteUser: " + remoteUser + "\n");
		System.out.println("remotePort: " + remotePort + "\n");
		System.out.println("serverName: " + serverName + "\n"); 
		System.out.println("serverPort: " + serverPort + "\n");
		System.out.println("servletPath: " + servletPath + "\n");
		System.out.println("requestUrl: " + requestUrl + "\n");
		System.out.println("requestUri: " + requestUri + "\n");
		System.out.println("requestContextPath: " + requestContextPath + "\n");
		System.out.println("pathInfo: " + pathInfo + "\n");
		System.out.println("pathTranslated: " + pathTranslated + "\n");		
		System.out.println("servletContextPath: " + servletContextPath + "\n");
		System.out.println("servletContextContextName: " + servletContextContextName + "\n");
		System.out.println("servletContextServerInfo: " + servletContextServerInfo + "\n");
		
		//Assert we have a valid session
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		//Assert that there is valid user session
		if (userSession == null) {
			userSession = new UserSession();
			final FormDefaultValues formDefaultValues = new FormDefaultValues();
			userSession.setFormDefaultValues(formDefaultValues);
			session.setAttribute("userSession", userSession);
		}
		
		//Check if the request came from an internal IP
		if (Pattern.matches(server.getString("server.reverseProxy.address.pattern"), request.getRemoteAddr())) {
			userSession.setInternalSession(false);
		} else if (Pattern.matches(server.getString("server.network.address.pattern"), request.getRemoteAddr())) {
			userSession.setInternalSession(true);
		} else {
			userSession.setInternalSession(false);
		}
		request.setAttribute("isInternal", userSession.isInternalSession());
		
		//Set default form values if not set
		if (userSession.getLoggedIn()) {
			if (userSession.getFormDefaultValues().getDefaultDate() == null) {
				final Date rosterDefaultDate = new Date();
				userSession.getFormDefaultValues().setDefaultDate(rosterDefaultDate);
			}
			if (userSession.getFormDefaultValues().getDefaultLocation() == null) {
				userSession.getFormDefaultValues().setDefaultLocation(userSession.getLoginInformation().getUserInformation().getPrimaryLocation());
			}
			request.setAttribute("authorization", userSession.getLoginInformation().getAuthorization());
		}
		/*if (userSession.getLoggedIn()) {
		// Get current login information from server
		final WebClient connection = userSession.getConnection();
		Login login = null;
		final QueryFilter usernameFilter = new QueryFilter();
		usernameFilter.add(IFilterTypes.USERNAME_FILTER, userSession.getLoginInformation().getUsername());
		final List<AbstractMessage> loginList = connection.sendListingRequest(Login.ID, usernameFilter);
		if (Login.ID.equalsIgnoreCase(connection.getContentType())) {
			login = (Login)loginList.get(0);
		}
		userSession.getLoginInformation().setAuthorization(login.getAuthorization());
		userSession.getLoginInformation().setUserInformation(login.getUserInformation());
		}*/
		
		//Get the relative Path from request URL
		final String relativePath = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");
		final String relativePathPrefix = relativePath.replaceFirst("/", "").replaceFirst(".do", "");
		
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
		if (Pattern.matches(server.getString("server.http.url.pattern"), request.getRequestURL().toString()) || request.getServerPort() == Integer.parseInt(server.getString("server.default.port"))) {
			System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath));
			System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
			response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath));
			
			/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath));
			getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/

		}
		//If no URL is specified send redirect to home.do.
		else if (relativePath.equals("") || relativePath.equals("/")) {

			System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("roster.url")));
			System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
			response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("roster.url")));
			
			/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("addRosterEntry.url"));
			getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
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
				System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url")));
				System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
				response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url")));
				
				/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("notFound.url")));
				getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
			} else {
				if (loginRequired == true && !userSession.getLoggedIn()) {
					final Enumeration<Object> e = request.getParameterNames();
					String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("login.url") + "?savedUrl=" + relativePath;
					while (e.hasMoreElements()) {
						final String parameterName = (String)e.nextElement();
						final String parameterValue = request.getParameter(parameterName);
						url += "&" + parameterName + "=" + parameterValue;
					}
					System.out.println("Redirect: " + response.encodeRedirectURL(url));
					System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
					response.sendRedirect(response.encodeRedirectURL(url));
					
					/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
					getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
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
								System.out.println("Forward: " + response.encodeURL(templatePath));
								System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
								getServletContext().getRequestDispatcher(response.encodeURL(templatePath)).forward(request, response);
							} else {
								System.out.println("Forward: " + response.encodeURL(viewPath));
								System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
								getServletContext().getRequestDispatcher(response.encodeURL(viewPath)).forward(request, response);
							}
						}
					} catch (InstantiationException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					}
				}

			}
		}
	}
}
