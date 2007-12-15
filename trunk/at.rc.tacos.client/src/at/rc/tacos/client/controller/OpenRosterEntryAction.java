package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
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
        return "�ffnet ein Fenster um einen Dienstplan eintrag zu erstellen";
    }
    
    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Neuer Dienstplaneintrag";
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
        RosterEntryForm window = new RosterEntryForm();
		window.open();
    }

}
