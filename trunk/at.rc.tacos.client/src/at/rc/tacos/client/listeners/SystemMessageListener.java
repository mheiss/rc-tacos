package at.rc.tacos.client.listeners;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IEventListener;
import at.rc.tacos.model.SystemMessage;

/**
 * This class will handle the system event messages
 * @author Michael
 */
public class SystemMessageListener implements IEventListener
{
    @Override
    public void statusMessage(AbstractMessage message)
    {
        SystemMessage sysMessage = (SystemMessage)message;
        System.out.println(sysMessage);
    }
    
    //NOT HANDLED BY THIS LISTENER
    @Override
    public void loginMessage(AbstractMessage message) { }
    @Override
    public void logoutMessage(AbstractMessage message){ }
}
