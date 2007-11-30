package at.rc.tacos.server.listener;

import java.util.ArrayList;

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
     * Handles model request
     */
    @Override
    public void handleRequest(String id, String action,
            ArrayList<AbstractMessage> requestList) { }

}
