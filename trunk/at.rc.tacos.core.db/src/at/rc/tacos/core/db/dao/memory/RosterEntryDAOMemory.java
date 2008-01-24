package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.util.MyUtils;

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
		return rosterList.size();
	}

	@Override
	public boolean updateRosterEntry(RosterEntry entry)
	{
		int index = rosterList.indexOf(entry);
		rosterList.set(index, entry);
		return true;
	}

	@Override
	public boolean removeRosterEntry(int id)
	{
		if(rosterList.remove(id) != null)
			return true;
		//Nothing removed 
		return false;
	}

	@Override
	public RosterEntry getRosterEntryById(int id)
	{
		return rosterList.get(id);
	}

	@Override
	public List<RosterEntry> listRosterEntryByStaffMember(int staffMemberId)
	{
		List<RosterEntry> resultList = new ArrayList<RosterEntry>();
		for(RosterEntry entry:rosterList)
		{
			if(entry.getStaffMember().getStaffMemberId() == staffMemberId)
				resultList.add(entry);
		}
		return resultList;
	}

	@Override
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime)
	{
		List<RosterEntry> resultList = new ArrayList<RosterEntry>();

		//loop and compare
		for(RosterEntry entry:rosterList)
		{
			long entryStart = entry.getPlannedStartOfWork();
			//filter the entries at the date
			if(MyUtils.isEqualDate(startTime, entryStart))
				resultList.add(entry);
		}
		return resultList;
	}
}
