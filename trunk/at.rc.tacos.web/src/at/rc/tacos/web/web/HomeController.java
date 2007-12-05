package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tacosdp.domain.ServiceImpl;


/**
 * @author PayerM
 * @version 1.0
 */
public class HomeController implements Controller {

	public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("products", ServiceImpl.getInstance().listProducts());
		return params;

	}
}
