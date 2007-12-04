package at.rc.tacos.core.db.dao.mysql;

import java.util.List;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.RosterEntry;

public class RosterDAOMySQL implements RosterDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

    @Override
    public int addRosterEntry(RosterEntry entry)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public void updateRosterEntry(RosterEntry entry)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeRosterEntry(RosterEntry rosterEntry)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public RosterEntry getRosterEntryById(int rosterEntryId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RosterEntry> listRosterEntryByEmployee(int emplyeeID)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RosterEntry> listRosterEntryByTime(long startTime, long endTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RosterEntry> listRosterEntrys()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
