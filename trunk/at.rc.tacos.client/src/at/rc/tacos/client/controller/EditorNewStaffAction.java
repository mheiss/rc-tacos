package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.StaffMemberEditor;
import at.rc.tacos.client.editors.StaffMemberEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

/**
 * Opens an empty editor to create a new staff member and the login data.
 * @author Michael
 */
public class EditorNewStaffAction extends Action
{
	private IWorkbenchWindow window;
	
	/**
     * Default class constructor for creating the editor
     */
    public EditorNewStaffAction(IWorkbenchWindow window)
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
        return "Öffnen ein neues Fenster um einen Mitarbeiter anlegen zu können.";
    }

    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Mitarbeiter anlegen";
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
     * Opens the editor to create the staff
     */
    @Override
    public void run()
    {
    	IWorkbenchPage page = window.getActivePage();
        try 
        {
        	//create a new staff member and login
        	StaffMember newMember = new StaffMember();
        	Login newLogin = new Login();
        	//create the editor input
        	StaffMemberEditorInput input = new StaffMemberEditorInput(newMember,newLogin,true);
        	//try to open the editor
            page.openEditor(input, StaffMemberEditor.ID);
        } 
        catch (PartInitException e) 
        {
            Activator.getDefault().log("Failed to create a new staff member editor", IStatus.ERROR);
        }
    }
}