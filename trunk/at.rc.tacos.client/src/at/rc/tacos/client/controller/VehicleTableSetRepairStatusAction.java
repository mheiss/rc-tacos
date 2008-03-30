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
 * Sets the status of the vehicle to out of order
 * @author Birgit
 */
public class VehicleTableSetRepairStatusAction extends Action
{
    //properties
	private TableViewer viewer;
    
    /**
     * Default class constructor specifying the vehicle 
     * @param window the active workbench window
     * @param detail the vehicle to update
     */
    public VehicleTableSetRepairStatusAction(TableViewer viewer)
    {
        this.viewer = viewer;
    }
    
    /**
     * Sets the status and runs the action
     */
    @Override 
    public void run()
    {
    	//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		VehicleDetail detail = (VehicleDetail)((IStructuredSelection)selection).getFirstElement();
		
        boolean confirm = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                "Setzen des Status best‰tigen",
                "Wollen sie das Fahrzeug "+detail.getVehicleName()+" wirklich auﬂer Dienst stellen?");
        //check
        if(!confirm)
            return;
        //set the status
        detail.setReadyForAction(false);
        detail.setOutOfOrder(true);
        detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
        //send update request
        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
    }
    
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Setzt den Status 'Auﬂer Dienst'";
    }

    /**
     * Returns the text for the context menu item
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Status Auﬂer Dienst";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.vehicleOutOfOrder");
    }

}
