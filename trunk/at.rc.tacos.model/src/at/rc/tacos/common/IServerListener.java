package at.rc.tacos.common;

import java.util.ArrayList;

import at.rc.tacos.model.QueryFilter;

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
     * @return the response from the server
     */    
    public AbstractMessage handleAddRequest(AbstractMessage addObject);
    
    /**
     * Remove request from the client to handle
     * @param removeObject the object to remove
     * @return the response from the server
     */
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject);
    
    /**
     * Update request from the client to handle
     * @param updateObject the object to update
     * @return the response from the server
     */
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject);
    
    /**
     * Listing request from the client to handle
     * @param queryFilter the filter to apply
     * @return the response from the server
     * @throws Exception when a error occured during the listing
     */
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter);
    
    /**
     *  Request from the client to login
     *  @param message the login message to authenticate the user
     *  @return the result of the login process  
     */
    public AbstractMessage handleLoginRequest(AbstractMessage message);
    
    /**
     * Request from the client to logout
     * @param message the logout message to logout the user
     * @return the resolt of the logout process
     */
    public AbstractMessage handleLogoutRequest(AbstractMessage message);
}
