package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected entry
 * @author b.thek
 */
public class ForwardTransportAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public ForwardTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Weiterleiten");
		setToolTipText("Verschiebt den Transport in das Journalblatt und setzt die Transportnummer auf \"WLTG\"");
	}
	
	@Override
	public void run()
	{
		
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();

		//confirm the cancel
		InputDialog dlg = new InputDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Weiterleitung", 
				"Bitte geben Sie Informationen zur Weiterleitung ein", 
				null,null);
		if (dlg.open() == Window.OK) 
		{
			transport.setNotes(transport.getNotes() +" Weiterleitungsinformation: " +dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			transport.setTransportNumber(Transport.TRANSPORT_FORWARD);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
	}
}
