package at.rc.tacos.client.controller;

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
    private long date;

    /**
     * Default class constructor for an action.
     * @param date the dateime to switch to
     */
    public SelectRosterDateAction(long date)
    {
        this.date = date;
    }

    @Override
    public void run()
    {
        //set up the filter and query the server
        QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,Long.toString(date));
        NetWrapper.getDefault().requestListing(RosterEntry.ID,filter);
    }
}
