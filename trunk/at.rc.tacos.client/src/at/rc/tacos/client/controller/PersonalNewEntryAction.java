package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.RosterEntryForm;
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
        //open the editor
        Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        RosterEntryForm window = new RosterEntryForm(parent);
		window.open();
    }
}
