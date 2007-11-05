package at.rc.tacos.core.service;

/**
 * The network layer defines the methods for the tacos net
 * @author Michael
 */
public interface INetLayer 
{
	/** Sends the login request to the server */
	public void login(String username,String password);
}
