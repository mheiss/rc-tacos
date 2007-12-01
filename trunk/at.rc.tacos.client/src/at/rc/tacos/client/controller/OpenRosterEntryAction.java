package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.view.RosterEntryForm;
import at.rc.tacos.factory.ImageFactory;

public class OpenRosterEntryAction extends Action
{
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnet ein Fenster um einen Dienstplan eintrag zu erstellen";
    }
    
    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.icon.calendar");
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
//        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Dummy", "This is a dummy implementation");
        RosterEntryForm window = new RosterEntryForm();
		window.open();
    }

}
