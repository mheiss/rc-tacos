package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

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
	private RosterDAO rosterDao = DaoFactory.SQL.createRosterEntryDAO();
	
	//the logger
	private static Logger logger = Logger.getLogger(RosterEntryListener.class);

	/**
	 * Add a roster entry
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException,SQLException
	{
		RosterEntry entry = (RosterEntry)addObject;
		int id = rosterDao.addRosterEntry(entry);
		if(id == -1)
			throw new DAOException("RosterEntryListener","Failed to add the roster entry "+entry);
		entry.setRosterId(id);
		logger.info("added by:" +username +";" +entry);
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
		} else if (queryFilter.containsFilterType(IFilterTypes.DATE_FILTER) && queryFilter.containsFilterType(IFilterTypes.ROSTER_LOCATION_FILTER)) {
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			
			int filterLocationId = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_LOCATION_FILTER));
			
			rosterList = rosterDao.listRosterEntriesByDateAndLocation(dateStart, dateEnd, filterLocationId);
			if(rosterList == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("RosterEntryListener","Failed to list the roster entries by date from "+time);
			}
			list.addAll(rosterList);
		}
		else if (queryFilter.containsFilterType(IFilterTypes.ROSTER_MONTH_FILTER) && queryFilter.containsFilterType(IFilterTypes.ROSTER_YEAR_FILTER) && queryFilter.containsFilterType(IFilterTypes.ROSTER_LOCATION_FILTER)) {
			int locationFilter = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_LOCATION_FILTER));
			int monthFilter = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_MONTH_FILTER));
			int yearFilter = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_YEAR_FILTER));
			int locationStaffMemberFilter = -1;
			if (queryFilter.containsFilterType(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER)) {
				locationStaffMemberFilter = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER));
			}
			String functionFilter = null;
			String functionStaffMemberCompetenceFilter = null;
			int staffMemberFilter = -1;
			if (queryFilter.containsFilterType(IFilterTypes.ROSTER_FUNCTION_JOB_SERVICE_TYPE_FILTER)) {
				functionFilter = queryFilter.getFilterValue(IFilterTypes.ROSTER_FUNCTION_JOB_SERVICE_TYPE_FILTER);
			}
			if (queryFilter.containsFilterType(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER)) {
				functionStaffMemberCompetenceFilter = queryFilter.getFilterValue(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER);
			}
			if (queryFilter.containsFilterType(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER)) {
				staffMemberFilter = Integer.parseInt(queryFilter.getFilterValue(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER));
			}
			rosterList = rosterDao.listRosterEntriesForRosterMonth(locationFilter, monthFilter, yearFilter, locationStaffMemberFilter, functionFilter, functionStaffMemberCompetenceFilter, staffMemberFilter);
			if(rosterList == null)
			{
				String time = "";
				throw new DAOException("RosterEntryListener","Failed to list the roster entries by date from "+time);
			}
			list.addAll(rosterList);
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
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException,SQLException
	{
		RosterEntry entry = (RosterEntry)updateObject;
		if(!rosterDao.updateRosterEntry(entry))
			throw new DAOException("RosterEntryListener","Failed to update the roster entry:"+entry);
		logger.info("updated by: " +username +";" +entry);
		return entry;
	}
}
