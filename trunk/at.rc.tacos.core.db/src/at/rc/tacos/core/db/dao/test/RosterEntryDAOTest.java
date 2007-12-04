package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.RosterEntry;

/**
 * Data source for roster entries
 * @author Michael
 */
public class RosterEntryDAOTest implements RosterDAO
{
    //the shared instance
    private static RosterEntryDAOTest instance;
    
    //the data list
    private ArrayList<RosterEntry> rosterList; 

    /**
     * Default class constructor
     */
    private RosterEntryDAOTest()
    {
        rosterList = new TestDataSource().rosterList;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static RosterEntryDAOTest getInstance()
    {
        if (instance == null)
            instance = new RosterEntryDAOTest();
        return instance;
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
