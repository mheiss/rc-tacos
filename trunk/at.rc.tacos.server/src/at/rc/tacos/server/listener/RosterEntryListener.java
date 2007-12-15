package at.rc.tacos.server.listener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon roster entry updates
 * @author Michael
 */
public class RosterEntryListener extends ServerListenerAdapter
{
    private RosterDAO rosterDao = DaoService.getInstance().getFactory().createRosterEntryDAO();

    /**
     * Add a roster entry
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        RosterEntry entry = (RosterEntry)addObject;
        int id = rosterDao.addRosterEntry(entry);
        entry.setRosterId(id);
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
            list.addAll(rosterDao.listRosterEntrys());
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
    
                list.addAll(rosterDao.listRosterEntryByDate(dateStart, dateEnd));
            }
            catch(ParseException pe)
            {
                throw new IllegalArgumentException("cannot pars the date");
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
        rosterDao.removeRosterEntry(entry);
        return entry;
    }

    /**
     * Update a roster entry
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        RosterEntry entry = (RosterEntry)updateObject;
        rosterDao.updateRosterEntry(entry);
        return entry;
    }
}
