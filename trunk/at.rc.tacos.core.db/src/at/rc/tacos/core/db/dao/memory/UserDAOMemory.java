package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;

/**
 * Data source for login/logout
 * @author Michael
 */
public class UserDAOMemory implements UserLoginDAO
{
    //the shared instance
    private static UserDAOMemory instance;
    
    //the data list
    private List<Login> userList;
    
    /**
     * Default class constructor
     */
    private UserDAOMemory()
    {
        userList = new ArrayList<Login>();
        //add test data
        for(Entry<String,String> entry:TestDataSource.getInstance().userLogin.entrySet())
        {
        	Login login = new Login(entry.getKey(),entry.getValue(),false);
        	userList.add(login);
        }
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static UserDAOMemory getInstance()
    {
        if (instance == null)
            instance = new UserDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        userList.clear();
    }
    
    @Override
    public int checkLogin(String username,String password)
    {
        //loop over all user logins
        for(Login login:userList)
        {
            if(username.equals(login.getUsername()) && password.equals(login.getPassword()))
                return LOGIN_SUCCESSFULL;
        }
        //no valid login
        return LOGIN_FAILED;
    }
    
    @Override
    public int addLogin(Login login)
    {
    	//add the staffMember
    	StaffMember member = login.getUserInformation();
    	int staffId = StaffMemberDAOMemory.getInstance().addStaffMember(member);
    	member.setStaffMemberId(staffId);
    	
    	//add the login
    	userList.add(login);
    	return userList.size();
    }

	@Override
	public Login getLoginAndStaffmember(String username) 
	{
		//loop
		for(Login login:userList)
		{
			if(login.getUsername().equalsIgnoreCase(username))
				return login;
		}
		//nothing
		return null;
	}

	@Override
	public boolean removeLogin(int id) 
	{
		//the login
		Login login = userList.get(id);
		//assert valid
		if(login == null)
			return false;
		
		//remove the staff member
		StaffMemberDAOMemory.getInstance().removeStaffMember(login.getUserInformation().getStaffMemberId());
		//remove the login
		if(userList.remove(id) != null)
			return true;
		//nothing removed 
		return false;
	}

	@Override
	public boolean updateLogin(Login login) 
	{
		//update the staff member
		StaffMember member = login.getUserInformation();
		StaffMemberDAOMemory.getInstance().updateStaffMember(member);
		//update the login
		int index = userList.indexOf(login);
		userList.set(index, login);
		return true;	
	}
}
