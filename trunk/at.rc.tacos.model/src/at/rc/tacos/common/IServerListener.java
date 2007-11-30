package at.rc.tacos.common;

import java.util.ArrayList;

/**
 * This interface describes the methods that the server must
 * provide to handle client requests.
 * @author Michael
 */
public interface IServerListener
{
    /**
     * Request from the client to handle.
     * @param id the identification of the model object
     * @param action the type of the action
     * @see IModelActions for details about the actions
     */    
    public void handleRequest(String id,String action,ArrayList<AbstractMessage> requestList);
    
    /**
     *  Request from the client to login
     */
    public void handleLogin(AbstractMessage message);
    
    /**
     * Request from the client to logout
     */
    public void handleLogout(AbstractMessage message);
}
