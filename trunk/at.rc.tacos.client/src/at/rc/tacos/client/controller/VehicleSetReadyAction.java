package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

/**
 * Sets the status of this vehicle to ready for action
 * @author Michael
 */
public class VehicleSetReadyAction extends Action
{
    //properties
    private VehicleDetail detail;
    private IWorkbenchWindow window;
    
    /**
     * Default class constructor specifying the vehicle
     * @param window the active workbench window
     * @param vehicle the vehicle to update
     */
    public VehicleSetReadyAction(IWorkbenchWindow window,VehicleDetail detail)
    {
        this.detail = detail;
        this.window = window;
    }
    
    /**
     * Checks the vehicle and runs the action
     */
    @Override 
    public void run()
    {
        //first check if we have at least a driver
        if(detail.getDriver() == null)
        {
            MessageDialog.openError(window.getShell(),
                    "Fehler beim setzen des Status 'Einsatzbereit'",
                    "Dem Fahrzeug "+detail.getVehicleName()+" kann nicht der Status 'Einsatzbereit' gegeben werden.\n" +
                    "Bitte weisen Sie dem Fahrzeug zuerst einen Fahrer zu.");
            return;
        }
        //reset the status
        detail.setOutOfOrder(false);
        detail.setReadyForAction(true);
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
