package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * Assigns the in the vehicle table of the transport form selected vehicle to the current transport
 * @author b.thek
 */
public class AssignCarDirectAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	private Transport transport;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public AssignCarDirectAction(TableViewer viewer, Transport transport)
	{
		this.viewer = viewer;
		this.transport = transport;
	}
	
	@Override
	public void run()
	{	
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		VehicleDetail vehicle = (VehicleDetail)((IStructuredSelection)selection).getFirstElement();
		System.out.println("............ AssignCarDirectAction, vehicle: " +vehicle.getVehicleName());
		//open the editor
		transport.setVehicleDetail(vehicle);
		transport.setDisposedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
		GregorianCalendar cal = new GregorianCalendar();
		long now = cal.getTimeInMillis();
		transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, now);
		transport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		
		
		
	}
}
