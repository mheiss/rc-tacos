package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.factory.ImageFactory;

public class OpenEmergencyTransportAction extends Action
{
    /**
     * Returns the tool tip text for the action
     * @return the tool tip text
     */
    @Override
    public String getToolTipText() 
    {
        return "�ffnet ein Fenster um einen neuen Einsatz zu erstellen";
    }
    
    /**
     * Returns the text to show in the tool bar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Notfall";
    }
    
    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.icon.folderblue");
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
    	TransportForm window = new TransportForm("emergencyTransport");
		window.open();
    }

}
