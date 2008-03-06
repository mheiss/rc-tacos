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
	//the vehicle manager
	VehicleManager manager = ModelFactory.getInstance().getVehicleList();
	
	@Override
	public void add(AbstractMessage addMessage)
	{
		//cast to a vehicle and add it
        VehicleDetail detail = (VehicleDetail)addMessage;
        manager.add(detail);
	}
	
    @Override
    public void remove(AbstractMessage removeMessage)
    {
    	//cast to a vehicle and remove it
        VehicleDetail detail = (VehicleDetail)removeMessage;
        manager.remove(detail);
    }
	
	@Override
	public void update(AbstractMessage updateMessage)
	{
		//cast to a vehicle and add it
        VehicleDetail detail = (VehicleDetail)updateMessage;
        manager.update(detail);
	}
	
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
    	//remove all stored vehicles
    	manager.resetVehicles();
        //loop and add all vehicles
        for(AbstractMessage detailObject:listMessage)
        {
        	//cast to a vehicle and add it
            VehicleDetail detail = (VehicleDetail)detailObject;
            manager.add(detail);
        }
    }
}
