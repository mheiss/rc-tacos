package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.view.RosterEntryForm_old;
import at.rc.tacos.factory.ImageFactory;

public class PersonalNewEntryAction extends Action
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
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Dienstplaneintrag";
    }
    
    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.icon.addrosterentry");
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
        RosterEntryForm_old window = new RosterEntryForm_old();
		window.open();
    }
}
