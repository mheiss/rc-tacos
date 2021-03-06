package at.rc.tacos.web.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Superclass for all Controllers
 * @author Payer Martin
 * @version 1.0
 */
public abstract class Controller {

	public abstract Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception;
}
