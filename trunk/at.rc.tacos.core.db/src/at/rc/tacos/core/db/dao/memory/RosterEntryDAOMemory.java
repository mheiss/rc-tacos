package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.RosterEntry;

/**
 * Data source for roster entries
 * @author Michael
 */
public class RosterEntryDAOMemory implements RosterDAO
{
    //the shared instance
    private static RosterEntryDAOMemory instance;
    
    //the data list
    private ArrayList<RosterEntry> rosterList; 

    /**
     * Default class constructor
     */
    private RosterEntryDAOMemory()
    {
        rosterList = new ArrayList<RosterEntry>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static RosterEntryDAOMemory getInstance()
    {
        if (instance == null)
            instance = new RosterEntryDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        rosterList = new ArrayList<RosterEntry>();
    }

    @Override
    public int addRosterEntry(RosterEntry entry)
    {
        rosterList.add(entry);
        return rosterList.indexOf(entry);
    }
    
    @Override
    public void updateRosterEntry(RosterEntry entry)
    {
        int index = rosterList.indexOf(entry);
        rosterList.remove(index);
        rosterList.add(index,entry);
    }
    
    @Override
    public void removeRosterEntry(RosterEntry rosterEntry)
    {
        rosterList.remove(rosterEntry);
    }

    @Override
    public RosterEntry getRosterEntryById(int rosterEntryId)
    {
        for(RosterEntry entry:rosterList)
        {
            if(entry.getRosterId() == rosterEntryId)
                return entry;
        }
        return null;
    }
    
    @Override
    public List<RosterEntry> listRosterEntrys()
    {
        return rosterList;
    }

    @Override
    public List<RosterEntry> listRosterEntryByEmployee(int emplyeeID)
    {
        List<RosterEntry> resultList = new ArrayList<RosterEntry>();
        for(RosterEntry entry:rosterList)
        {
            if(entry.getStaffMember().getPersonId() == emplyeeID)
                resultList.add(entry);
        }
        return resultList;
    }

    @Override
    public List<RosterEntry> listRosterEntryByTime(long startTime, long endTime)
    {
        List<RosterEntry> resultList = new ArrayList<RosterEntry>();
        for(RosterEntry entry:rosterList)
        {
            if(entry.getPlannedStartOfWork() >= startTime && entry.getPlannedEndOfWork() <= endTime)
                resultList.add(entry);
        }
        return resultList;
    }
}
