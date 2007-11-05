package at.rc.tacos.core.service;

/**
 * The listener interface for the login
 * @author Michael
 */
public interface IClientLoginListener
{
    /** Login was successfully */
    public void loginSuccessfully();
    
    /** Login failed */
    public void loginFailed(String errorMessage);
}
