package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Detaches the car from the transport
 * @author b.thek
 */
public class DetachCarAction extends Action
{
	//properties
	private TableViewer viewer;
	private String transportNumberOld;
	
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
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Fahrzeug abziehen", "Möchten Sie das Fahrzeug wirklich vom Transport (" +transport.getFromStreet()+") abziehen?");
		if (!cancelConfirmed) 
			return;
		
		//---->
		transportNumberOld = transport.getTransportNumber(); //TODO - Transportnummernverwaltung?
		
		transport.clearVehicleDetail();//TODO ok = ?
//		transport.getStatusMessages().set(arg0, arg1); //TODO - clear all status messages!!!
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
