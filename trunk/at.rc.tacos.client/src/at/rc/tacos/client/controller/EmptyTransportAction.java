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
 * Assigns the transport as an empty transport
 * @author b.thek
 */
public class EmptyTransportAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public EmptyTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Leerfahrt");
		setToolTipText("Macht aus dem Transport eine Leerfahrt");
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
				"Leerfahrt", "Soll die Fahrt (von: " +transport.getFromStreet()+"wirklich als Leerfahrt abgelegt werden? Die Transportnummer bleibt dabei erhalten.");
		if (!cancelConfirmed) 
			return;
		transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
		transport.setNotes(transport.getNotes() + "/" +"Leerfahrt!");
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
