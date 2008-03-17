package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LoginManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IConnectionStates;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.SystemMessage;

public class SessionListener extends ClientListenerAdapter
{
	protected SessionManager session = SessionManager.getInstance();
	protected LoginManager manager = ModelFactory.getInstance().getLoginManager();
	
	@Override
	public void add(AbstractMessage addMessage) 
	{
		if(addMessage instanceof Login)
		{
			Login addLogin = (Login)addMessage;
			manager.add(addLogin);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage) 
	{
		if(removeMessage instanceof Login)
		{
			Login removeLogin = (Login)removeMessage;
			manager.remove(removeLogin);
		}
	}

	@Override
    public void loginMessage(AbstractMessage message)
    {
    	//cast to a login message
        Login login = (Login)message;
        //check the login
        if (login.isLoggedIn())
        {
        	session.fireLoginSuccessfully(login);
        	//log the message
        	Status status = new Status(IStatus.OK,Activator.PLUGIN_ID,"Successfully authenticated "+login.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
        else if(login.isIslocked())
        {
        	session.fireLoginDenied(login);
        	Status status = new Status(IStatus.WARNING,Activator.PLUGIN_ID,"User is locked and not allowed to login:"+login.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
        else
        {
        	session.fireLoginDenied(login);
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
        	session.fireLogout();
            Status status = new Status(IStatus.INFO,Activator.PLUGIN_ID,"Successfully logged out the user: "+logout.getUsername()); 
            Activator.getDefault().getLog().log(status);
        }
    }

	@Override
	public void connectionChange(int status) 
	{
		if(status == IConnectionStates.STATE_DISCONNECTED)
			session.fireConnectionLost();
	}

	@Override
	public void transferFailed(String contentType,String queryType,AbstractMessage message) 
	{
		session.fireTransferFailed(contentType,queryType,message);
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
			session.setDayInfoMessage(dayInfo);
		}
		//do we have a login message?
		if(updateMessage instanceof Login)
		{
			Login login = (Login)updateMessage;
			manager.update(login);
		}
	}

	@Override
	public void list(ArrayList<AbstractMessage> listMessage) 
	{
		//get the first item to check the type
		AbstractMessage listObject = listMessage.get(0);
		if(listObject instanceof DayInfoMessage)
		{
			update(listObject);
		}
		if(listObject instanceof Login)
		{
			manager.removeAllEntries();
			//loop and add all logins
			for(AbstractMessage abstractObject:listMessage)
			{
				Login login = (Login)abstractObject;
				manager.add(login);
			}
		}
	}
}
