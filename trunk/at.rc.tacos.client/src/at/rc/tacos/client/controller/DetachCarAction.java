package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

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
		setText("Fahrzeug abziehen");
		setToolTipText("Zieht das Fahrzeug vom Transport ab");
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
	}
}
