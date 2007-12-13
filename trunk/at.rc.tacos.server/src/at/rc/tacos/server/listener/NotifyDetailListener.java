package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon NotifyDetail changes
 * @author Michael
 */
public class NotifyDetailListener extends ServerListenerAdapter
{
    private CallerDAO callerDao = DaoService.getInstance().getFactory().createNotifierDAO();
    
    /**
     * Add a notifier detail
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        NotifierDetail detail = (NotifierDetail)addObject;
        callerDao.addCaller(detail);
        return detail;
    }

    /**
     * Listing of all notifiers
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(callerDao.listCallers());
        return list;
    }

    /**
     * Remove a mobile phone
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        NotifierDetail detail = (NotifierDetail)removeObject;
        callerDao.removeCaller(detail);
        return detail;
    }

    /**
     * Update a notifier detail
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        NotifierDetail detail = (NotifierDetail)updateObject;
        callerDao.updateCaller(detail);
        return detail;
    }
}
