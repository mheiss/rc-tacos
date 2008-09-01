package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Assigns the in the vehicle table of the transport form selected vehicle to the current transport
 * Move the transport from the outstanding to the underway transports by setting the programStatus of the transport
 * Set the transport status 'order placed' at the time
 * Assign the vehicle
 * Set the user which has execute this step (disposed user)
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
