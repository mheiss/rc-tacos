package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.LocationEditor;
import at.rc.tacos.client.editors.LocationEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Location;

/**
 * Opens an empty editor to create a new location
 * @author Michael
 */
public class EditorNewLocationAction extends Action
{
	private IWorkbenchWindow window;
	
	/**
     * Default class constructor for creating the editor
     */
    public EditorNewLocationAction(IWorkbenchWindow window)
	{
		this.window = window;
	}

    /**
     * Returns the tooltip text for the action
     * @return the tooltip text
     */
    @Override
    public String getToolTipText() 
    {
        return "Öffnen ein neues Fenster um eine Dienststelle anzulegen.";
    }

    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Dienststelle hinzufügen";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("image.admin.add");
    }

    /**
     * Opens the editor to create the location
     */
    @Override
    public void run()
    {
    	IWorkbenchPage page = window.getActivePage();
        try 
        {
        	//create a new location
        	Location newLocation = new Location();
        	//create the editor input
        	LocationEditorInput input = new LocationEditorInput(newLocation,true);
        	//try to open the editor
            page.openEditor(input, LocationEditor.ID);
        } 
        catch (PartInitException e) 
        {
            Activator.getDefault().log("Failed to create a new location editor", IStatus.ERROR);
        }
    }
}