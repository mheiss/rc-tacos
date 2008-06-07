package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.AddressDAO;
import at.rc.tacos.core.db.dao.PeriodsDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Period;
import at.rc.tacos.model.QueryFilter;

public class PeriodListener extends ServerListenerAdapter
{
	//the database access
	private PeriodsDAO periodsDao = DaoFactory.SQL.createPeriodsDAO();
	//the logger
	private static Logger logger = Logger.getLogger(PeriodListener.class);

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException, SQLException 
	{
		Period newPeriod = (Period)addObject;
		//add to the database
		int id = periodsDao.addPeriod(newPeriod);
		if(id == -1)
			throw new DAOException("PeriodListener","Failed to add the period record: "+newPeriod);

		//set the id
		newPeriod.setPeriodId(id);
		logger.info("added by:" +username +";" +addObject);
		return newPeriod;
	}

	@Override
	public List<AbstractMessage> handleAddAllRequest(List<AbstractMessage> addList) throws DAOException,SQLException
	{
		logger.info("Period records added: "+addList.size()+ " Einträge");
		//loop and add all period recors
		for(AbstractMessage abstractPeriod: addList)
		{
			Period newPeriod = (Period)abstractPeriod;
			//add to the database
			int id = periodsDao.addPeriod(newPeriod);
			if(id == -1)
				throw new DAOException("PeriodListener","Failed to add the period record: "+newPeriod);
			newPeriod.setPeriodId(id);
		}
		return addList;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		Period period = (Period)removeObject;
		if(!periodsDao.removePeriod(period.getPeriodId()))
			throw new DAOException("PeriodListener","Failed to remove the period record: "+period);
		logger.info("removed: " + period);
		//just forward to the client
		return period;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException, SQLException 
	{
		Period period = (Period)updateObject;
		if(!periodsDao.updatePeriod(period))
			throw new DAOException("PeriodListener","Failed to update the period record: "+period);
		logger.info("updated by: " +username +";" +period);
		//just forward to the client
		return updateObject;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<Period> periodList;

		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all period records is denied.");
			throw new DAOException("PeriodListener","Listing of all period records is denied");
		}

//		else if(queryFilter.containsFilterType(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER))
//		{
//			//get the query filter
//			final String serviceTypeCompetenceFilter = queryFilter.getFilterValue(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER);
//		
//			periodList = periodsDao.getPeriodListByServiceTypeCompetence(serviceTypeCompetenceFilter);
//			if(periodList == null)
//			{
//				throw new DAOException("PeriodListener","Failed to list the periods by serviceTypeCompetence: "+serviceTypeCompetenceFilter);
//			}
//			list.addAll(periodList);
//		} 
		return list;
	}
}
