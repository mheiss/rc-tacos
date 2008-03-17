package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;


/**
 * The action that is triggered by creating a new dialysis entry
 * @author b.thek
 */
public class CreateDialysisTransportAction extends Action 
{
    private DialysisPatient dia;
    
    /**
     * Creates a new TransportAction.
     * @param entry the new transport
     */
    public CreateDialysisTransportAction(DialysisPatient dia) 
    {
        this.dia = dia;
    }

    @Override
	public void run() 
    {
        //send the transport
        NetWrapper.getDefault().sendAddMessage(DialysisPatient.ID, dia);
    }
}