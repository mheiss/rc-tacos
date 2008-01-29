package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;


/**
 * The action that is triggered by creating a new transport
 * @author b.thek
 */
public class CreateTransportAction extends Action 
{
    private Transport transport;
    
    /**
     * Creates a new TransportAction.
     * @param entry the new transport
     */
    public CreateTransportAction(Transport transport) 
    {
        this.transport = transport;
    }

    public void run() 
    {
        //send the transport
        NetWrapper.getDefault().sendAddMessage(Transport.ID, transport);
    }
}