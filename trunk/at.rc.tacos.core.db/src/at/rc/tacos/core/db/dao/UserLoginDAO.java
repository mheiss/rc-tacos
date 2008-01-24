package at.rc.tacos.core.db.dao;

import at.rc.tacos.model.Login;

public interface UserLoginDAO 
{
	/** 
	 * Successfully authenticateed the user
	 */
	public final static int LOGIN_SUCCESSFULL = 0;
	
	/**
	 * Failed to authenticate the user<br>
	 * The provided username or the password is wrong
	 */
	public final static int LOGIN_FAILED = -1;
	
	/**
	 * The account is locked an the user is not allowed to login
	 */
	public final static int LOGIN_DENIED = -2;
	
    /**
     * Adds a new login to the database, the connected staff member will also be created.
     * @param login the login object to create
     * @return the id of the created login
     */
    public int addLogin(Login login);
    
    /**
     * Updates a login and the accociated staff member in the database.
     * @param login the login and the staff member to update.
     * @return true if the login was successfully
     */
    public boolean updateLogin(Login login);
    
    /**
     * Removes the login from the database.
     * The connected staff member will also be deleted.
     * @param id the id of the login to remove
     * @return true if the remove was successfull
     */
    public boolean removeLogin(int id);
	
	/**
	 * Checks the username and the password hash agains the database
	 * @param username the username 
	 * @param pwdHash the password to authenticate
	 * @return true if the authentication was successfull
	 */
    public boolean checkLogin(String username,String pwdHash);

    /**
     * Returns the accociated login object identified by the username
     * @param username the username to get the login from
     * @return the login object
     */
    public Login getLoginAndStaffmember(String username);
}
