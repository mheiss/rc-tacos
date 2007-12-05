package at.rc.tacos.client.listeners;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.SystemMessage;

/**
 * This class will handle the system event messages
 * @author Michael
 */
public class SystemMessageListener extends ClientListenerAdapter
{
    @Override
    public void systemMessage(AbstractMessage message)
    {
        SystemMessage sysMessage = (SystemMessage)message;
        System.out.println("System message: " + sysMessage);
    }
}
