package at.rc.tacos.core.db.dao.memory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public Integer addRosterEntry(RosterEntry entry)
    {
        rosterList.add(entry);
        return rosterList.size();
    }
    
    @Override
    public boolean updateRosterEntry(RosterEntry entry)
    {
        int index = rosterList.indexOf(entry);
        rosterList.remove(index);
        rosterList.add(index,entry);
        
        return true;
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
    public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime)
    {
        List<RosterEntry> resultList = new ArrayList<RosterEntry>();

        String filterStart = formatDate(startTime);
                
        //loop and compare
        for(RosterEntry entry:rosterList)
        {
            String entryStart = formatDate(entry.getPlannedStartOfWork());
            String entryEnd = formatDate(entry.getPlannedEndOfWork());
            //filter the entries at the date
            if(entryStart.equalsIgnoreCase(filterStart))
                resultList.add(entry);
            if(!entryStart.equalsIgnoreCase(entryEnd))
            {
                if(filterStart.equalsIgnoreCase(entryEnd))
                    resultList.add(entry);
            }
        }
        return resultList;
    }
    
    /** 
     * Helper method to format the date and return a string 
     * @param date the date to format
     * @return the formatted string
     */
    private String formatDate(long date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return sdf.format(cal.getTime());
    }
}
