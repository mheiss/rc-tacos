package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

/**
 * Sets the status of this vehicle to ready for action
 * @author Birgit
 */
public class VehicleTableSetReadyAction extends Action
{
    //properties
    private TableViewer viewer;
    
    /**
     * Default class constructor specifying the vehicle
     * @param window the active workbench window
     * @param vehicle the vehicle to update
     */
    public VehicleTableSetReadyAction(TableViewer viewer)
    {
    	this.viewer = viewer;
    }
    
    /**
     * Checks the vehicle and runs the action
     */
    @Override 
    public void run()
    {
    	//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		VehicleDetail detail = (VehicleDetail)((IStructuredSelection)selection).getFirstElement();
				
		
        //first check if we have at least a driver
        if(detail.getDriver() == null)
        {
        	MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Fehler beim setzen des Status 'Einsatzbereit'",
        			"Dem Fahrzeug "+detail.getVehicleName()+" kann nicht der Status 'Einsatzbereit' gegeben werden.\n" +
                    "Bitte weisen Sie dem Fahrzeug zuerst einen Fahrer zu.");
        	return;
        }
        	
            
        
        //reset the status
        detail.setOutOfOrder(false);
        detail.setReadyForAction(true);
        if(detail.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_NA)
            detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
    }
    
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Setzt den Status 'Einsatzbereit'";
    }

    /**
     * Returns the text for the context menu item
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Status Einsatzbereit";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.vehicleReady");
    }
}
