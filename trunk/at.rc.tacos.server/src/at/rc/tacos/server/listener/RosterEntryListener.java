package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
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
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(rosterDao.listRosterEntrys());
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
