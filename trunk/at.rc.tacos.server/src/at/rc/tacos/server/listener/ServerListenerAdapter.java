package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IServerListener;
import at.rc.tacos.model.QueryFilter;

/**
 * An abstract adapter class for receiving client requests.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * @author Michael
 */
public abstract class ServerListenerAdapter implements IServerListener
{
    /**
     * Handles login request
     */
    @Override
    public AbstractMessage handleLoginRequest(AbstractMessage loginObject)
    {
        return null;
    }
    
    /**
     * Handles logout request
     */
    @Override
    public AbstractMessage handleLogoutRequest(AbstractMessage logoutObject)
    {
        return null;
    }
    
   /**
    * Handles add requests
    */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        return null;
    }

   /**
    * Handles listing requests
    */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        return null;
    }

   /**
    * Handles remove requests
    */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        return null;
    }

    /**
     * Handles update requests
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        return null;
    }
}
