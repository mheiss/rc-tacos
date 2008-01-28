package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.util.MyUtils;

/**
 * This class will be notified uppon roster entry updates
 * @author Michael
 */
public class RosterEntryListener extends ServerListenerAdapter
{
	private RosterDAO rosterDao = DaoFactory.MYSQL.createRosterEntryDAO();

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
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all roster entries is denied.");
			return list;
		}
		else if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			list.addAll(rosterDao.listRosterEntryByDate(dateStart, dateStart));
		}
		else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
		{
			//get the query filter
			final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			list.add(rosterDao.getRosterEntryById(id));
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
		rosterDao.removeRosterEntry(entry.getRosterId());
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
