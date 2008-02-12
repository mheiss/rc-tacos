package at.rc.tacos.core.db.dao;

import java.util.List;

import at.rc.tacos.model.Login;

public interface UserLoginDAO 
{
    public static final String TABLE_NAME = "userlogin";
    
	/** 
	 * Successfully authenticateed the user.<br>
	 * This represents the value 0.
	 */
	public final static int LOGIN_SUCCESSFULL = 0;
	
	/**
	 * Failed to authenticate the user<br>
	 * The provided username or the password is wrong.<br>
	 * This represents the value -1.
	 */
	public final static int LOGIN_FAILED = -1;
	
	/**
	 * The account is locked an the user is not allowed to login.<br>
	 * This represents the value -2.
	 */
	public final static int LOGIN_DENIED = -2;
	
    /**
     * Adds a new login to the database, the connected staff member will also be created.
     * @param login the login object to create
     * @return the id of the created staffmember
     */
    public int addLogin(Login login);
    
    /**
     * Updates a login information in the database. This method will <b>NOT</b> update the staff member.
     * Please use the <code>StaffMemberDAO.updateStaffMember</code> to update the staff member.!<br>
     * This will update all fields in the database except the password field.
     * @param login the login information to update
     * @return true if the login was successfully
     */
    public boolean updateLogin(Login login);
    
    /**
     * Updates the password for the user in the database
     * @param username the user to update
     * @param newPwd the hash value of the new password
     * @return true if the update was successfully
     */
    public boolean updatePassword(String username,String newPwd);
    
    /**
     * Lists all logins stored in the database. The password filds are empty.
     */
    public List<Login> listLogins();
    
    /**
     * lockes a login
     * no login will be possible for this user
     * @param username
     * @return true if locking was successfull
     */
    public boolean lockLogin(String username);
	
    /**
     * unlockes a login
     * login will be possible again for this user
     * @param username
     * @return true if unlocking was successfull
     */
    public boolean unlockLogin(String username);
    
	/**
	 * Checks the username and the password hash agains the database.
	 * @param username the username 
	 * @param pwdHash the password to authenticate
	 * @return <code>UserDAOMemory.LOGIN_SUCCESSFULL</code> if the authentication was successfull
	 *  	   <code>UserDAOMemory.LOGIN_FAILED</code> if password or username was wrong
	 *		   <code>UserDAOMemory.LOGIN_DENIED</code> if the user is locked
	 */
    public int checkLogin(String username,String pwdHash);

    /**
     * Returns the accociated login object identified by the username
     * @param username the username to get the login from
     * @return the login object
     */
    public Login getLoginAndStaffmember(String username);
}
