package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.factory.ImageFactory;

/**
 * Provides an action to close the current editor.
 * @author Michael
 */
public class EditorCloseAction extends Action implements IWorkbenchWindowActionDelegate 
{
	private IWorkbenchWindow window;

	/**
	 * Default class constructor for discard changes and close the editor
	 */
	public EditorCloseAction(IWorkbenchWindow window)
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
		return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.remove");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run()
	{
		//The active editor
		IEditorPart editor = window.getActivePage().getActiveEditor();
		//close the editor
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