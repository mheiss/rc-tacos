package at.rc.tacos.server.listener;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.DAOException;

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
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException
    {
        CallerDetail detail = (CallerDetail)addObject;
        int id = callerDao.addCaller(detail);
        if(id == -1)
        	throw new DAOException("NotifyDetailListener","Failed to add the caller:"+detail);
        return detail;
    }

    /**
     * Update a notifier detail
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException
    {
        CallerDetail detail = (CallerDetail)updateObject;
        if(!callerDao.updateCaller(detail))
        	throw new DAOException("NotifyDetailListener","Failed to update the caller:"+detail);
        return detail;
    }
}
