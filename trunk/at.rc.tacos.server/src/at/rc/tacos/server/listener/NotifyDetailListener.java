package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;
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
        CallerDetail detail = (CallerDetail)addObject;
        try
        {
            callerDao.addCaller(detail);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return detail;
    }

    /**
     * Listing of all notifiers
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        try
        {
            list.addAll(callerDao.listCallers());
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return list;
    }

    /**
     * Remove a mobile phone
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        CallerDetail detail = (CallerDetail)removeObject;
        try
        {
            callerDao.removeCaller(0);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return detail;
    }

    /**
     * Update a notifier detail
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        CallerDetail detail = (CallerDetail)updateObject;
        try
        {
            callerDao.updateCaller(detail,0);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return detail;
    }
}
