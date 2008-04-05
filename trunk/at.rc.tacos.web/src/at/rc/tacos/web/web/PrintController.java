package at.rc.tacos.web.web;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.web.utils.PrinterJobRoster;

public class PrintController extends Controller
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
		SimpleDateFormat formath = new SimpleDateFormat("dd-MM-yyyy");
		QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,formath.format(new Date()));
		resultList = client.sendListingRequest(RosterEntry.ID, filter);
		if(RosterEntry.ID.equalsIgnoreCase(client.getContentType()))          
			params.put("rosterList", resultList); 

		if(action!=null && id!=null){
			PrinterJob pj = PrinterJob.getPrinterJob();
				
			try 
			{
				if(pj.printDialog()){
					pj.setPrintable(new PrinterJobRoster(resultList, action),this.getPaperSize()); 
					pj.print();
				}
			} 
			catch (PrinterException e1) 
			{
				e1.printStackTrace();
			}
			
		}

		return params;
	}
	
//  set PageFormat fix to "A4"
	private PageFormat getPaperSize()
	{
		PageFormat pf = new PageFormat();
		Paper a4 = new Paper(); 
		double reso = 72.0;
  	           
//  	 size (inch)
		double a4Width  =  8.26;
		double a4Height = 11.69;
		a4.setSize(a4Width * reso, a4Height * reso);

//  	 margin (inch)
		double a4LeftMargin   = 0.78; 
		double a4RightMargin  = 0.78;
		double a4TopMargin    = 0.78;
		double a4BottomMargin = 0.78;

		a4.setImageableArea(a4LeftMargin * reso, a4TopMargin * reso,
  	                   (a4Width - a4LeftMargin - a4RightMargin)*reso,
  	                   (a4Height - a4TopMargin - a4BottomMargin)*reso);
		pf.setPaper(a4);
	  	
		return pf;
	}
}
