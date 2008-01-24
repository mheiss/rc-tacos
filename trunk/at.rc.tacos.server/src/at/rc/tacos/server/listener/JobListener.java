package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.QueryFilter;

public class JobListener extends ServerListenerAdapter
{
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        System.out.println("WARNING: this is not implemented");
        return addObject;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        System.out.println("WARNING: this is not implemented");
        return new ArrayList<AbstractMessage>();
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        System.out.println("WARNING: this is not implemented");
        return removeObject;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        System.out.println("WARNING: this is not implemented");
        return super.handleUpdateRequest(updateObject);
    }
}