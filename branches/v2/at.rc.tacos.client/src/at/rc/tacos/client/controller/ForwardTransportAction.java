package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Assigns the transport as orwarded
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

		//check if the object is currently locked
		String resultLockMessage = LockManager.sendLock(Transport.ID, transport.getTransportId());
		
		//check the result of the lock
		if(resultLockMessage != null)
		{
			boolean forceEdit =  MessageDialog.openQuestion(
					Display.getCurrent().getActiveShell(), 
					"Information: Eintrag wird bearbeitet", 
					"Der Transport den Sie weiterleiten m�chten wird bereits von "+ resultLockMessage+ " bearbeitet.\n"+
					"Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern f�hren!\n\n"+
					"Es wird dringend empfohlen, den Eintrag erst nach Freigabe durch " +resultLockMessage +" zu stornieren!\n\n"+
					"M�chten Sie den Eintrag trotzdem sofort weiterleiten?");
			if(!forceEdit)
				return;
			//log the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log("Der Eintrag "+transport+" wird trotz Sperrung durch "+resultLockMessage +" von "+username+" bearbeitet",Status.WARNING);
		}
		
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
		
		//remove the lock from the object
		LockManager.removeLock(Transport.ID, transport.getTransportId());
	}
}
