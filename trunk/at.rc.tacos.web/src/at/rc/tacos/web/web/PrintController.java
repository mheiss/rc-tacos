package at.rc.tacos.web.web;

import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.web.utils.PrinterJobRoster;

public class PrintController implements Controller
{
	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		WebClient client = userSession.getConnection();
		List<AbstractMessage> resultList;

		//QueryFilter filter = new QueryFilter(IFilterTypes.STATION_FILTER, null);
		resultList = client.sendListingRequest(RosterEntry.ID, null);
		if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("rosterList", resultList); 

		
		if(action!=null && id!=null){
			PrinterJob pj = PrinterJob.getPrinterJob();
			if(pj.printDialog()){
				PageFormat pf= pj.defaultPage();
				pf=pj.pageDialog(pf);			
				pj.setPrintable(new PrinterJobRoster(resultList, action),pf);
				
				try {
					pj.print();
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		return params;
	}
}
