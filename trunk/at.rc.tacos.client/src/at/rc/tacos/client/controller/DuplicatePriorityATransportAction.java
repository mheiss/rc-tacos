package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;


/**
 * The action that is triggered by creating a new transport
 * @author b.thek
 */
public class DuplicatePriorityATransportAction extends Action implements IProgramStatus
{
    private Transport transport;
    
    /**
     * Creates a new TransportAction.
     * @param entry the new transport
     */
    public DuplicatePriorityATransportAction(Transport transport) 
    {
        this.transport = transport;
    }

    public void run() 
    {
        //duplicate the transport
    	Transport newTransport = transport;
    	newTransport.setTransportId(0);
    	newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
    	newTransport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
    	//assig nef vehicle
    	VehicleDetail nef = ModelFactory.getInstance().getVehicleList().getNEFVehicle();
    	transport.setVehicleDetail(nef);
    	//mark transport number (no number for the nef)
    	transport.setTransportNumber(-3);
    	NetWrapper.getDefault().sendAddMessage(Transport.ID,newTransport);
    	//TODO???? update vehicle???????
    }
}