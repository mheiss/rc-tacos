package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
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
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
	{
		RosterEntry entry = (RosterEntry)addObject;
		int id = rosterDao.addRosterEntry(entry);
		if(id == -1)
			throw new DAOException("RosterEntryListener","Failed to add the roster entry "+entry);
		entry.setRosterId(id);
		return entry;
	}

	/**
	 * Listing of all entries 
	 */
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<RosterEntry> rosterList;
		
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all roster entries is denied.");
			throw new DAOException("RosterEntryListener","Listing of all roster entries is denied");
		}
		else if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			rosterList = rosterDao.listRosterEntryByDate(dateStart, dateEnd);
			if(rosterList == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("RosterEntryListener","Failed to list the roster entries by date from "+time);
			}
			list.addAll(rosterList);
		}
		else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
		{
			//get the query filter
			final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			RosterEntry entry = rosterDao.getRosterEntryById(id);
			if(entry == null)
				throw new DAOException("RosterEntryListener","Failed to list the roster entry by id:"+id);
			list.add(entry);
		}
		//return the list
		return list;
	}

	/**
	 * Remove a roster entry
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
	{
		RosterEntry entry = (RosterEntry)removeObject;
		if(!rosterDao.removeRosterEntry(entry.getRosterId()))
			throw new DAOException("RosterEntryListener","Failed to remove the roster entry:"+entry);
		return entry;
	}

	/**
	 * Update a roster entry
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
	{
		RosterEntry entry = (RosterEntry)updateObject;
		if(!rosterDao.updateRosterEntry(entry))
			throw new DAOException("RosterEntryListener","Failed to update the roster entry:"+entry);
		return entry;
	}
}
