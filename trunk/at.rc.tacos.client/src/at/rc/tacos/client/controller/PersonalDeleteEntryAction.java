package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.RosterEntry;

/**
 * Opens the editor to edit the selected entry
 * @author Michael
 */
public class PersonalDeleteEntryAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public PersonalDeleteEntryAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag l�schen");
		setToolTipText("L�scht den Dienstplaneintrag");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		RosterEntry entry = (RosterEntry)((IStructuredSelection)selection).getFirstElement();
		//confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Dienstplaneintrag l�schen", "M�chten Sie den Dienstplaneintrag wirklich l�schen?");
		if (!cancelConfirmed) 
			return;
		//request to delete
		NetWrapper.getDefault().sendRemoveMessage(RosterEntry.ID, entry);
	}
}
