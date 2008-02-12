package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.factory.ImageFactory;

public class CloseEditorAction extends Action implements IWorkbenchWindowActionDelegate 
{
	private IWorkbenchWindow window;
	
	/**
     * Default class constructor for discard changes and close the editor
     */
    public CloseEditorAction(IWorkbenchWindow window)
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
        return "Schließt das aktuelle Fenster ohne die Änderungen zu speichern";
    }

    /**
     * Retruns the text to show in the toolbar
     * @return the text to render
     */
    @Override
    public String getText()
    {
        return "Fenster schließen";
    }

    /**
     * Returns the image to use for this action.
     * @return the image to use
     */
    @Override
    public ImageDescriptor getImageDescriptor() 
    {
        return ImageFactory.getInstance().getRegisteredImageDescriptor("images.admin.close");
    }

    /**
     * Shows the view to edit a vehicle
     */
    @Override
    public void run()
    {
    	//The active editor
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		//close the editor
		boolean result = MessageDialog.openConfirm(window.getShell(), 
				"Schließen und nicht speichern", 
				"Wollen Sie wirklich das Fenster für " + editor.getTitle()+ " schließen?\n" +
						"Alle nicht gespeicherten Änderungen gehen verloren");
		//close the editor
		if(result)
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, true);
    }

	@Override
	public void dispose() 
	{
	}

	@Override
	public void init(IWorkbenchWindow window) 
	{
	}

	public void run(IAction action) 
	{
	}

	public void selectionChanged(IAction action, ISelection selection) 
	{
	}
}