package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * This action opens the about dialog of the application
 * @author Michael
 *
 */
public class OpenAboutAction extends Action
{
    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnet einen Informationsdialog";
    }
    
    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Information";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return null;
    }
    
    /**
     * Shows the abut dialog of the application
     */
    @Override
    public void run()
    {
        ActionFactory.ABOUT.create(PlatformUI.getWorkbench().getActiveWorkbenchWindow()).run();
    }
}
