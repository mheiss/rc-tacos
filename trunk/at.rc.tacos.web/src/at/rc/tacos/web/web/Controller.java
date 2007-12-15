package at.rc.tacos.web.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	/**
	 * Does internal processing and creates model.
	 * @param request HTTP Request
	 * @param response HTTP Response
	 * @param context Servlet context
	 * @return hashmap which represents model
	 * @throws Exception
	 */
	Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception;
}
