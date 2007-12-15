package at.rc.tacos.client.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;

import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

/**
 * This class will handle the selection events for the calendar
 * and query the database for roster entries.
 * @author Michael
 */
public class SelectRosterDateAction extends Action
{
    private Date date;

    /**
     * Default class constructor for an action.
     * @param date the dateime to switch to
     */
    public SelectRosterDateAction(Date date)
    {
        //mask the unused fields
        this.date = date;
    }

    @Override
    public void run()
    {
        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(date);
        
        //set up the filter and query the server
        QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,strDate);
        NetWrapper.getDefault().requestListing(RosterEntry.ID,filter);
    }
}
