package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class will be notified uppon vehicle detail changes
 * @author Michael
 */
public class VehicleDetailListener extends ClientListenerAdapter
{    
    @Override
    public void add(AbstractMessage addMessage)
    {
        // TODO Auto-generated method stub
        super.add(addMessage);
    }

    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        VehicleManager manager = ModelFactory.getInstance().getVehicleManager();
        for(AbstractMessage msg:listMessage)
        {
            VehicleDetail entry = (VehicleDetail)msg;
            manager.add(entry);
        }
    }
}
