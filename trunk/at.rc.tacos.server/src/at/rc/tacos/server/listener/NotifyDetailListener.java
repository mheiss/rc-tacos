package at.rc.tacos.server.listener;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.CallerDetail;

/**
 * This class will be notified uppon NotifyDetail changes
 * @author Michael
 */
public class NotifyDetailListener extends ServerListenerAdapter
{
    private CallerDAO callerDao = DaoFactory.TEST.createNotifierDAO();
    
    /**
     * Add a notifier detail
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        CallerDetail detail = (CallerDetail)addObject;
        callerDao.addCaller(detail);
        return detail;
    }

    /**
     * Update a notifier detail
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        CallerDetail detail = (CallerDetail)updateObject;
        callerDao.updateCaller(detail);
        return detail;
    }
}
