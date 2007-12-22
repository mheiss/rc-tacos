package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.VehicleDetail;

public class EditVehicleAction extends Action
{
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Editiert ein vorhandenes Fahrzeut";
    }
    
    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Fahrzeug besetzen";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.icon.add");
    }
    
    /**
     * Shows the view to edit a vehicle
     */
    @Override
    public void run()
    {
        //create a new vehicle
        VehicleDetail detail = new TestDataSource().vehicleList.get(1);
        ModelFactory.getInstance().getVehicleManager().add(detail);
//        VehicleForm window = new VehicleForm();
//        window.open();
    }
}
