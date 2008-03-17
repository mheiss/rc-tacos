package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Login;

/**
 * This class manages the login information for each user that can be edited in the
 * administration section.
 * @author Michael
 */
public class LoginManager extends PropertyManager
{
	//the list
    private List<Login> objectList = new ArrayList<Login>();

    /**
     * Default class constructor
     */
    public LoginManager() { }

    /**
     * Adds a new login to the list
     * @param login the login info to add
     */
    public void add(final Login login) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(login);
                //notify the view
                firePropertyChange("LOGIN_ADD", null, login);
            }
        }); 
    }    

    /**
     * Removes the login information from the list
     * @param login the login information to remove
     */
    public void remove(final Login login) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(login);
                firePropertyChange("LOGIN_REMOVE", login, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the login information in the list
     * @param login the login information to update
     */
    public void update(final Login login) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
            	//assert we have this login
            	if(!objectList.contains(login))
            		return;
                //get the position of the entry
                int id = objectList.indexOf(login);
                objectList.set(id, login);
                firePropertyChange("LOGIN_UPDATE", null, login); 
            }
        }); 
    }
    
    /**
     * Removes all elements form the list
     */
    public void removeAllEntries()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("LOGIN_CLEAR",null,null);
            }
        }); 
    }
    
    /**
     * Returns the given login information by the username
     * @param username the username to get the login information
     * @return the accociated login info or null if nothing found
     */
    public Login getLoginByUsername(String username)
    {
        //loop and search
        for(Login login :objectList)
        {
            if(login.getUsername().equalsIgnoreCase(username))
            	return login;
        }
        //nothing found
        return null;
    }
    
    /**
     * Returns all login informations in the list
     * @return the login list
     */
    public List<Login> getLocations()
    {
        return objectList;
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }
}
