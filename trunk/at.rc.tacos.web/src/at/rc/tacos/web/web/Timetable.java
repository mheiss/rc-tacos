package at.rc.tacos.web.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.core.net.internal.WebClient;

//import at.rc.tacos.core.net.internal.WebClient;

public class Timetable extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	static final long serialVersionUID = 1L;
	 
	public Timetable() {
		super();
	}

    /**
     * Hanldes get requests and passes them to the post method
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doPost(request,response);
    }  	

    /**
     * Handles post requests
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String action = request.getParameter("action");
        if(action!=null){
        	if(action.equalsIgnoreCase("getTimetableDay")){
        		request.getRequestDispatcher("jsp/rosterViewDay.jsp").forward(request, response);
        	}
        	if(action.equalsIgnoreCase("getTimetableWeek")){
        		request.getRequestDispatcher("jsp/rosterViewWeek.jsp").forward(request, response);
        	}
        }
        
        
    } 
}
