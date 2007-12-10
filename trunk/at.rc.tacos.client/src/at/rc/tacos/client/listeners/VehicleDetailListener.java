package at.rc.tacos.client.listeners;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;

/**
 * This class will be notified uppon vehicle detail changes
 * @author Michael
 */
public class VehicleDetailListener extends ClientListenerAdapter
{
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        System.out.println("Listing of "+listMessage.size()+ " items");
    }
}
