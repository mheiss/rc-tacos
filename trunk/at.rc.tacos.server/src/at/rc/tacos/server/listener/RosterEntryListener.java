package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

/**
 * This class will be notified uppon roster entry updates
 * @author Michael
 */
public class RosterEntryListener extends ServerListenerAdapter
{
    private RosterDAO rosterDao = DaoFactory.TEST.createRosterEntryDAO();

    /**
     * Add a roster entry
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        RosterEntry entry = (RosterEntry)addObject;
        try
        {
            int id = rosterDao.addRosterEntry(entry);
            entry.setRosterId(id);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }

        return entry;
    }

    /**
     * Listing of all entries 
     * @throws ParseException 
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();

        System.out.println("New listing request");

        //if there is no filter -> request all
        if(queryFilter == null || queryFilter.getFilterList().isEmpty())
        {
            try
            {
                list.addAll(rosterDao.listRosterEntrys());
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
        else if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
        {
            //get the query filter
            final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
            //format the time
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Calendar filterTime = Calendar.getInstance();
            try
            {
                filterTime.setTime(df.parse(dateFilter));

                long dateStart = filterTime.getTimeInMillis();
                //set the end to 23:59
                filterTime.set(Calendar.HOUR, 23);
                filterTime.set(Calendar.MINUTE,59);
                long dateEnd = filterTime.getTimeInMillis();

                try
                {
                    list.addAll(rosterDao.listRosterEntryByDate(dateStart, dateEnd));
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.getMessage();
                }
            }
            catch(ParseException pe)
            {
                throw new IllegalArgumentException("cannot pars the date");
            }
        }
        else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            try
            {
                list.add(rosterDao.getRosterEntryById(id));
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
        //return the list
        return list;
    }

    /**
     * Remove a roster entry
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        RosterEntry entry = (RosterEntry)removeObject;
        try
        {
            rosterDao.removeRosterEntry(entry);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return entry;
    }

    /**
     * Update a roster entry
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        RosterEntry entry = (RosterEntry)updateObject;
        try
        {
            rosterDao.updateRosterEntry(entry);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return entry;
    }
}
