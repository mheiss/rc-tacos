package at.rc.tacos.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.web.session.UserSession;

public class OldEditUserController extends Controller
{
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("id");

		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		params.put("editStaffMember", userSession.getStaffMemberById(Integer.valueOf(action)));

		return params;
	}
}
