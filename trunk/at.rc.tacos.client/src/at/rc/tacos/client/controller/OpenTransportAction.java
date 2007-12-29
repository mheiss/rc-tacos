package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.factory.ImageFactory;

public class OpenTransportAction extends Action
{
    /**
     * Returns the tool tip text for the action
     * @return the tool tip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnet ein Fenster um einen neuen Transportauftrag zu erstellen";
    }
    
    /**
     * Returns the text to show in the tool bar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Transport";
    }
    
    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.icon.folderyellowg");
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
    	TransportForm window = new TransportForm("prebooking");
		window.open();
    }

}
