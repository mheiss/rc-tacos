package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

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
        return "Öffnet ein Fenster um einen neuen Notfall Transport zu erstellen";
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
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createTransportEmergency"); 
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
    	//get the active shell
    	Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	
    	//create the window
    	TransportForm window = new TransportForm(parent,"emergencyTransport");
    	window.getShell().setVisible(false);
		window.create();
		
		//get the parent and the window shell
		Shell myShell = window.getShell();
	    myShell.setSize(1080, 680);
		
		//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		myShell.setLocation(locationX,locationY);
		
		//now open the window
		myShell.open();
		myShell.setVisible(true);
    }
}
