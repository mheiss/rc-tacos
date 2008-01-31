package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class OpenViewAction extends Action 
{
	private final String viewId;

	public OpenViewAction(String viewId) 
	{
		this.viewId = viewId;
	}

	public void run() 
	{
		try 
		{
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			window.getActivePage().showView(viewId);
		} 
		catch (PartInitException e) 
		{
			e.printStackTrace();
		}
	}
}