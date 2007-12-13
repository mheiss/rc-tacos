package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.*;

public interface RosterDAO 
{
    public int addRosterEntry(RosterEntry entry);
    public void updateRosterEntry(RosterEntry entry);
    public void removeRosterEntry(RosterEntry entry);
    
    public RosterEntry getRosterEntryById(int rosterEntryId);    
	public List<RosterEntry> listRosterEntrys();
	public List<RosterEntry> listRosterEntryByEmployee(int emplyeeID);
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime);
}
