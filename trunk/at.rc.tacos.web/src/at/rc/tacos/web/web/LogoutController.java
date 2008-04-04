package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController implements Controller {

	public Map<String, Object> doLogout(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		final HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(context.getContextPath() + request.getServletPath());
		return new HashMap();
	}
	
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
		return doLogout(request, response, context);
	}
}
