package at.rc.tacos.client.controller;


import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.util.DateValidator;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * Opens the editor to edit the selected entry (stornieren)
 * @author b.thek
 */
public class CancelTransportAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	
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
		InputDialog dlg = new InputDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Transport Stornierung", 
				"Bitte geben Sie Informationen zur Stornierung ein", 
				null,null);
		if (dlg.open() == Window.OK) 
		{
			transport.setNotes(transport.getNotes() +" Stornoinformation: " +dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			transport.setTransportNumber(Transport.TRANSPORT_CANCLED);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
	}
}
