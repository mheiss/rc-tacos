package at.rc.tacos.client.listeners;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IEventListener;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;

public class AuthenticationListener implements IEventListener
{
    @Override
    public void loginMessage(AbstractMessage message)
    {
        Login login = (Login)message;
        System.out.println(login);
    }

    @Override
    public void logoutMessage(AbstractMessage message)
    {
        Logout logout = (Logout)message;
        System.out.println(logout);
    }
    
    //NOT HANDLED BY THIS LISTENER
    @Override
    public void statusMessage(AbstractMessage message) { }
}
