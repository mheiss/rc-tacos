package at.rc.tacos.client.controller;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Opens the editor to edit the selected entry (stornieren)
 * @author b.thek
 */
public class CancelTransportAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	private String transportNumberOld;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public CancelTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Transport stornieren");
		setToolTipText("Storniert den Transport und verschiebt ihn in das Journalblatt");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		
		
		//confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Transport stornieren", "Möchten Sie den Transport (von: " +transport.getFromStreet() +") wirklich stornieren?");
		if (!cancelConfirmed) 
			return;
		//request to cancel (storno)	
		transportNumberOld = transport.getTransportNumber(); //TODO - Transportnummernverwaltung? old in Hilfstabelle? -oder alles am Server?
		transport.setTransportNumber("STORNO");
		transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
