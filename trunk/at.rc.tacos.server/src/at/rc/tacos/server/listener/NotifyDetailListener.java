package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;

/**
 * This class will be notified uppon NotifyDetail changes
 * @author Michael
 */
public class NotifyDetailListener extends ServerListenerAdapter
{
    private CallerDAO callerDao = DaoFactory.MYSQL.createNotifierDAO();
    
    /**
     * Add a notifier detail
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
    {
        CallerDetail detail = (CallerDetail)addObject;
        int id = callerDao.addCaller(detail);
        if(id == -1)
        	throw new DAOException("NotifyDetailListener","Failed to add the caller:"+detail);
        return detail;
    }
    
    /**
     * List notifier by id
     */
	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        //if there is no filter -> not supported
        if(queryFilter == null || queryFilter.getFilterList().isEmpty())
        {
        	System.out.println("Warning: Listing of all callers is not supported");
        	throw new DAOException("NotifierDetailListener","Listing of all callers is not supported");
        }
        else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            CallerDetail caller = callerDao.getCallerByID(id);
            if(caller == null)
            	throw new DAOException("NotifierDetailListener","Failed to get the caller by id:"+id);
            list.add(caller);
        }
        return list;
	}

	/**
     * Update a notifier detail
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
    {
        CallerDetail detail = (CallerDetail)updateObject;
        if(!callerDao.updateCaller(detail))
        	throw new DAOException("NotifyDetailListener","Failed to update the caller:"+detail);
        return detail;
    }
}
