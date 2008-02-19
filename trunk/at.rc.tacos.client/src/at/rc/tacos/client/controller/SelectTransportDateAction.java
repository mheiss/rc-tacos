package at.rc.tacos.client.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;

/**
 * This class will handle the selection events for the calendar
 * and query the database for transports.
 * @author b.thek
 */
public class SelectTransportDateAction extends Action
{
    private Date date;

    /**
     * Default class constructor for an action.
     * @param date the date to switch to
     */
    public SelectTransportDateAction(Date date)
    {
        //mask the unused fields
        this.date = date;
    }

    @Override
    public void run()
    {
    	//save the date
    	SessionManager.getInstance().setDisplayedDate(date.getTime());
    	
        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(date);
        
        //set up the filter and query the server
        QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,strDate);
        //query transports in progress and journal transports
        NetWrapper.getDefault().requestListing(Transport.ID,filter);
    }
}
