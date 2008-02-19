package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.ServiceTypeEditor;
import at.rc.tacos.client.editors.ServiceTypeEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.ServiceType;;

/**
 * Opens a new editor to create a service type
 * @author Michael
 */
public class EditorNewServiceTypeAction extends Action
{
	private IWorkbenchWindow window;
	
	/**
     * Default class constructor for creating the editor
     */
    public EditorNewServiceTypeAction(IWorkbenchWindow window)
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
        return "Öffnen ein neues Fenster um ein Dienstverhältnis anzulegen.";
    }

    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Dienstverhältnis hinzufügen";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.create");
    }

    /**
     * Opens the editor to create the service type
     */
    @Override
    public void run()
    {
    	IWorkbenchPage page = window.getActivePage();
        try 
        {
        	//create a new service type
        	ServiceType newServiceType = new ServiceType();
        	//create the editor input
        	ServiceTypeEditorInput input = new ServiceTypeEditorInput(newServiceType,true);
        	//try to open the editor
            page.openEditor(input, ServiceTypeEditor.ID);
        } 
        catch (PartInitException e) 
        {
            Activator.getDefault().log("Failed to create a new serviceType editor", IStatus.ERROR);
        }
    }
}