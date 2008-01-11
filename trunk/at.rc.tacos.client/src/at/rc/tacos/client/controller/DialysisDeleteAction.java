package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected dialysis entry
 * @author b.thek
 */
public class DialysisDeleteAction extends Action
{
	//properties
	private TableViewer viewer;
	private String editingType;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public DialysisDeleteAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Dialyseeintrag löschen");
		setToolTipText("Löscht den Dialyseeintrag endgültig!");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		DialysisPatient dia = (DialysisPatient)((IStructuredSelection)selection).getFirstElement();
		//delete the entry
		//confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Dialyseeintrag löschen", "Möchten Sie den Dialyseeintrag wirklich löschen?");
		if (!cancelConfirmed) 
			return;
		//request to delete
		NetWrapper.getDefault().sendRemoveMessage(DialysisPatient.ID, dia);
	}
}
