package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class statisticEmployeeController  implements Controller {

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();

		return params;
	}

}
