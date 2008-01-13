package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.*;

public interface RosterDAO 
{
    public Integer addRosterEntry(RosterEntry entry) throws SQLException;
    public boolean updateRosterEntry(RosterEntry entry) throws SQLException;
    public void removeRosterEntry(RosterEntry entry) throws SQLException;
    
    public RosterEntry getRosterEntryById(int rosterEntryId) throws SQLException;  
	public List<RosterEntry> listRosterEntrys() throws SQLException;
	public List<RosterEntry> listRosterEntryByEmployee(int employeeID) throws SQLException;
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime) throws SQLException;
}
