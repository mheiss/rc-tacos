package at.rc.tacos.server.listener;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IServerListener;

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
    public void handleLogin(AbstractMessage message) { }

    /**
     * Handles logout request
     */
    @Override
    public void handleLogout(AbstractMessage message) { }

   /**
    * Handles add requests
    */
    @Override
    public void handleAddRequest(AbstractMessage addObject) { }

   /**
    * Handles listing requests
    */
    @Override
    public void handleListingRequest() { }

   /**
    * Handles remove requests
    */
    @Override
    public void handleRemoveRequest(AbstractMessage removeObject) { }

    /**
     * Handles update requests
     */
    @Override
    public void handleUpdateRequest(AbstractMessage updateObject) { }
}
