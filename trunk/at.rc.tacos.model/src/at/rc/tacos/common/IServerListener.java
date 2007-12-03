package at.rc.tacos.common;

/**
 * This interface describes the methods that the server must
 * provide to handle client requests.
 * @author Michael
 */
public interface IServerListener
{
    /**
     * Add request from the client to handle.
     * @param addObject the object to add
     */    
    public void handleAddRequest(AbstractMessage addObject);
    
    /**
     * Remove request from the client to handle
     * @param removeObject the object to remove
     */
    public void handleRemoveRequest(AbstractMessage removeObject);
    
    /**
     * Update request from the client to handle
     * @param updateObject the object to update
     */
    public void handleUpdateRequest(AbstractMessage updateObject);
    
    /**
     * Listing request from the client to handle
     */
    public void handleListingRequest();
    
    /**
     *  Request from the client to login
     */
    public void handleLogin(AbstractMessage message);
    
    /**
     * Request from the client to logout
     */
    public void handleLogout(AbstractMessage message);
}
