package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import at.rc.tacos.client.view.VehicleForm;
import at.rc.tacos.factory.ImageFactory;

public class CreateNewVehicle extends Action
{
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Fügt ein neues Fahrzeug der Fahrzeugübersicht hinzu";
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
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
        //create a new vehicle
//        VehicleDetail detail = new VehicleDetail("BM1","as","asfd");
//        Activator.getDefault().getVehicleManager().add(detail);
        VehicleForm window = new VehicleForm();
        window.open();
    }
}
