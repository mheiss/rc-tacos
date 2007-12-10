package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelListener;

/**
 * An abstract adapter class for handling server responses.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * @author Michael
 */
public abstract class ClientListenerAdapter implements IModelListener
{
    /**
     *  Add request to handle
     */
    @Override
    public void add(AbstractMessage addMessage)
    {
                
    }

    /**
     * Listing of items to handle
     */
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {

    }

    /**
     * Login response to handle
     */
    @Override
    public void loginMessage(AbstractMessage message)
    {

    }

    /**
     * Logout response to handle
     */
    @Override
    public void logoutMessage(AbstractMessage message)
    {
               
    }

    /**
     * Remove request to handle
     */
    @Override
    public void remove(AbstractMessage removeMessage)
    {
                
    }

    /**
     * System message to handle
     */
    @Override
    public void systemMessage(AbstractMessage message)
    {
                
    }

    /**
     * Update message to handle
     */
    @Override
    public void update(AbstractMessage updateMessage)
    {
                
    }
}
