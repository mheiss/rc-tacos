package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.SystemMessage;

public class SessionListener extends ClientListenerAdapter
{
    @Override
    public void loginMessage(AbstractMessage message)
    {
    	//cast to a login message
        Login login = (Login)message;
        //check the login
        if (login.isLoggedIn())
        {
        	SessionManager.getInstance().fireLoginSuccessfully(login);
        	//log the message
        	System.out.println("Successfully authenticated "+login.getUsername());
        	Status status = new Status(IStatus.OK,Activator.PLUGIN_ID,"Successfully authenticated "+login.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
        else
        {
        	SessionManager.getInstance().fireLoginDenied(login);
        	System.out.println("Failed to authenticate "+login.getUsername() +" Cause: "+login.getErrorMessage());
        	Status status = new Status(IStatus.WARNING,Activator.PLUGIN_ID,"Failed to authenticate "+login.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
    }

    @Override
    public void logoutMessage(AbstractMessage message)
    {
        //get the result and cast
        final Logout logout = (Logout)message;
        //check if the user is logged out
        if(logout.isLoggedOut())
        {
        	SessionManager.getInstance().fireLogout();
            System.out.println("Successfully logged out the user: "+logout.getUsername());
            Status status = new Status(IStatus.INFO,Activator.PLUGIN_ID,"Successfully logged out the user: "+logout.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
    }

	@Override
	public void connectionChange(int status) 
	{
		if(status == IConnectionStates.STATE_DISCONNECTED)
			SessionManager.getInstance().fireConnectionLost();
	}

	@Override
	public void transferFailed(String contentType,String queryType,AbstractMessage message) 
	{
		SessionManager.getInstance().fireTransferFailed(contentType,queryType,message);
	} 
	
    @Override
    public void systemMessage(AbstractMessage message)
    {
        SystemMessage sysMessage = (SystemMessage)message;
        //the log message
        Status status;
        
        if(sysMessage.getType() == SystemMessage.TYPE_INFO)
        	status = new Status(IStatus.INFO,Activator.PLUGIN_ID,sysMessage.getMessage());
        else if(sysMessage.getType() == SystemMessage.TYPE_ERROR)
        	status = new Status(IStatus.ERROR,Activator.PLUGIN_ID,sysMessage.getMessage());
        //log as an error message
        else 
        	status = new Status(IStatus.ERROR,Activator.PLUGIN_ID,sysMessage.getMessage());
        //log the message
        Activator.getDefault().getLog().log(status);
    }

	@Override
	public void update(AbstractMessage updateMessage) 
	{
		//do we have a dayInfo message?
		if (updateMessage instanceof DayInfoMessage) 
		{
			DayInfoMessage dayInfo = (DayInfoMessage) updateMessage;
			SessionManager.getInstance().setDayInfoMessage(dayInfo);
		}
	}

	@Override
	public void list(ArrayList<AbstractMessage> listMessage) 
	{
		//get the first item and pass the message
		update(listMessage.get(0));
	}
}
