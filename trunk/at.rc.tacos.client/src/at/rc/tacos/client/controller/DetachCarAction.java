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
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Detaches the car from the transport
 * @author b.thek
 */
public class DetachCarAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public DetachCarAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Abziehen des Fahrzeuges");
		setToolTipText("Zieht das Fahrzeug vom Transport ab");
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
					"Der Transport,von dem Sie das Fahrzeug abziehen möchten wird bereits von "+ resultLockMessage+ " bearbeitet.\n"+
					"Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"+
					"Es wird dringend empfohlen, das Fahrzeug erst nach Freigabe durch " +resultLockMessage +" abzuziehen!\n\n"+
					"Möchten Sie das Fahrzeug trotzdem abziehen?");
			if(!forceEdit)
				return;
			//log the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log("Der Eintrag "+transport+" wird trotz Sperrung durch "+resultLockMessage +" von "+username+" bearbeitet",Status.WARNING);
		}
		
		
		//confirm the cancel
		InputDialog dlg = new InputDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Transport Stornierung", 
				"Bitte geben Sie eine Begründung für das Abziehen des Fahrzeuges" +" " +transport.getVehicleDetail().getVehicleName() +" ein", 
				null,null);
		if (dlg.open() == Window.OK) 
		//confirm the cancel
		{
			transport.getStatusMessages().clear();
			transport.clearVehicleDetail();
			transport.setNotes(transport.getNotes() +"Fahrzeugabzug: " +dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
		
		//remove the lock from the object
		LockManager.removeLock(Transport.ID, transport.getTransportId());
	}
}
